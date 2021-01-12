/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PWarpConfigPacket.java
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
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.datasets.Point;
import com.noahhusby.sledgehammer.gui.GUIHandler;
import com.noahhusby.sledgehammer.network.IS2PPacket;
import com.noahhusby.sledgehammer.network.P2S.P2SWarpConfigPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.permissions.PermissionRequest;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.Warp;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import org.json.simple.JSONObject;

import java.text.DecimalFormat;

public class S2PWarpConfigPacket implements IS2PPacket {
    @Override
    public String getPacketID() {
        return Constants.warpConfigID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject packet) {
        if(!GUIHandler.getInstance().validateRequest(SledgehammerPlayer.getPlayer(info.getSender()), packet.getString("salt"))) return;

        SmartObject data = SmartObject.fromJSON((JSONObject) packet.get("data"));
        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(info.getSender());
        JSONObject response = new JSONObject();
        boolean g = PermissionHandler.getInstance().isAdmin(player) || player.hasPermission("sledgehammer.warp.edit");

        switch (ProxyConfigAction.valueOf(packet.getString("action"))) {
            case OPEN_CONFIG:
                PermissionHandler.getInstance().check(player, "sledgehammer.warp.edit", (code, global) -> {
                    if(code == PermissionRequest.PermissionCode.PERMISSION) {
                        SledgehammerNetworkManager.getInstance().send(new P2SWarpConfigPacket(player,
                                P2SWarpConfigPacket.ServerConfigAction.OPEN_CONFIG, global));
                        return;
                    }
                    player.sendMessage(ChatConstants.noPermission);
                });

                break;
            case CREATE_WARP:
                String warpName = data.getString("warpName");
                WarpHandler.WarpStatus warpStatus = WarpHandler.getInstance().getWarpStatus(warpName, info.getServer());

                switch (warpStatus) {
                    case EXISTS:
                    case RESERVED:
                        response.put("warpName", warpName);
                        SledgehammerNetworkManager.getInstance().send(new P2SWarpConfigPacket(
                                player, P2SWarpConfigPacket.ServerConfigAction.ADD_FAILURE, g, response));
                        break;
                    case AVAILABLE:
                        WarpHandler.getInstance().requestNewWarp(warpName, player, (success, warp) -> {
                            response.put("warpName", warp.getName());
                            response.put("warpId", warp.getId());
                            SledgehammerNetworkManager.getInstance().send(new P2SWarpConfigPacket(
                                    player, P2SWarpConfigPacket.ServerConfigAction.ADD_SUCCESSFUL, g, response));
                        });
                        break;
                }
                break;
            case UPDATE_WARP:
                int warpId = SledgehammerUtil.JsonUtils.toInt(data.get("id"));
                String name = data.getString("name");
                String headId = data.getString("headId");
                Warp.PinnedMode pin = Warp.PinnedMode.valueOf(data.getString("pinned"));

                Warp warp = WarpHandler.getInstance().getWarp(warpId);
                if(warp == null) return;
                warp.setHeadID(headId);
                warp.setName(name);
                warp.setPinnedMode(pin);

                WarpHandler.getInstance().getWarps().save(true);
                break;
            case UPDATE_PLAYER_DEFAULT:
                String sort = data.getString("sort");
                if(sort.equalsIgnoreCase("all")) {
                    player.getAttributes().put("WARP_SORT", "WARP_SORT_ALL");
                } else if(sort.equalsIgnoreCase("group")) {
                    player.getAttributes().put("WARP_SORT", "WARP_SORT_GROUP");
                } else if(sort.equalsIgnoreCase("pinned")) {
                    player.getAttributes().put("WARP_SORT", "WARP_SORT_PINNED");
                }
                break;
            case WARP_UPDATE_LOCATION:
                SmartObject point = SmartObject.fromJSON((JSONObject) data.get("point"));
                DecimalFormat format = new DecimalFormat("###.###");

                String x = format.format(Double.parseDouble(point.getString("x")));
                String y = format.format(Double.parseDouble(point.getString("y")));
                String z = format.format(Double.parseDouble(point.getString("z")));
                String yaw = format.format(Double.parseDouble(point.getString("yaw")));
                String pitch = format.format(Double.parseDouble(point.getString("pitch")));

                Warp w = WarpHandler.getInstance().getWarp(SledgehammerUtil.JsonUtils.toInt(data.get("warpId")));
                w.setPoint(new Point(x, y, z, yaw, pitch));
                w.setServer(info.getServer());
                WarpHandler.getInstance().getWarps().save(true);

                response.put("warpId", w.getId());
                SledgehammerNetworkManager.getInstance().send(new P2SWarpConfigPacket(player,
                        P2SWarpConfigPacket.ServerConfigAction.LOCATION_UPDATE,
                        g, response));
                break;
            case REMOVE_WARP:
                WarpHandler.getInstance().getWarps().remove(WarpHandler.getInstance().getWarp(
                        SledgehammerUtil.JsonUtils.toInt(data.get("warpId"))));
                WarpHandler.getInstance().getWarps().save(true);
                SledgehammerNetworkManager.getInstance().send(new P2SWarpConfigPacket(player,
                        P2SWarpConfigPacket.ServerConfigAction.REMOVE_SUCCESSFUL, g));
                break;
        }
    }

    public enum ProxyConfigAction {
        OPEN_CONFIG, CREATE_WARP, UPDATE_WARP, UPDATE_PLAYER_DEFAULT, WARP_UPDATE_LOCATION, REMOVE_WARP
    }
}
