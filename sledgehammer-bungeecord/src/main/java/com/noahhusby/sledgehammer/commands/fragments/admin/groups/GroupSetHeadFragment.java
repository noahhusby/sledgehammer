/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GroupSetHeadFragment.java
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

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.ServerGroup;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class GroupSetHeadFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 2) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Usage: /sha group sethead <id> <head texture>", ChatColor.RED)));
            return;
        }

        String ID = args[0];
        String headTexture = args[1];
        ServerGroup group = null;
        for(ServerGroup sg : ServerConfig.getInstance().getGroups())
            if(sg.getID().equals(ID)) group = sg;

        if(group == null) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("That group doesn't exist!", ChatColor.RED)));
            return;
        }

        sender.sendMessage(ChatConstants.getValueMessage("head", headTexture, group.getID()));
        group.setHeadID(headTexture);
        ServerConfig.getInstance().getGroups().save(true);

    }

    @Override
    public String getName() {
        return "sethead";
    }

    @Override
    public String getPurpose() {
        return "Set head texture of a group";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<id>","<head texture>"};
    }
}
