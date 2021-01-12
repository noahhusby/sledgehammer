/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ReloadFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.admin;


import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;

import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ReloadFragment implements ICommandFragment {
    @Override
    public void execute(CommandSender sender, String[] args) {
        ConfigHandler.getInstance().reload();
        sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Reloaded sledgehammer", ChatColor.BLUE)));
    }

    @Override
    public String getName() {
        return "reload";
    }

    @Override
    public String getPurpose() {
        return "Reloads the config";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
