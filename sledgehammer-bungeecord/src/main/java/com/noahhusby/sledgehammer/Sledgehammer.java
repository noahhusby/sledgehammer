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
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import com.noahhusby.sledgehammer.addons.AddonManager;
import com.noahhusby.sledgehammer.addons.terramap.TerramapAddon;
import com.noahhusby.sledgehammer.btenet.BTENet;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.commands.BorderCommand;
import com.noahhusby.sledgehammer.commands.CsTpllCommand;
import com.noahhusby.sledgehammer.commands.SledgehammerAdminCommand;
import com.noahhusby.sledgehammer.commands.SledgehammerCommand;
import com.noahhusby.sledgehammer.commands.TpllCommand;
import com.noahhusby.sledgehammer.commands.TplloCommand;
import com.noahhusby.sledgehammer.commands.WarpCommand;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.maps.MapThread;
import com.noahhusby.sledgehammer.players.BorderCheckerThread;
import com.noahhusby.sledgehammer.players.FlaggedBorderCheckerThread;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

public class Sledgehammer extends Plugin implements Listener {
    public static Logger logger;
    public static Sledgehammer sledgehammer;
    public static AddonManager addonManager;

    public final ScheduledThreadPoolExecutor alternativeThreads = (ScheduledThreadPoolExecutor) Executors.newScheduledThreadPool(2);

    @Override
    public void onEnable() {
        sledgehammer = this;
        logger = getLogger();
        alternativeThreads.setRemoveOnCancelPolicy(true);

        ProxyServer.getInstance().getPluginManager().registerListener(this, this);
        ConfigHandler.getInstance().init(getDataFolder());
        registerFromConfig();
    }

    @Override
    public void onDisable() {
        addonManager.onDisable();
        BTENet.getInstance().onDisable();
    }

    /**
     * Called upon startup or reload. These are settings that can be changed without a restart
     */
    public void registerFromConfig() {
        List<Runnable> remove = new ArrayList<>();
        remove.addAll(alternativeThreads.getQueue());

        for(Runnable r : remove) alternativeThreads.remove(r);

        ProxyServer.getInstance().getPluginManager().unregisterCommands(this);

        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerAdminCommand());

        ServerConfig.getInstance();

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

        addonManager = AddonManager.getInstance();
        addonManager.onDisable();
        
        if(ConfigHandler.terramapEnabled) addonManager.registerAddon(new TerramapAddon());

        addonManager.onEnable();
        BTENet.getInstance().onEnable();

        if(!ConfigHandler.warpCommand.equals("")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpCommand(ConfigHandler.warpCommand));
        }

        if(ConfigHandler.globalTpll) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TpllCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TplloCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new CsTpllCommand());
        }

        if(ConfigHandler.borderTeleportation && !ConfigHandler.doesOfflineExist) {
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

        if(ConfigHandler.useOfflineMode && !ConfigHandler.doesOfflineExist) {
            logger.warning("------------------------------");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("The offline OSM database was enabled without a proper database configured.");
            logger.warning("Please follow the guide on https://github.com/noahhusby/Sledgehammer/wiki/Border-Offline-Database to configure an offline database.");
            logger.warning("This feature will now be disabled.");
            for(int x = 0; x < 2; x++) {
                logger.warning("");
            }
            logger.warning("------------------------------");
            ConfigHandler.useOfflineMode = false;
        }

        ProxyServer.getInstance().registerChannel("sledgehammer:channel");

        if(ConfigHandler.mapEnabled) {
            alternativeThreads.scheduleAtFixedRate(new MapThread(), 0, 30, TimeUnit.SECONDS);
        }

        if(ConfigHandler.borderTeleportation) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new BorderCommand());
            alternativeThreads.scheduleAtFixedRate(new BorderCheckerThread(), 0, 5, TimeUnit.SECONDS);
            alternativeThreads.scheduleAtFixedRate(new FlaggedBorderCheckerThread(), 0, 5, TimeUnit.SECONDS);
        }

        OpenStreetMaps.getInstance().init();
    }

    /**
     * Add a new listener to the Sledgehammer plugin
     * @param listener The Bungeecord listener
     */
    public static void addListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().registerListener(sledgehammer, listener);
    }

    /**
     * Print a message on the debug logger. Only outputs with debug mode enabled
     * @param m The debug message
     */
    public static void debug(String m) {
        if(ConfigHandler.debug) logger.info(m);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PostLoginEvent e) {
        if(e.getPlayer().hasPermission("sledgehammer.admin") && !ConfigHandler.getInstance().isAuthCodeConfigured()) {
            ChatHelper.sendAuthCodeWarning(e.getPlayer());
        }
    }

    /**
     * @author SmylerMC
     * @param l
     */
    public static void terminateListener(Listener l) {
        ProxyServer.getInstance().getPluginManager().unregisterListener(l);
    }
    
}
