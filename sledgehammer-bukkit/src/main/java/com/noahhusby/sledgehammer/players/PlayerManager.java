/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - PlayerManager.java
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

package com.noahhusby.sledgehammer.players;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.network.S2P.S2PPlayerUpdatePacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class PlayerManager {
    private static PlayerManager instance = null;

    public static PlayerManager getInstance() {
        return instance == null ? new PlayerManager() : instance;
    }

    private PlayerManager() {
        ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor();
        executor.scheduleAtFixedRate(new PlayerLocationUpdate(), 0, 100, TimeUnit.MILLISECONDS);
    }

    private class PlayerLocationUpdate implements Runnable {

        Map<UUID, Location> lastLocation = Maps.newHashMap();

        @Override
        public void run() {
            for(Player p : Bukkit.getOnlinePlayers()) {
                Location last = lastLocation.get(p.getUniqueId());
                if(last == null || !last.equals(p.getLocation()))
                    SledgehammerNetworkManager.getInstance().send(new S2PPlayerUpdatePacket(p));

                lastLocation.put(p.getUniqueId(), p.getLocation());
            }
        }
    }
}
