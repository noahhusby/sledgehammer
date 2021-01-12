/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerFragment.java
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

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.fragments.FragmentManager;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.commands.fragments.admin.groups.*;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import net.md_5.bungee.api.CommandSender;

public class GroupFragment extends FragmentManager implements ICommandFragment {

    public GroupFragment() {
        setCommandBase("sha group");
        setTitle("Sledgehammer Group Commands");
        registerCommandFragment(new GroupInfoFragment());
        registerCommandFragment(new GroupListFragment());
        registerCommandFragment(new GroupCreateFragment());
        registerCommandFragment(new GroupRemoveFragment());
        registerCommandFragment(new GroupSetHeadFragment());
        registerCommandFragment(new GroupSetNameFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!PermissionHandler.getInstance().isAdmin(sender)) {
            sender.sendMessage(ChatConstants.noPermission);
            return;
        }

        executeFragment(sender, args);
    }

    @Override
    public String getName() {
        return "group";
    }

    @Override
    public String getPurpose() {
        return "Configure group settings";
    }

    @Override
    public String[] getArguments() {
        return new String[]{};
    }
}
