/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerAdminCommand.java
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

package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.fragments.CommandFragmentManager;
import com.noahhusby.sledgehammer.commands.fragments.admin.*;
import net.md_5.bungee.api.CommandSender;

public class SledgehammerAdminCommand extends CommandFragmentManager {
    public SledgehammerAdminCommand() {
        super("sha", "sledgehammer.admin");

        setCommandBase("sha");
        setTitle("Sledgehammer Admin Commands");

        registerCommandFragment(new ReloadFragment());
        registerCommandFragment(new SetupFragment());
        registerCommandFragment(new ServerFragment());
        registerCommandFragment(new GroupFragment());
        registerCommandFragment(new PermissionCheckFragment());
        registerCommandFragment(new TestLocationFragment());
        registerCommandFragment(new MigrateFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!hasPerms(sender)) {
            sender.sendMessage(ChatConstants.noPermission);
            return;
        }
        executeFragment(sender, args);
    }
}
