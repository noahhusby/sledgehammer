/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpSetFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.warps;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.permissions.PermissionRequest;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class WarpSetFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        SledgehammerPlayer player = SledgehammerPlayer.getPlayer(sender);
        PermissionHandler.getInstance().check(player, "sledgehammer.warp.set", (code, global) -> {
            if(code == PermissionRequest.PermissionCode.PERMISSION) {
                if(args.length == 0) {
                    sender.sendMessage(ChatHelper.makeTextComponent(
                            new TextElement(String.format("Usage: /%s set <name>", ConfigHandler.warpCommand), ChatColor.RED)));
                    return;
                }
                WarpHandler.WarpStatus warpStatus = WarpHandler.getInstance().getWarpStatus(args[0], player.getServer().getInfo().getName());

                switch (warpStatus) {
                    case EXISTS:
                        sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("A warp with that name already exists!", ChatColor.RED)));
                        sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Use the warp GUI to move it's location.", ChatColor.GRAY)));
                        break;
                    case RESERVED:
                        if(global) {
                            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("A warp with that name already exists!", ChatColor.RED)));
                            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Use the warp GUI to move it's location.", ChatColor.GRAY)));
                            return;
                        }
                        sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("This warp name is reserved and cannot be used.", ChatColor.RED)));
                        break;
                    case AVAILABLE:
                        WarpHandler.getInstance().requestNewWarp(args[0], sender);
                        break;
                }
                return;
            }
            sender.sendMessage(ChatConstants.noPermission);
        });
    }

    @Override
    public String getName() {
        return "set";
    }

    @Override
    public String getPurpose() {
        return "";
    }

    @Override
    public String[] getArguments() {
        return new String[]{};
    }
}
