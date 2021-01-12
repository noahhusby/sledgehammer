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

public class ServerGroupFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(ServerConfig.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatConstants.notSledgehammerServer);
            return;
        }

        if(args.length < 3) {
            sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Usage: /sha server <server name> setgroup [clear|<group id>]", ChatColor.RED)));
            return;
        }

        SledgehammerServer s = ServerConfig.getInstance().getServer(args[0]);
        if(args[2].equalsIgnoreCase("clear")) {
            if(s.getGroup().getID().equals(s.getName())) {
                sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("This server has no groups!", ChatColor.RED)));
                return;
            }

            for(ServerGroup sg : ServerConfig.getInstance().getGroups())
                sg.getServers().remove(s.getName());

            ServerConfig.getInstance().getGroups().save(true);
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Server has been removed from all groups!", ChatColor.GREEN)));
            return;
        }

        for(ServerGroup sg : ServerConfig.getInstance().getGroups()) {
            if(sg.getServers().contains(s.getName())) {
                sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("This server is assigned to another group!", ChatColor.RED)));
                sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Use ", ChatColor.GRAY),
                        new TextElement("/sha server <server name> setgroup clear ", ChatColor.BLUE),
                        new TextElement("to remove it from all groups!", ChatColor.GRAY)));
                return;
            }
        }

        String id = args[2];
        ServerGroup group = null;

        for(ServerGroup sg : ServerConfig.getInstance().getGroups())
            if(sg.getID().equals(id)) group = sg;

        if(group == null) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("That group doesn't exist!", ChatColor.RED)));
            return;
        }

        group.getServers().add(s.getName());
        ServerConfig.getInstance().getGroups().save(true);

        sender.sendMessage(ChatConstants.getValueMessage("group", group.getID(), s.getName()));
    }

    @Override
    public String getName() {
        return "setgroup";
    }

    @Override
    public String getPurpose() {
        return "Set group for the server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"[clear|<group id>]"};
    }
}
