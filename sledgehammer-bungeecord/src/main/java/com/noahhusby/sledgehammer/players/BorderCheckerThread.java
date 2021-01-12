/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - BorderCheckerThread.java
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

package com.noahhusby.sledgehammer.players;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.datasets.Point;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;

/**
 * Finds players and flags them if they are within a certain region of a border
 * Flagged players are then tracked in {@link FlaggedBorderCheckerThread}
 */
public class BorderCheckerThread implements Runnable {
    @Override
    public void run() {
        List<SledgehammerPlayer> players = PlayerManager.getInstance().getPlayers();

        for(SledgehammerPlayer p : players) {
            if(!p.onEarthServer()) {
                p.setTrackingPoint(null);
                p.setFlagged(false);
                continue;
            }

            if(!SledgehammerUtil.inEarthRegion(p)) {
                p.setTrackingPoint(null);
                p.setFlagged(false);
                continue;
            }

            if(p.checkAttribute("BORDER_MODE", false)) {
                p.setTrackingPoint(null);
                p.setFlagged(false);
                continue;
            }

            boolean checkLocation = false;

            if(p.getTrackingPoint() == null) {
                p.setTrackingPoint(p.getLocation());
                p.setFlagged(false);
                checkLocation = true;
            }

            if(!checkLocation) {
                Point location = p.getLocation();
                Point track = p.getTrackingPoint();

                double x = Double.parseDouble(location.x);
                double z = Double.parseDouble(location.z);

                double tx = Double.parseDouble(track.x);
                double tz = Double.parseDouble(track.z);

                boolean inZone = x < tx + Constants.borderZone && x > tx - Constants.borderZone && z < tz + Constants.borderZone && z > tz - Constants.borderZone;

                if(!inZone) {
                    p.setTrackingPoint(p.getLocation());
                    p.setFlagged(false);
                    checkLocation = true;
                }
            }

            if(checkLocation) {
                Point track = p.getTrackingPoint();

                double[] proj = SledgehammerUtil.toGeo(Double.parseDouble(track.x), Double.parseDouble(track.z));
                ServerInfo info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    p.setFlagged(true);
                    continue;
                }

                proj = SledgehammerUtil.toGeo(Double.parseDouble(track.x) + Constants.borderZone, Double.parseDouble(track.z) + Constants.borderZone);
                info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    p.setFlagged(true);
                    continue;
                }

                proj = SledgehammerUtil.toGeo(Double.parseDouble(track.x) + Constants.borderZone, Double.parseDouble(track.z) - Constants.borderZone);
                info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    p.setFlagged(true);
                    continue;
                }

                proj = SledgehammerUtil.toGeo(Double.parseDouble(track.x) - Constants.borderZone, Double.parseDouble(track.z) + Constants.borderZone);
                info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    p.setFlagged(true);
                    continue;
                }

                proj = SledgehammerUtil.toGeo(Double.parseDouble(track.x) - Constants.borderZone, Double.parseDouble(track.z) - Constants.borderZone);
                info = OpenStreetMaps.getInstance().getServerFromLocation(proj[0], proj[1], true);

                if(info != null && !info.getName().equalsIgnoreCase(p.getServer().getInfo().getName())) {
                    p.setFlagged(true);
                }
            }
        }
    }
}
