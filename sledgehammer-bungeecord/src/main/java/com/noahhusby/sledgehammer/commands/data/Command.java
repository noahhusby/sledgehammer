/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Command.java
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

package com.noahhusby.sledgehammer.commands.data;

import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import net.md_5.bungee.api.CommandSender;

public abstract class Command extends net.md_5.bungee.api.plugin.Command {
    private final String permissionNode;
    public Command(String name, String node) {
        super(name);
        this.permissionNode = node;
    }

    public Command(String name, String node, String[] alias) {
        super(name, "", alias);
        this.permissionNode = node;
    }

    protected boolean isAllowed(CommandSender sender) {
        if(!sender.hasPermission("sledgehammer.requiresh")) return true;
        return SledgehammerPlayer.getPlayer(sender).onSledgehammer();
    }

    protected boolean hasPerms(CommandSender sender) {
        return hasPerms(sender, false);
    }

    protected boolean hasPerms(CommandSender sender, boolean exact) {
        if(PermissionHandler.getInstance().isAdmin(sender)) return true;
        if(permissionNode == null) return false;

        if(sender.hasPermission(permissionNode+".admin")) return true;
        for(String s : sender.getPermissions())
            if((exact && s.equalsIgnoreCase(permissionNode)) || (!exact && s.contains(permissionNode)))
                return true;

        return false;
    }

    protected boolean hasPerms(CommandSender sender, String specificNode) {
        if(PermissionHandler.getInstance().isAdmin(sender)) return true;
        if(permissionNode == null) return false;

        for(String s : sender.getPermissions()) {
            if(s == null) continue;
            if(s.equals(permissionNode+"."+specificNode)) return true;
        }

        return false;
    }

    public boolean isAdmin(CommandSender sender) {
        return PermissionHandler.getInstance().isAdmin(sender);
    }


    protected SledgehammerNetworkManager getNetworkManager() {
        return SledgehammerNetworkManager.getInstance();
    }
}

