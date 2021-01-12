/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpFragmentManager.java
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

package com.noahhusby.sledgehammer.commands.fragments.warps;

import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import net.md_5.bungee.api.CommandSender;

import java.util.ArrayList;
import java.util.List;

public abstract class WarpFragmentManager extends Command {

    public WarpFragmentManager(String name, String node) {
        this(name, node, new String[]{});
    }

    public WarpFragmentManager(String name, String node, String[] alias) {
        super(name, node, alias);
    }

    private final List<ICommandFragment> commandFragments = new ArrayList<>();
    protected void registerCommandFragment(ICommandFragment c) {
        commandFragments.add(c);
    }

    protected boolean executeFragment(CommandSender sender, String[] args) {
        if (args.length != 0) {
            ArrayList<String> dataList = new ArrayList<>();
            for (int x = 1; x < args.length; x++) dataList.add(args[x]);
            String[] data = dataList.toArray(new String[dataList.size()]);
            for (ICommandFragment f : commandFragments) {
                if (f.getName().equals(args[0].toLowerCase())) {
                    f.execute(sender, data);
                    return true;
                }
            }
        }

        return false;
    }
}
