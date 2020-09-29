/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerEarthModeFragment.java
 * All rights reserved.
 */

package com.noahhusby.sledgehammer.commands.fragments.admin.server;

import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.fragments.ICommandFragment;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.types.Server;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;

public class ServerEarthModeFragment implements ICommandFragment {

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(args.length < 3) {
            sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Usage: /sha server <server name> setearth <true/false>", ChatColor.RED)));
        } else {
            String arg = args[2].toLowerCase();
            if(arg.equals("true") || arg.equals("false")) {
                Server s = ServerConfig.getInstance().getServer(args[0]);

                s.earthServer = Boolean.parseBoolean(arg);
                ServerConfig.getInstance().pushServer(s);
                sender.sendMessage(ChatConstants.getValueMessage("earth", arg, s.name));
            } else {
                sender.sendMessage(ChatHelper.makeAdminTextComponent(new TextElement("Usage: /sha server <server name> setearth <true/false>", ChatColor.RED)));
            }
        }
    }

    @Override
    public String getName() {
        return "setearth";
    }

    @Override
    public String getPurpose() {
        return "Set whether the server is a build server";
    }

    @Override
    public String[] getArguments() {
        return new String[]{"<true/false>"};
    }
}
