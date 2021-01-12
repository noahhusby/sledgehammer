/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SPermissionPacket.java
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
import com.noahhusby.sledgehammer.network.IP2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import org.json.simple.JSONObject;

public class P2SPermissionPacket implements IP2SPacket {
    private final String server;
    private final String salt;
    private final SledgehammerPlayer player;
    private final String permission;

    public P2SPermissionPacket(String server, SledgehammerPlayer player, String permission, String salt) {
        this.salt = salt;
        this.player = player;
        this.server = server;
        this.permission = permission;
    }

    @Override
    public String getPacketID() {
        return Constants.permissionCheckID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("salt", salt);
        data.put("player", player.getName());
        data.put("permission", permission);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player, server);
    }
}
