/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - P2STeleportPacket.java
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

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.data.location.Point;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class P2STeleportPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.teleportID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        Player player = SledgehammerUtil.getPlayerFromName(info.getSender());
        if(player == null) {
            throwNoSender();
            return;
        }

        if(!player.isOnline()) {
            throwNoSender();
            return;
        }

        JSONObject pointData = (JSONObject) data.get("point");
        Point point = new Point((String) pointData.get("x"), (String) pointData.get("y"), (String) pointData.get("z"),
                (String) pointData.get("pitch"), (String) pointData.get("yaw"));

        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), String.format("minecraft:tp %s %s %s %s %s %s", info.getSender(), point.x,
                point.y, point.z, point.yaw, point.pitch));
    }
}
