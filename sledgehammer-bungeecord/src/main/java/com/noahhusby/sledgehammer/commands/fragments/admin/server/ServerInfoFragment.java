/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerInfoFragment.java
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

import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.config.ServerInfo;

public class ServerInfoFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        SledgehammerServer server = ServerConfig.getInstance().getServer(args[0]);
        ServerInfo info = ProxyServer.getInstance().getServerInfo(args[0]);
        sender.sendMessage();
        sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Server Info - ", ChatColor.GRAY),
                new TextElement(info.getName(), ChatColor.BLUE)));
        sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Name: ", ChatColor.GRAY),
                new TextElement(info.getName(), ChatColor.BLUE)));
        if(server == null) {
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Status: ", ChatColor.GRAY),
                    new TextElement("Unconfigured", ChatColor.RED)));
        } else if (!server.isInitialized()) {
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Status: ", ChatColor.GRAY),
                    new TextElement("Configured", ChatColor.GREEN), new TextElement(" (Not Initialized)", ChatColor.RED)));
            sender.sendMessage();
            if(server.isEarthServer()) {
                sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Earth: ", ChatColor.GRAY),
                        new TextElement("Yes", ChatColor.GREEN)));
            } else {
                sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Earth: ", ChatColor.GRAY),
                        new TextElement("No", ChatColor.RED)));
            }
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Friendly Name: ", ChatColor.GRAY),
                    new TextElement(server.getFriendlyName(), ChatColor.BLUE)));
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("X Offset: ", ChatColor.GRAY),
                    new TextElement(String.valueOf(server.getxOffset()), ChatColor.BLUE)));
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Z Offset: ", ChatColor.GRAY),
                    new TextElement(String.valueOf(server.getzOffset()), ChatColor.BLUE)));
        } else {
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Status: ", ChatColor.GRAY),
                    new TextElement("Initialized", ChatColor.GREEN)));
            sender.sendMessage();
            if(server.isEarthServer()) {
                sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Earth: ", ChatColor.GRAY),
                        new TextElement("Yes", ChatColor.GREEN)));
            } else {
                sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Earth: ", ChatColor.GRAY),
                        new TextElement("No", ChatColor.RED)));
            }
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Friendly Name: ", ChatColor.GRAY),
                    new TextElement(server.getFriendlyName(), ChatColor.BLUE)));
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("X Offset: ", ChatColor.GRAY),
                    new TextElement(String.valueOf(server.getxOffset()), ChatColor.BLUE)));
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Z Offset: ", ChatColor.GRAY),
                    new TextElement(String.valueOf(server.getzOffset()), ChatColor.BLUE)));
            sender.sendMessage();
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("SH Version: ", ChatColor.GRAY),
                    new TextElement(server.getSledgehammerVersion(), ChatColor.BLUE)));
        }
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPurpose() {
        return "List info about server";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
