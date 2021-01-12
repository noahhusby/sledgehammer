/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpRemoveFragment.java
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

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.permissions.PermissionRequest;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.Warp;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

import java.util.List;

public class WarpRemoveFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        PermissionHandler.getInstance().check(SledgehammerPlayer.getPlayer(sender), "sledgehammer.warp.remove", (code, global) -> {
            if(code == PermissionRequest.PermissionCode.PERMISSION) {
                run(sender, args, global);
                return;
            }
            sender.sendMessage(ChatConstants.noPermission);
        });
    }

    private void run(CommandSender sender, String[] args, boolean global) {
        if(args.length < 1) {
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Usage: /" + ConfigHandler.warpCommand +
                    " remove <name>", ChatColor.RED)));
            return;
        }

        List<Warp> rawWarps = WarpHandler.getInstance().getWarps(args[0]);
        List<Warp> warps = Lists.newArrayList();

        for(Warp w : rawWarps) {
            if(global) {
                warps.add(w);
                continue;
            }

            if(SledgehammerPlayer.getPlayer(sender).getSledgehammerServer().getGroup().getServers().contains(w.getServer()))
                warps.add(w);
        }

        if(warps.isEmpty()) {
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Warp not found!", ChatColor.RED)));
            return;
        }

        if(warps.size() == 1) {
            WarpHandler.getInstance().removeWarp(warps.get(0).getId(), sender);
            return;
        }

        boolean showWarps = (args.length == 1);

        if(!showWarps) {
            try {
                int val = Integer.parseInt(args[1]);
                if(val < 0 || val > warps.size()) {
                    showWarps = true;
                }
            } catch (Exception e) {
                showWarps = true;
            }
        }

        if(showWarps) {
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("There are multiple warps with that name!", ChatColor.BLUE)));
            for(int i = 0; i < warps.size(); i++)
                sender.sendMessage(ChatHelper.makeTextComponent(new TextElement(i + ": ", ChatColor.RED),
                        new TextElement(warps.get(i).getName(), ChatColor.BLUE), new TextElement(" - ", ChatColor.GRAY),
                        new TextElement(warps.get(i).getServer(), ChatColor.YELLOW)));
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Use '", ChatColor.GRAY),
                    new TextElement("/" + ConfigHandler.warpCommand + " remove " + args[0], ChatColor.YELLOW),
                    new TextElement(" <id>", ChatColor.RED), new TextElement("' to remove a specific warp!", ChatColor.GRAY)));
            return;
        }

        int val = Integer.parseInt(args[1]);
        WarpHandler.getInstance().removeWarp(warps.get(val).getId(), sender);
    }

    @Override
    public String getName() {
        return "remove";
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
