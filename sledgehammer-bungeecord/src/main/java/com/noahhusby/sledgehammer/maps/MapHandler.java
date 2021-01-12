/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - MapHandler.java
 *
 * Sledgehammer is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Sledgehammer is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.maps;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import com.noahhusby.sledgehammer.network.P2S.P2STeleportPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.warp.Warp;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class MapHandler {
    private static MapHandler mInstance = null;

    public static MapHandler getInstance() {
        if(mInstance == null) mInstance = new MapHandler();
        return mInstance;
    }

    public WebsocketEndpoint ws;

    List<MapSession> sessions = new ArrayList<>();
    private boolean isMapInitialized = false;
    private boolean heartbeat = false;

    /**
     * Create a new map session
     * @param sender The {@link CommandSender} that the session should be associated with
     */
    public void newMapCommand(CommandSender sender) {
        sessions.removeIf(ms -> (ms.name.equalsIgnoreCase(sender.getName())) && ms.timeout < System.currentTimeMillis());

        MapSession session = null;
        for(MapSession s : sessions)
            if(s.name.equalsIgnoreCase(sender.getName())) session = s;

        boolean sessionFound = session != null;
        if(!sessionFound) {
            session = new MapSession();
            session.name = sender.getName();
            session.key = UUID.randomUUID();
            session.time = System.currentTimeMillis();
            session.timeout = System.currentTimeMillis() + (60000*ConfigHandler.mapTimeout);
        }

        sender.sendMessage(ChatHelper.makeTitleMapComponent(new TextElement("Click here to access the warp map!", ChatColor.BLUE),
                ConfigHandler.mapLink+"/session?uuid="+session.name+"&key="+session.key));
        if(!sessionFound) sessions.add(session);
    }

    /**
     * Called by Sledgehammer-Map to invoke actions
     * @param message Message from Sledgehammer-Map
     */
    private void onIncomingMessage(String message) {
        JSONObject packet;
        try {
            packet = (JSONObject) new JSONParser().parse(message);
        } catch (ParseException e) {
            e.printStackTrace();
            Sledgehammer.logger.warning("Error while parsing map packet!");
            return;
        }

        switch((String) packet.get("action")) {
            // INIT PACKET
            case "init":
                String state = (String) packet.get("state");
                if(!state.trim().equalsIgnoreCase("success")) {
                    Sledgehammer.logger.info("Map initialization responded with error state: "+state);
                    return;
                }

                Sledgehammer.logger.info("Successfully initialized map!");
                isMapInitialized = true;
                break;

            // WARP PACKET
            case "warp":
                JSONObject data = (JSONObject) packet.get("data");
                String uuid = (String) data.get("uuid");
                String key = (String) data.get("key");
                String warpName = (String) data.get("warp");

                for(MapSession session : sessions) {
                    if(session.name.equalsIgnoreCase(uuid) && key.equalsIgnoreCase(session.key.toString())) {
                        //Warp warp = WarpHandler.getInstance().getWarp(warpName);
                        Warp warp = new Warp();
                        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(session.name);
                        if(player == null) return;

                        if(warp == null) {
                            player.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Error: Warp not found", ChatColor.RED)));
                            return;
                        }

                        if(player.getServer().getInfo() != ProxyServer.getInstance().getServerInfo(warp.getServer())) {
                            player.connect(ProxyServer.getInstance().getServerInfo(warp.getServer()));
                            player.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Sending you to ", ChatColor.GRAY), new TextElement(warp.getServer(), ChatColor.RED)));
                        }

                        player.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Warping to ", ChatColor.GRAY), new TextElement(warpName, ChatColor.RED)));
                        SledgehammerNetworkManager.getInstance().send(new P2STeleportPacket(session.name, warp.getServer(), warp.getPoint()));

                    }
                }
                break;

            // HEARTBEAT PACKET
            case "alive":
                heartbeat = false;
                break;
        }
    }

    /**
     * Attempts to create a connection to the map websocket
     */
    public void init() {
        try {
            ws = new WebsocketEndpoint(new URI("ws://"+ConfigHandler.mapHost+":"+ConfigHandler.mapPort));
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        ws.addMessageHandler(message -> getInstance().onIncomingMessage(message));

        if(ws.userSession == null) return;

        JSONObject packet = new JSONObject();
        JSONObject data = new JSONObject();

        data.put("title", ConfigHandler.mapTitle);
        data.put("subtitle", ConfigHandler.mapSubtitle);
        data.put("lat", ConfigHandler.startingLat);
        data.put("lon", ConfigHandler.startingLon);
        data.put("zoomLevel", ConfigHandler.startingZoom);
        data.put("auth", ConfigHandler.authenticationCode);

        packet.put("data", data);
        packet.put("action", "init");
        ws.sendMessage(packet.toJSONString());

        refreshWarps();
    }

    /**
     * Attempt to refresh the warps of the map
     */
    public void refreshWarps() {
        JSONObject packet = new JSONObject();
        JSONArray waypoints = new JSONArray();

        for(Warp w : WarpHandler.getInstance().getWarps()) {
            JSONObject waypoint = new JSONObject();

            waypoint.put("name", ChatHelper.capitalize(w.getName()));
            waypoint.put("info", "");

            double[] proj = SledgehammerUtil.toGeo(Double.parseDouble(w.getPoint().x), Double.parseDouble(w.getPoint().z));

            waypoint.put("lon", proj[0]);
            waypoint.put("lat", proj[1]);
            waypoints.add(waypoint);
        }

        JSONObject data = new JSONObject();
        data.put("waypoints", waypoints.toJSONString());
        data.put("auth", ConfigHandler.authenticationCode);

        packet.put("action", "warp_refresh");
        packet.put("data", data);

        ws.sendMessage(packet.toJSONString());
    }

    /**
     * Send a heartbeat to the map
     */
    public void heartbeat() {
        heartbeat = true;

        JSONObject packet = new JSONObject();
        JSONObject data = new JSONObject();

        data.put("auth", ConfigHandler.authenticationCode);
        packet.put("data", data);
        packet.put("action", "alive");
        ws.sendMessage(packet.toJSONString());
    }

    /**
     * Gets the current heartbeat of the map
     * @return True if alive, false if not
     */
    public boolean getHeartBeatState() {
        return heartbeat;
    }

    /**
     * Sets whether the map is successfully initialized
     * @param state True if initialized, false if not
     */
    public void setInitState(boolean state) {
        this.isMapInitialized = state;
    }

    /**
     * Gets whether the map is initialized
     * @return True if initialized, false if not
     */
    public boolean isMapInitialized() {
        return isMapInitialized;
    }
}
