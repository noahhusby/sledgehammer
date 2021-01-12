/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - SledgehammerNetworkManager.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.network;

import com.google.common.io.ByteArrayDataInput;
import com.google.common.io.ByteStreams;
import com.noahhusby.sledgehammer.*;
import com.noahhusby.sledgehammer.network.P2S.*;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.plugin.messaging.PluginMessageListener;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SledgehammerNetworkManager implements PluginMessageListener, Listener {
    private static SledgehammerNetworkManager mInstance = null;

    public static SledgehammerNetworkManager getInstance() {
        if(mInstance == null) mInstance = new SledgehammerNetworkManager();
        return mInstance;
    }

    private final List<IP2SPacket> registeredProxyPackets;
    private final List<SmartObject> cachedProxyPackets;

    private SledgehammerNetworkManager() {
        registeredProxyPackets = new ArrayList<>();
        cachedProxyPackets = new ArrayList<>();

        registerProxyPacket(new P2SCommandPacket());
        registerProxyPacket(new P2SInitilizationPacket());
        registerProxyPacket(new P2SLocationPacket());
        registerProxyPacket(new P2SSetwarpPacket());
        registerProxyPacket(new P2STeleportPacket());
        registerProxyPacket(new P2STestLocationPacket());
        registerProxyPacket(new P2SWarpGUIPacket());
        registerProxyPacket(new P2SPermissionPacket());
        registerProxyPacket(new P2SWarpConfigPacket());
    }

    private void registerProxyPacket(IP2SPacket packet) {
        registeredProxyPackets.add(packet);
    }

    public void send(IS2PPacket packet) {
        JSONObject response = new JSONObject();
        response.put("command", packet.getPacketInfo().getID());
        response.put("sender", packet.getPacketInfo().getSender());
        response.put("server", packet.getPacketInfo().getServer());
        response.put("time", System.currentTimeMillis());
        response.put("data", packet.getMessage(new JSONObject()));

        sendMessage(Constants.responsePrefix + response.toJSONString(), packet.getPacketInfo().getSender());
    }

    private void onPacketRecieved(String m) {
        try {
            SmartObject packet = SmartObject.fromJSON((JSONObject) new JSONParser().parse(m));

            SmartObject packetData = SmartObject.fromJSON((JSONObject) packet.get("data"));
            PacketInfo packetInfo = new PacketInfo(packet.getString("command"), packet.getString("sender"),
                    packet.getString("server"), (long) packet.get("time"));

            if(!SledgehammerUtil.isPlayerAvailable(packetInfo.getSender())) {
                cachedProxyPackets.add(packet);
                return;
            }

            for(IP2SPacket p : registeredProxyPackets)  {
                if(p.getPacketID().equalsIgnoreCase(packetInfo.getID())) {
                    p.onMessage(packetInfo, packetData);
                }
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    private void sendMessage(String message, String sender) {
        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);
        try {
            out.writeUTF(message);
        } catch (IOException e) {
            e.printStackTrace();
        }

        Bukkit.getPlayer(sender).sendPluginMessage(Sledgehammer.sledgehammer, "sledgehammer:channel", stream.toByteArray());
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for(SmartObject p : cachedProxyPackets) {
            String sender = p.getString("sender");
            if(sender.equalsIgnoreCase(event.getPlayer().getName())) onPacketRecieved(p.toJSONString());
        }
    }


    @Override
    public void onPluginMessageReceived(String channel, Player player, byte[] message) {
        if (!channel.equalsIgnoreCase( "sledgehammer:channel")) {
            return;
        }
        ByteArrayDataInput in = ByteStreams.newDataInput(message);
        String m = in.readUTF();

        onPacketRecieved(m);
    }
}
