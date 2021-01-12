/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - P2SLocationPacket.java
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
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.projection.GeographicProjection;
import com.noahhusby.sledgehammer.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.projection.ScaleProjection;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;

public class P2SLocationPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.locationID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        Player player = SledgehammerUtil.getPlayerFromName(info.getSender());
        if(player == null) {
            throwNoSender();
            return;
        }

        String lat = data.getString("lat");
        String lon = data.getString("lon");

        int xOffset = 0;
        int zOffset = 0;

        try {
            xOffset = Integer.parseInt(data.getString("xOffset"));
            zOffset = Integer.parseInt(data.getString("zOffset"));
        } catch (Exception ignored) { }

        GeographicProjection projection = new ModifiedAirocean();
        GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
        ScaleProjection scaleProj = new ScaleProjection(uprightProj, Constants.SCALE, Constants.SCALE);

        double proj[] = scaleProj.fromGeo(Double.parseDouble(lon), Double.parseDouble(lat));

        int x = (int) Math.floor(proj[0]) + xOffset;
        int z = (int) Math.floor(proj[1]) + zOffset;

        int y = Constants.scanHeight;

        while(player.getWorld().getBlockAt(x, y, z).getType() != Material.AIR) {
            y += Constants.scanHeight;
        }

        while(player.getWorld().getBlockAt(x, y, z).getType() == Material.AIR) {
            y -= 1;
        }

        Bukkit.getServer().dispatchCommand(Bukkit.getConsoleSender(),String.format("minecraft:tp %s %s %s %s", player.getName(), x, y+1, z));

    }
}
