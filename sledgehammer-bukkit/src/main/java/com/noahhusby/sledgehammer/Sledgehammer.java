/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - Sledgehammer.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer;

import com.noahhusby.sledgehammer.chat.ChatHandler;
import com.noahhusby.sledgehammer.eventhandler.ServerEventHandler;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.players.PlayerManager;
import org.bukkit.Bukkit;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class Sledgehammer extends JavaPlugin implements Listener {

    public static Logger logger;
    public static Plugin sledgehammer;
    public static String bungeecordName = "";

    @Override
    public void onEnable() {
        logger = getLogger();
        sledgehammer = this;

        Bukkit.getServer().getPluginManager().registerEvents(ChatHandler.getInstance(), this);
        Bukkit.getServer().getPluginManager().registerEvents(this, this);
        Bukkit.getServer().getPluginManager().registerEvents(new ServerEventHandler(), this);
        Bukkit.getServer().getPluginManager().registerEvents(SledgehammerNetworkManager.getInstance(), this);

        getServer().getMessenger().registerOutgoingPluginChannel(this, "sledgehammer:channel");
        getServer().getMessenger().registerIncomingPluginChannel( this, "sledgehammer:channel", SledgehammerNetworkManager.getInstance());

        PlayerManager.getInstance();
    }
}
