/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerSetFriendlyFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.ServerGroup;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ServerOffsetFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(ServerConfig.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatConstants.notSledgehammerServer);
            return;
        }

        if(args.length < 4) {
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Usage: /sha server <server name> setoffest <x|z> <offset>", ChatColor.RED)));
            return;
        }

        SledgehammerServer s = ServerConfig.getInstance().getServer(args[0]);
        String axis = args[2];
        if(!(axis.equalsIgnoreCase("x") || axis.equalsIgnoreCase("z"))) {
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Usage: /sha server <server name> setoffest <x|z> <offset>", ChatColor.RED)));
            return;
        }

        String valString = args[3];
        int value;
        try {
            value = Integer.parseInt(valString);
        } catch (NumberFormatException e) {
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Usage: /sha server <server name> setoffest <x|z> <offset>", ChatColor.RED)));
            return;
        }

        if(axis.equalsIgnoreCase("z")) {
            s.setzOffset(value);
            sender.sendMessage(ChatConstants.getValueMessage("zOffset", valString, s.getName()));
        } else {
            s.setxOffset(value);
            sender.sendMessage(ChatConstants.getValueMessage("xOffset", valString, s.getName()));
        }

        ServerConfig.getInstance().getServers().save(true);
    }

    @Override
    public String getName() {
        return "setoffset";
    }

    @Override
    public String getPurpose() {
        return "Set the tpll offset";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<x|z> <offset>"};
    }
}
