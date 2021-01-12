/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerNetworkManager.java
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

package com.noahhusby.sledgehammer.network;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.network.S2P.*;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class SledgehammerNetworkManager implements Listener {
    private static SledgehammerNetworkManager mInstance = null;

    public static SledgehammerNetworkManager getInstance() {
        if(mInstance == null) mInstance = new SledgehammerNetworkManager();
        return mInstance;
    }

    private final List<IS2PPacket> registeredPackets;

    private SledgehammerNetworkManager() {
        Sledgehammer.addListener(this);
        registeredPackets = new ArrayList<>();

        register(new S2PInitializationPacket());
        register(new S2PSetwarpPacket());
        register(new S2PTestLocationPacket());
        register(new S2PWarpPacket());
        register(new S2PWebMapPacket());
        register(new S2PPlayerUpdatePacket());
        register(new S2PPermissionPacket());
        register(new S2PWarpConfigPacket());
    }

    /**
     * Register incoming packets
     * @param packet {@link IS2PPacket}
     */
    private void register(IS2PPacket packet) {
        registeredPackets.add(packet);
    }

    /**
     * Send an outgoing packet
     * @param packet {@link IP2SPacket}
     */
    public void send(IP2SPacket packet) {
        JSONObject response = new JSONObject();
        response.put("command", packet.getPacketInfo().getID());
        response.put("sender", packet.getPacketInfo().getSender());
        response.put("server", packet.getPacketInfo().getServer());
        response.put("time", System.currentTimeMillis());
        response.put("data", packet.getMessage(new JSONObject()));

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        DataOutputStream out = new DataOutputStream(stream);

        try {
            out.writeUTF(response.toJSONString());
        } catch (IOException e) {
            e.printStackTrace();
        }

        ProxyServer.getInstance().getServerInfo(packet.getPacketInfo().getServer()).sendData("sledgehammer:channel", stream.toByteArray(), true);
    }

    /**
     * Checks for sledgehammer packets from incoming plugin messages
     * @param e {@link PluginMessageEvent}
     */
    @EventHandler(priority = EventPriority.LOWEST)
    public void onIncomingPacket(PluginMessageEvent e) {
        if (!e.getTag().equalsIgnoreCase("sledgehammer:channel")) return;
        DataInputStream in = new DataInputStream(new ByteArrayInputStream(e.getData()));

        try {
            SmartObject packet = SmartObject.fromJSON((JSONObject) new JSONParser().parse(getRawMessage(in)));

            SmartObject packetData = SmartObject.fromJSON((JSONObject) packet.get("data"));
            PacketInfo packetInfo = new PacketInfo(packet.getString("command"), packet.getString("sender"),
                    packet.getString("server"), (long) packet.get("time"));

            for(IS2PPacket p : registeredPackets) {
                if(p.getPacketID().equalsIgnoreCase(packetInfo.getID())) {
                    p.onMessage(packetInfo, packetData);
                }
            }
        } catch (ParseException ex) {
            ex.printStackTrace();
        } catch (Exception ignored) { }
    }

    /**
     * Converts {@link DataInputStream} to raw text
     * @param i  {@link DataInputStream}
     * @return Raw Message
     */
    private String getRawMessage(DataInputStream i) {
        try {
            StringBuilder x = new StringBuilder();
            while(i.available() != 0) {
                char c = (char) i.readByte();
                x.append(c);
            }
            return x.substring(x.indexOf("<")+1).trim();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
