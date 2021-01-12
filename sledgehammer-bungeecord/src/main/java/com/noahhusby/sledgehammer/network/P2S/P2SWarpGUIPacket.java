/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SWarpGUIPacket.java
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

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.gui.GUIHandler;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.json.simple.JSONObject;

public class P2SWarpGUIPacket extends P2SPacket {

    private final String server;
    private final String sender;
    private final boolean editAccess;
    private String group = null;

    public P2SWarpGUIPacket(String sender, String server, boolean editAccess) {
        this.server = server;
        this.sender = sender;
        this.editAccess = editAccess;
    }

    public P2SWarpGUIPacket(String sender, String server, boolean editAccess, String group) {
        this(sender, server, editAccess);
        this.group = group;
    }

    @Override
    public String getPacketID() {
        return Constants.warpGUIID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        JSONObject payload = WarpHandler.getInstance().generateGUIPayload(SledgehammerPlayer.getPlayer(sender), editAccess);
        if(group != null) {
            payload.remove("requestGroup");
            payload.put("requestGroup", group);
            payload.put("defaultPage", "group");
        }
        payload.put("salt", GUIHandler.getInstance().track(SledgehammerPlayer.getPlayer(sender)));
        return payload;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
