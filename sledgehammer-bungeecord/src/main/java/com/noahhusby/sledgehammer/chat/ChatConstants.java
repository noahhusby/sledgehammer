/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ChatConstants.java
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
package com.noahhusby.sledgehammer.chat;

import com.noahhusby.sledgehammer.config.ConfigHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.chat.TextComponent;

public class ChatConstants {
    public static final String logInitPacket = "Received initialization packet from ";

    public static final TextComponent noPermission = ChatHelper.makeTextComponent(new TextElement("You don't have permission to run this command!", ChatColor.DARK_RED));
    public static final TextComponent issueByPlayer = ChatHelper.makeTextComponent(new TextElement("This command can only be executed by a player!", ChatColor.DARK_RED));
    public static final TextComponent notSledgehammerServer = ChatHelper.makeAdminTextComponent(new TextElement("This server is not configured as a sledgehammer server! Please use", ChatColor.GRAY),
            new TextElement(" /sha server <server name> setsledgehammer true ", ChatColor.BLUE), new TextElement("to enable it.", ChatColor.GRAY));
    public static final TextComponent notEarthServer = ChatHelper.makeAdminTextComponent(new TextElement("This server is not configured as an earth server! Please use", ChatColor.GRAY),
            new TextElement(" /sha server <server name> setearth true ", ChatColor.BLUE), new TextElement("to enable it.", ChatColor.GRAY));

    public static TextComponent getValueMessage(String key, String value, String where) {
        return ChatHelper.makeAdminTextComponent(new TextElement("Set value", ChatColor.GRAY),
                new TextElement(" " + key + " ", ChatColor.GOLD), new TextElement("to ", ChatColor.GRAY), new TextElement(value, ChatColor.RED),
                new TextElement(" on ", ChatColor.GRAY), new TextElement(where, ChatColor.GOLD));
    }

    public static TextComponent getNotAvailable() {
        return ChatHelper.makeTextComponent(ConfigHandler.replaceNotAvailable ?
                new TextElement("Unknown command. Type \"/help\" for help.", ChatColor.WHITE) :
                new TextElement("That command is not available.", ChatColor.RED));
    }
}
