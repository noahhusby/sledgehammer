/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerAddLocationFragment.java
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

package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.lib.data.JsonUtils;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.dialogs.scenes.location.*;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.io.IOException;

public class ServerAddLocationFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatConstants.issueByPlayer);
            return;
        }

        if(ServerConfig.getInstance().getServer(args[0]) == null) {
            sender.sendMessage(ChatConstants.notSledgehammerServer);
            return;
        }

        if(!ServerConfig.getInstance().getServer(args[0]).isEarthServer()) {
            sender.sendMessage(ChatConstants.notEarthServer);
            return;
        }

        if(args.length > 2) {
            String arg = SledgehammerUtil.getRawArguments(SledgehammerUtil.selectArray(args, 2));
            if(arg.equalsIgnoreCase("city")) {
                DialogHandler.getInstance().startDialog(sender, new CityScene(ProxyServer.getInstance().getServerInfo(args[0])));
                return;
            } else if(arg.equalsIgnoreCase("county")) {
                DialogHandler.getInstance().startDialog(sender, new CountyScene(ProxyServer.getInstance().getServerInfo(args[0])));
                return;
            } else if(arg.equalsIgnoreCase("state")) {
                DialogHandler.getInstance().startDialog(sender, new StateScene(ProxyServer.getInstance().getServerInfo(args[0])));
                return;
            } else if(arg.equalsIgnoreCase("country")) {
                DialogHandler.getInstance().startDialog(sender, new CountryScene(ProxyServer.getInstance().getServerInfo(args[0])));
                return;
            }

            boolean validJson = false;
            try {
                validJson = JsonUtils.isJsonValid(arg);
            } catch (IOException ignored) { }

            if(validJson) {
                Location l = SledgehammerUtil.JsonUtils.gson.fromJson(arg, Location.class);
                if(l == null) {
                    sender.sendMessage(ChatColor.RED + "Unable to parse json location! Please try again.");
                    return;
                }

                SledgehammerServer s = ServerConfig.getInstance().getServer(args[0]);
                s.getLocations().add(l);
                ServerConfig.getInstance().pushServer(s);

                String x = "";
                if(!l.city.equals("")) x+= ChatHelper.capitalize(l.city)+", ";
                if(!l.county.equals("")) x+= ChatHelper.capitalize(l.county)+", ";
                if(!l.state.equals("")) x+= ChatHelper.capitalize(l.state)+", ";
                if(!l.country.equals("")) x+= ChatHelper.capitalize(l.country);
                sender.sendMessage(ChatHelper.makeAdminTextComponent(
                        new TextElement("Successfully added ", ChatColor.GRAY),
                        new TextElement(l.detailType + ": ", ChatColor.BLUE),
                        new TextElement(x, ChatColor.RED)));
                return;
            } else if(arg.contains("{")) {
                sender.sendMessage(ChatColor.RED + "Unable to parse json location! Please try again.");
                return;
            }

        }
        DialogHandler.getInstance().startDialog(sender, new LocationSelectionScene(ProxyServer.getInstance().getServerInfo(args[0])));
    }

    @Override
    public String getName() {
        return "addlocation";
    }

    @Override
    public String getPurpose() {
        return "Add a location to the server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"[city|county|state|county|{json}]"};
    }
}
