/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SLocationPacket.java
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
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.json.simple.JSONObject;

public class P2SLocationPacket extends P2SPacket {

    private final String sender;
    private final String server;
    private final String lat;
    private final String lon;
    private String xOffset;
    private String zOffset;

    public P2SLocationPacket(String sender, String server, double[] geo) {
        this.server = server;
        this.sender = sender;
        SledgehammerServer sledgehammerServer = ServerConfig.getInstance().getServer(server);
        if(sledgehammerServer != null) {
            xOffset = String.valueOf(sledgehammerServer.getxOffset());
            zOffset = String.valueOf(sledgehammerServer.getzOffset());
        }

        this.lat = String.valueOf(geo[0]);
        this.lon = String.valueOf(geo[1]);
    }
    @Override
    public String getPacketID() {
        return Constants.locationID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("lat", lat);
        data.put("lon", lon);
        data.put("xOffset", xOffset);
        data.put("zOffset", zOffset);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
