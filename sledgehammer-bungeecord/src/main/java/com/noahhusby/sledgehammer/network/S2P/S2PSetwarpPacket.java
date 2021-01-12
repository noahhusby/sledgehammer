/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PSetwarpPacket.java
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

package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;

public class S2PSetwarpPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.setwarpID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        SmartObject point = SmartObject.fromJSON((JSONObject) data.get("point"));
        DecimalFormat format = new DecimalFormat("###.###");

        String x = format.format(Double.parseDouble(point.getString("x")));
        String y = format.format(Double.parseDouble(point.getString("y")));
        String z = format.format(Double.parseDouble(point.getString("z")));
        String yaw = format.format(Double.parseDouble(point.getString("yaw")));
        String pitch = format.format(Double.parseDouble(point.getString("pitch")));

        WarpHandler.getInstance().incomingLocationResponse(info.getSender(), new Point(x, y, z, yaw, pitch));
    }
}
