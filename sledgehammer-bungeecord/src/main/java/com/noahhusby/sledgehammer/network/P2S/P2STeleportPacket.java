/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2STeleportPacket.java
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
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.json.simple.JSONObject;

public class P2STeleportPacket extends P2SPacket {
    private final String server;
    private final String sender;
    private final Point point;

    public P2STeleportPacket(String sender, String server, Point point) {
        this.server = server;
        this.sender = sender;
        this.point = point;
    }

    @Override
    public String getPacketID() {
        return Constants.teleportID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("point", point.getJSON());
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}