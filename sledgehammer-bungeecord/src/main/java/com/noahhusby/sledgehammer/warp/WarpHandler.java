/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpHandler.java
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

package com.noahhusby.sledgehammer.warp;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.noahhusby.lib.data.storage.StorageList;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.ServerGroup;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.network.P2S.P2SSetwarpPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class WarpHandler {
    private static WarpHandler instance;

    public static WarpHandler getInstance() {
        if(instance == null) instance = new WarpHandler();
        return instance;
    }

    public StorageList<Warp> warps = new StorageList<>(Warp.class);
    public final Map<CommandSender, Warp> requestedWarps = Maps.newHashMap();

    /**
     * Gets a list of warps
     * @return List of warps
     */
    public StorageList<Warp> getWarps() {
        return warps;
    }

    /**
     * Gets a warp by name
     * @param name Name of warp
     * @return Warp if found, null if not
     */
    public List<Warp> getWarps(String name) {
        List<Warp> lw = Lists.newArrayList();
        for(Warp w : warps)
            if(w.getName().equalsIgnoreCase(name)) lw.add(w);

        return lw;
    }

    /**
     * Gets a warp by it's ID
     * @param id Warp ID
     * @return {@link Warp} if exists, null if not.
     */
    public Warp getWarp(int id) {
        for(Warp w : warps)
            if(w.getId() == id) return w;

        return null;
    }

    /**
     * Requests that a new warp should be created
     * @param warpName The name of the warp
     * @param sender The command sender
     */
    public void requestNewWarp(String warpName, CommandSender sender) {
        requestNewWarp(warpName, sender, null);
    }

    /**
     * Requests a new warp that should be created
     * @param warpName The name of the warp
     * @param sender The command sender
     * @param response The warp response
     */
    public void requestNewWarp(String warpName, CommandSender sender, WarpResponse response) {
        Warp warp = new Warp();
        warp.setName(warpName);
        warp.setResponse(response);
        requestedWarps.put(sender, warp);
        SledgehammerNetworkManager.getInstance().send(new P2SSetwarpPacket(sender.getName(), SledgehammerUtil.getServerFromSender(sender).getName()));

    }

    public void removeWarp(int warpID, CommandSender sender) {
        Warp warp = null;
        for(Warp w : warps)
            if(w.getId() == warpID) warp = w;

        if(warp == null) return;

        sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Successfully removed ", ChatColor.GRAY),
                new TextElement(warp.getName(), ChatColor.RED), new TextElement(" from ", ChatColor.GRAY),
                new TextElement(warp.getServer(), ChatColor.BLUE)));

        warps.remove(warp);
        warps.save(true);
    }

    /**
     * Saves a warp to the database
     * @param warp {@link Warp}
     */
    public void push(Warp warp) {
        warps.removeIf(w -> w.getId() == warp.getId());
        warps.add(warp);
        warps.save(true);
    }

    /**
     * Called by incoming location packet to register a warp
     * @param sender Name of sender
     * @param point Location of warp
     */
    public void incomingLocationResponse(String sender, Point point) {
        synchronized (requestedWarps) {
            Warp warp = null;
            SledgehammerPlayer player = null;
            for(Map.Entry<CommandSender, Warp> rw : requestedWarps.entrySet()) {
                if(rw.getKey().getName().equalsIgnoreCase(sender)) {
                    warp = rw.getValue();
                    player = SledgehammerPlayer.getPlayer(sender);
                }
            }

            if(warp == null) return;

            warp.setId(generateWarpID());
            warp.setPoint(point);
            warp.setServer(player.getServer().getInfo().getName());

            warps.add(warp);
            warps.save(true);

            if(warp.getResponse() != null) {
                warp.getResponse().onResponse(true, warp);
            } else {
                player.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Created warp ", ChatColor.GRAY),
                        new TextElement(warp.getName(), ChatColor.RED), new TextElement(" on ", ChatColor.GRAY), new TextElement(warp.getServer(), ChatColor.BLUE)));
            }

            List<CommandSender> removeSenders = Lists.newArrayList();
            for(Map.Entry<CommandSender, Warp> r : requestedWarps.entrySet())
                removeSenders.add(r.getKey());

            for(CommandSender s : removeSenders)
                requestedWarps.remove(s);
        }
    }

    public WarpStatus getWarpStatus(String warpName, String server) {
        for(Warp w : warps)
            if(w.getName().equalsIgnoreCase(warpName) && (!ConfigHandler.localWarp ||
                ServerConfig.getInstance().getServer(server).getGroup().getServers().contains(w.getServer())))
                return getWarpStatus(w.getId(), server);

        return WarpStatus.AVAILABLE;
    }

    public WarpStatus getWarpStatus(int warpId, String server) {
        boolean local = ConfigHandler.localWarp;
        for(Warp w : warps)
            if(w.getId() == warpId && !local && w.getPinnedMode() == Warp.PinnedMode.GLOBAL) return WarpStatus.RESERVED;

        for(Warp w: warps)
            if(w.getId() == warpId && (!local || ServerConfig.getInstance().getServer(server).getGroup().getServers().contains(w.getServer())))
                return WarpStatus.EXISTS;

        return WarpStatus.AVAILABLE;
    }

    /**

     * Gets list of warps as a String
     * @return List of warps as String
     */
    public TextComponent getWarpList(String server) {
        TextComponent list = ChatHelper.makeTitleTextComponent(new TextElement("Warps: ", ChatColor.RED));
        boolean first = true;
        for(Warp w : warps) {
            if(!(w.getServer().equalsIgnoreCase(server) || w.getPinnedMode() == Warp.PinnedMode.GLOBAL ||
                    !ConfigHandler.localWarp)) continue;
            if(first) {
                TextComponent t = new TextComponent(w.getName());
                t.setColor(ChatColor.BLUE);
                t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/%s %s",
                        ConfigHandler.warpCommand, w.getName())));
                t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to warp to " + w.getName()).create()));
                list.addExtra(t);
                first = false;
            } else {
                list.addExtra(ChatHelper.makeTextComponent(new TextElement(", ", ChatColor.GRAY)));
                TextComponent t = new TextComponent(w.getName());
                t.setColor(ChatColor.BLUE);
                t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/%s %s",
                        ConfigHandler.warpCommand, w.getName())));
                t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click to warp to " + w.getName()).create()));
                list.addExtra(t);
            }
        }

        return list;
    }

    /**
     * Generates the payload for the warp GUI
     * @param player {@link SledgehammerPlayer}
     * @param editAccess True if player has permission to edit warps, false if not
     * @return GUI Payload
     */
    public JSONObject generateGUIPayload(SledgehammerPlayer player, boolean editAccess) {
        SledgehammerServer s = player.getSledgehammerServer();
        if(s == null) return new JSONObject();

        JSONObject data = new JSONObject();
        data.put("local", ConfigHandler.localWarp);
        data.put("editAccess", editAccess);
        data.put("requestGroup", s.getGroup().getID());


        List<WarpGroup> groupsList = new ArrayList<>();

        for(Warp w : warps) {
            SledgehammerServer server = ServerConfig.getInstance().getServer(w.getServer());
            if(server == null) continue;

            WarpGroup wg = null;
            for(WarpGroup g : groupsList)
                if(g.ID.equals(server.getGroup().getID())) wg = g;

            if(wg == null) {
                ServerGroup sg = server.getGroup();
                wg = new WarpGroup(sg.getID(), sg.getName(), sg.getHeadID());
                wg.warps.add(w);
                groupsList.add(wg);
            } else {
                wg.warps.add(w);
            }
        }

        String defaultPage = ConfigHandler.warpMenuPage;

        if(ConfigHandler.showPinnedOnBlank) {
            boolean found = false;
            for(WarpGroup wg: groupsList)
                if(s.getGroup().getID().equals(wg.ID)) found = true;

            if(!found)
                defaultPage = "pinned";
        }

        if(player.checkAttribute("WARP_SORT", "WARP_SORT_ALL")) {
            defaultPage = "all";
        } else if(player.checkAttribute("WARP_SORT", "WARP_SORT_GROUP")) {
            defaultPage = "group";
        } else if(player.checkAttribute("WARP_SORT", "WARP_SORT_PINNED")) {
            defaultPage = "pinned";
        }

        data.put("defaultPage", defaultPage);

        JSONArray groups = new JSONArray();
        for(WarpGroup wg : groupsList)
            groups.add(wg.toJson());

        data.put("groups", groups);
        return data;
    }

    /**
     * Generates paylood for the warp configuration GUI
     * @param player {@link SledgehammerPlayer}
     * @param admin True if they are able to edit all groups
     * @return Config Payload
     */
    public JSONObject generateConfigPayload(SledgehammerPlayer player, boolean admin) {
        SledgehammerServer s = player.getSledgehammerServer();
        if(s == null) return new JSONObject();

        JSONObject data = new JSONObject();
        data.put("requestGroup", s.getGroup().getID());
        data.put("admin", admin);
        data.put("local", ConfigHandler.localWarp);

        List<WarpGroup> groupsList = new ArrayList<>();

        for(Warp w : warps) {
            SledgehammerServer server = ServerConfig.getInstance().getServer(w.getServer());
            if(server == null) continue;
            if(!server.getGroup().getID().equals(s.getGroup().getID()) && !admin) continue;

            WarpGroup wg = null;
            for(WarpGroup g : groupsList)
                if(g.ID.equals(server.getGroup().getID())) wg = g;

            if(wg == null) {
                ServerGroup sg = server.getGroup();
                wg = new WarpGroup(sg.getID(), sg.getName(), sg.getHeadID());
                wg.warps.add(w);
                groupsList.add(wg);
            } else {
                wg.warps.add(w);
            }
        }

        JSONArray groups = new JSONArray();
        for(WarpGroup wg : groupsList)
            groups.add(wg.toJson());

        data.put("groups", groups);
        return data;
    }

    /**
     * Generates an ID for warps
     * @return New ID
     */
    private int generateWarpID() {
        if(ConfigHandler.proxyTotal != -1) return generateMultiWarpID();
        int x = -1;
        for(Warp w : warps)
            if(w.getId() > x) x = w.getId();

        return x < 0 ? 0 : x + 1;
    }

    /**
     * Generates a warp ID for multi-server networks
     * @return New ID
     */
    private int generateMultiWarpID() {
        int i = ConfigHandler.proxyId;
        int min = i * Constants.warpIdBuffer;
        int max = (min + Constants.warpIdBuffer) - 1;
        while (true) {
            for(int x = min; x < max; x++) {
                if(getWarp(x) == null) return x;
            }

            i += (ConfigHandler.proxyTotal + 1);
            min = i * Constants.warpIdBuffer;
            max = (min + Constants.warpIdBuffer) - 1;
        }
    }

    private class WarpGroup {
        public String ID;
        public String name;
        public String headID;
        public List<Warp> warps;

        public WarpGroup(String ID, String name, String headID) {
            this.ID = ID;
            this.name = name;
            this.headID = headID;
            this.warps = new ArrayList<>();
        }

        public JSONObject toJson() {
            JSONObject wg = new JSONObject();
            wg.put("id", ID);
            wg.put("name", name);
            wg.put("headId", headID);

            JSONArray wa = new JSONArray();
            for(Warp w : warps)
                wa.add(w.toWaypoint());

            wg.put("warps", wa);
            return wg;
        }
    }

    public enum WarpStatus {
        EXISTS, RESERVED, AVAILABLE
    }
}
