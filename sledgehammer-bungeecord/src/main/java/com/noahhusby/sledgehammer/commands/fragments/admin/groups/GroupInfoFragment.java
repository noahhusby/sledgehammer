/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GroupInfoFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.admin.groups;

import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.ServerGroup;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class GroupInfoFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 1) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Usage: /sha group info <id>", ChatColor.RED)));
            return;
        }

        String gn = args[0];
        ServerGroup group = null;

        for(ServerGroup g : ServerConfig.getInstance().getGroups())
            if(g.getID().equalsIgnoreCase(gn)) group = g;

        if(group == null) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Group doesn't exist!", ChatColor.RED)));
            return;
        }

        sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Group Information:", ChatColor.GRAY)));
        sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("ID: ", ChatColor.GRAY), new TextElement(group.getID(), ChatColor.BLUE)));
        sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Name: ", ChatColor.GRAY), new TextElement(group.getName(), ChatColor.BLUE)));
        sender.sendMessage(ChatHelper.makeTextComponent(new TextElement("Servers: ", ChatColor.GRAY),
                new TextElement(String.join(", ", group.getServers()), ChatColor.BLUE)));
    }

    @Override
    public String getName() {
        return "info";
    }

    @Override
    public String getPurpose() {
        return "See info about a group";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<id>"};
    }
}
