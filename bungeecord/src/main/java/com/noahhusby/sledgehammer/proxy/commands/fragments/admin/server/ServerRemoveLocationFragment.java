/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerRemoveLocationFragment.java
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

package com.noahhusby.sledgehammer.proxy.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.proxy.ChatUtil;
import com.noahhusby.sledgehammer.proxy.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.proxy.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.proxy.dialogs.scenes.setup.LocationRemovalScene;
import com.noahhusby.sledgehammer.proxy.servers.ServerHandler;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

public class ServerRemoveLocationFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (ServerHandler.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatUtil.notSledgehammerServer);
            return;
        }

        if (!ServerHandler.getInstance().getServer(args[0]).isEarthServer()) {
            sender.sendMessage(ChatUtil.notEarthServer);
            return;
        }

        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatUtil.getNoPermission());
            return;
        }
        DialogHandler.getInstance().startDialog(sender, new LocationRemovalScene(ProxyServer.getInstance().getServerInfo(args[0]), null));
    }

    @Override
    public String getName() {
        return "removelocation";
    }

    @Override
    public String getPurpose() {
        return "Remove a location on the server";
    }

    @Override
    public String[] getArguments() {
        return null;
    }
}
