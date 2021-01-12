/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GroupListFragment.java
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
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;

public class GroupListFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        TextComponent list = ChatHelper.makeTitleTextComponent(new TextElement("Groups: ", ChatColor.RED));
        boolean first = true;
        for(ServerGroup s : ServerConfig.getInstance().getGroups()) {
            if(first) {
                TextComponent t = new TextComponent(s.getID());
                t.setColor(ChatColor.BLUE);
                t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/sha group info %s",
                        s.getID())));
                t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click for more info").create()));
                list.addExtra(t);
                first = false;
            } else {
                list.addExtra(ChatHelper.makeTextComponent(new TextElement(", ", ChatColor.GRAY)));
                TextComponent t = new TextComponent(s.getID());
                t.setColor(ChatColor.BLUE);
                t.setClickEvent(new ClickEvent(ClickEvent.Action.RUN_COMMAND, String.format("/sha group info %s",
                        s.getID())));
                t.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT,
                        new ComponentBuilder("Click for more info").create()));
                list.addExtra(t);
            }
        }
        sender.sendMessage(list);
    }

    @Override
    public String getName() {
        return "list";
    }

    @Override
    public String getPurpose() {
        return "List all groups";
    }

    @Override
    public String[] getArguments() {
        return new String[]{};
    }
}
