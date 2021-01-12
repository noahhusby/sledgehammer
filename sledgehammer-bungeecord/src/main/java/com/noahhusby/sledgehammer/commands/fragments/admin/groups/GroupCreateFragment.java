/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GroupCreateFragment.java
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

public class GroupCreateFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length == 0) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Usage: /sha group create <id> [name]", ChatColor.RED)));
            return;
        }

        String ID = args[0];

        for(ServerGroup sg : ServerConfig.getInstance().getGroups()) {
            if(sg.getID().equals(ID)) {
                sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("A group with that name already exists!", ChatColor.RED)));
                return;
            }
        }

        if(args.length == 1) {

            ServerGroup group = new ServerGroup();
            group.setID(ID);
            ServerConfig.getInstance().getGroups().add(group);
            ServerConfig.getInstance().getGroups().save(true);
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Succesfully created new group ", ChatColor.GRAY),
                    new TextElement(group.getID(), ChatColor.BLUE)));
            return;
        }

        StringBuilder name = new StringBuilder(args[1]);
        for(int i = 2; i < args.length; i++)
            name.append(" ").append(args[i]);

        ServerGroup group = new ServerGroup();
        group.setID(ID);
        group.setName(name.toString());
        sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Succesfully created new group ", ChatColor.GRAY),
                new TextElement(group.getID(), ChatColor.BLUE), new TextElement(" with name ", ChatColor.GRAY),
                new TextElement(name.toString(), ChatColor.RED)));
        ServerConfig.getInstance().getGroups().add(group);
        ServerConfig.getInstance().getGroups().save(true);
    }

    @Override
    public String getName() {
        return "create";
    }

    @Override
    public String getPurpose() {
        return "Create a new group";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<id>","[name]"};
    }
}
