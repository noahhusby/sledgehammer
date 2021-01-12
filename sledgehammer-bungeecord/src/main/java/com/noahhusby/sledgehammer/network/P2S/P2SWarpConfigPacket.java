/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - P2SWarpConfigPacket.java
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
import com.noahhusby.sledgehammer.gui.GUIHandler;
import com.noahhusby.sledgehammer.network.IP2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import org.json.simple.JSONObject;

public class P2SWarpConfigPacket implements IP2SPacket {

    private final SledgehammerPlayer player;
    private final ServerConfigAction action;
    private final JSONObject data;
    private final boolean admin;

    public P2SWarpConfigPacket(SledgehammerPlayer player, ServerConfigAction action, boolean admin) {
        this(player, action, admin, new JSONObject());
    }

    public P2SWarpConfigPacket(SledgehammerPlayer player, ServerConfigAction action, boolean admin, JSONObject data) {
        this.player = player;
        this.action = action;
        this.data = data;
        this.admin = admin;
    }

    @Override
    public String getPacketID() {
        return Constants.warpConfigID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data = WarpHandler.getInstance().generateConfigPayload(player, admin);
        data.put("salt", GUIHandler.getInstance().track(player));
        data.put("action", action.name());
        data.put("data", this.data);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player, player.getServer().getInfo().getName());
    }

    public enum ServerConfigAction {
        OPEN_CONFIG, REMOVE_SUCCESSFUL, REMOVE_FAILURE, ADD_SUCCESSFUL, ADD_FAILURE, LOCATION_UPDATE
    }
}
