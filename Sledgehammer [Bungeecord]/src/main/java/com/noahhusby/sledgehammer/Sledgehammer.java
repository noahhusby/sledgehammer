/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Sledgehammer.java
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

import com.noahhusby.sledgehammer.addons.AddonManager;
import com.noahhusby.sledgehammer.addons.TerramapAddon;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.commands.*;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.players.PlayerManager;
import com.noahhusby.sledgehammer.maps.MapHandler;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.*;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;

import java.util.logging.Logger;

public class Sledgehammer extends Plugin implements Listener {
    public static Logger logger;
    public static Plugin sledgehammer;

    public static AddonManager addonManager;

    @Override
    public void onEnable() {
        this.sledgehammer = this;
        logger = getLogger();

        ConfigHandler.getInstance().init(getDataFolder());

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerAdminCommand());
        ProxyServer.getInstance().getPluginManager().registerListener(this, this);

        if(!ConfigHandler.getInstance().isAuthCodeConfigured()) {
            logger.severe("------------------------------");
            for(int x = 0; x < 2; x++) {
                logger.severe("");
            }
            logger.severe("The authentication code is not configured, or configured incorrectly.");
            logger.severe("Please generate a valid authentication code using https://www.uuidgenerator.net/version4");
            logger.severe("Most Sledgehammer features will now be disabled.");
            for(int x = 0; x < 2; x++) {
                logger.severe("");
            }
            logger.severe("------------------------------");
            return;
        }


        addonManager.onEnable();


        if(!ConfigHandler.warpCommand.equals("")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpCommand());
        }

        if(ConfigHandler.globalTpll) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TpllCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TplloCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new CsTpllCommand());
        }

        if(ConfigHandler.borderTeleportation && !ConfigHandler.useOfflineMode) {
            logger.warning("------------------------------");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("Automatic border teleportation was enabled without an offline OSM database.");
            logger.warning("This feature will now be disabled.");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("------------------------------");
            ConfigHandler.borderTeleportation = false;
        }

        if(ConfigHandler.borderTeleportation && !ConfigHandler.doesOfflineExist) {
            logger.warning("------------------------------");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("The offline OSM database was enabled without a proper database configured.");
            logger.warning("Please follow the guide on https://github.com/noahhusby/sledgehammer to configure an offline database.");
            logger.warning("This feature will now be disabled.");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("------------------------------");
            ConfigHandler.borderTeleportation = false;
        }

        ProxyServer.getInstance().registerChannel("sledgehammer:channel");

        OpenStreetMaps.getInstance();

        MapHandler.getInstance().init();
    }

    @Override
    public void onDisable() {
        addonManager.onDisable();
    }

    @Override
    public void onLoad() {
        addonManager = AddonManager.getInstance();
        addonManager.registerAddon(new TerramapAddon());
        addonManager.onLoad();
    }

    @EventHandler
    public void onMessage(PluginMessageEvent e) {
        addonManager.onPluginMessage(e);
        SledgehammerNetworkManager.getInstance().onPluginMessageReceived(e);
    }

    @EventHandler
    public void onPlayerJoin(PostLoginEvent e) {
        PlayerManager.getInstance().onPlayerJoin(e.getPlayer());
        if(e.getPlayer().hasPermission("sledgehammer.admin") && !ConfigHandler.getInstance().isAuthCodeConfigured()) {
            ChatHelper.sendAuthCodeWarning(e.getPlayer());
        }
    }

    @EventHandler
    public void onPlayerDisconnect(PlayerDisconnectEvent e) {
        PlayerManager.getInstance().onPlayerDisconnect(e.getPlayer());
    }

    @EventHandler
    public void onServerJoin(ServerConnectedEvent e) {
        ServerConfig.getInstance().onServerJoin(e);
    }

    public static void setupListener(Listener l) {
        ProxyServer.getInstance().getPluginManager().registerListener(sledgehammer, l);
    }
}
