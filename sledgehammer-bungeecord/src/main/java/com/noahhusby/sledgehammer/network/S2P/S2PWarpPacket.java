/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - S2PWarpPacket.java
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
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.gui.GUIHandler;
import com.noahhusby.sledgehammer.network.P2S.P2STeleportPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.Warp;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;

public class S2PWarpPacket extends S2PPacket {
    @Override
    public String getPacketID() {
        return Constants.warpID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {

        if(!GUIHandler.getInstance().validateRequest(
                SledgehammerPlayer.getPlayer(info.getSender()), data.getString("salt"))) return;

        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(info.getSender());
        if(player == null) return;
        int warpId = SledgehammerUtil.JsonUtils.toInt(data.get("warpId"));

        Warp warp = null;
        for(Warp w : WarpHandler.getInstance().getWarps())
            if(w.getId() == warpId) warp = w;

        if(warp == null) {
            player.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("That warp does not exist!", ChatColor.RED)));
            return;
        }

        if(SledgehammerUtil.getServerFromSender(player) != SledgehammerUtil.getServerByName(warp.getServer())) {
            player.connect(SledgehammerUtil.getServerByName(warp.getServer()));
            player.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Sending you to ", ChatColor.GRAY), new TextElement(warp.getServer(), ChatColor.RED)));
        }

        player.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Warping to ", ChatColor.GRAY), new TextElement(warp.getName(), ChatColor.RED)));
        SledgehammerNetworkManager.getInstance().send(new P2STeleportPacket(player.getName(), warp.getServer(), warp.getPoint()));
    }
}
