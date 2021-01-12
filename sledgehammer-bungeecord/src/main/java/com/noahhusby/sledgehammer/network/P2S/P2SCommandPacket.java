/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SCommandPacket.java
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
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.json.simple.JSONObject;

public class P2SCommandPacket extends P2SPacket {

    private final String server;
    private final String sender;
    private final String[] args;

    public P2SCommandPacket(String sender, String server, String... args) {
        this.server = server;
        this.sender = sender;
        this.args = args;
    }

    @Override
    public String getPacketID() {
        return Constants.commandID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        StringBuilder a = new StringBuilder(args[0]);

        if(args.length > 1)
            for(int x = 1; x < args.length; x++)
                a.append(" ").append(args[x]);

        data.put("args", a.toString());
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), sender, server);
    }
}
