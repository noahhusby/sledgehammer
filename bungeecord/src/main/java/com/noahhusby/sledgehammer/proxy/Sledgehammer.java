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

package com.noahhusby.sledgehammer.proxy;

import com.google.common.collect.Lists;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import com.noahhusby.sledgehammer.proxy.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.proxy.terramap.TerramapAddon;
import com.noahhusby.sledgehammer.proxy.commands.BorderCommand;
import com.noahhusby.sledgehammer.proxy.commands.CsTpllCommand;
import com.noahhusby.sledgehammer.proxy.commands.SledgehammerAdminCommand;
import com.noahhusby.sledgehammer.proxy.commands.SledgehammerCommand;
import com.noahhusby.sledgehammer.proxy.commands.TpllCommand;
import com.noahhusby.sledgehammer.proxy.commands.TplloCommand;
import com.noahhusby.sledgehammer.proxy.commands.WarpCommand;
import com.noahhusby.sledgehammer.proxy.config.ConfigHandler;
import com.noahhusby.sledgehammer.proxy.datasets.OpenStreetMaps;
import com.noahhusby.sledgehammer.proxy.modules.ModuleHandler;
import com.noahhusby.sledgehammer.proxy.network.NetworkHandler;
import com.noahhusby.sledgehammer.proxy.players.BorderCheckerThread;
import com.noahhusby.sledgehammer.proxy.players.FlaggedBorderCheckerThread;
import com.noahhusby.sledgehammer.proxy.players.PlayerManager;
import lombok.Getter;
import lombok.NoArgsConstructor;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PostLoginEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.plugin.Plugin;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.logging.Logger;

public class  Sledgehammer extends Plugin implements Listener {
    public static Logger logger;
    @Getter
    private static Sledgehammer instance;

    @Override
    public void onEnable() {
        instance = this;
        logger = getLogger();
        ConfigHandler.getInstance().init(getDataFolder());
        ModuleHandler.getInstance().registerModules(PlayerManager.getInstance(), NetworkHandler.getInstance(), OpenStreetMaps.getInstance(), PermissionHandler.getInstance());
        load();
    }

    @Override
    public void onDisable() {
        removeListener(this);
        ProxyServer.getInstance().getScheduler().cancel(this);
        ProxyServer.getInstance().getPluginManager().unregisterCommands(this);
        ModuleHandler.getInstance().disableAll();
        ConfigHandler.getInstance().unload();
    }

    public void reload() {
        Sledgehammer.logger.warning("Reloading Sledgehammer!");
        Sledgehammer.logger.info("");
        onDisable();
        ConfigHandler.getInstance().reload();
        load();
        Sledgehammer.logger.info("");
        Sledgehammer.logger.warning("Reloaded Sledgehammer!");
    }

    public void load() {
        addListener(this);
        ConfigHandler.getInstance().load();
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerCommand());
        ProxyServer.getInstance().getPluginManager().registerCommand(this, new SledgehammerAdminCommand());

        if (!ConfigHandler.getInstance().isAuthCodeConfigured()) {
            ChatUtil.sendMessageBox(ProxyServer.getInstance().getConsole(), ChatColor.DARK_RED + "WARNING", ChatUtil.combine(ChatColor.RED,
                    "The sledgehammer authentication code is not configured, or is configured incorrectly.\n" +
                    "Please generate a valid authentication code using https://www.uuidgenerator.net/version4\n"
                    + "Most Sledgehammer features will now be disabled."));
            return;
        }

        if (ConfigHandler.terramapEnabled && TerramapAddon.instance == null) {
            ModuleHandler.getInstance().registerModule(new TerramapAddon());
            ModuleHandler.getInstance().enable(TerramapAddon.instance);
        } else if(ConfigHandler.terramapEnabled) {
            ModuleHandler.getInstance().enable(TerramapAddon.instance);
        } else if(TerramapAddon.instance != null && ModuleHandler.getInstance().getModules().containsKey(TerramapAddon.instance)) {
            ModuleHandler.getInstance().disable(TerramapAddon.instance);
            ModuleHandler.getInstance().getModules().remove(TerramapAddon.instance);
        }

        if (!ConfigHandler.warpCommand.equals("")) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new WarpCommand(ConfigHandler.warpCommand));
        }

        if (ConfigHandler.globalTpll) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TpllCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new TplloCommand());
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new CsTpllCommand());
        }

        if (ConfigHandler.borderTeleportation && !ConfigHandler.doesOfflineExist) {
            ChatUtil.sendMessageBox(ProxyServer.getInstance().getConsole(), ChatColor.DARK_RED + "WARNING", ChatUtil.combine(ChatColor.RED,
                    "Automatic border teleportation was enabled without an offline OSM database.\n" +
                    "This feature will now be disabled."));
            ConfigHandler.borderTeleportation = false;
        }

        if (ConfigHandler.useOfflineMode && !ConfigHandler.doesOfflineExist) {
            ChatUtil.sendMessageBox(ProxyServer.getInstance().getConsole(), ChatColor.DARK_RED + "WARNING", ChatUtil.combine(ChatColor.RED,
                    "The offline OSM database was enabled without a proper database configured.\n" +
                    "Please follow the guide on https://github.com/noahhusby/Sledgehammer/wiki/Border-Offline-Database to configure an offline database.\n" +
                    "This feature will now be disabled."));
            ConfigHandler.useOfflineMode = false;
        }

        ProxyServer.getInstance().registerChannel(Constants.serverChannel);

        if (ConfigHandler.borderTeleportation) {
            ProxyServer.getInstance().getPluginManager().registerCommand(this, new BorderCommand());
            ProxyServer.getInstance().getScheduler().schedule(this, new BorderCheckerThread(), 0, 10, TimeUnit.SECONDS);
            ProxyServer.getInstance().getScheduler().schedule(this, new FlaggedBorderCheckerThread(), 0, 10, TimeUnit.SECONDS);
        }

        ModuleHandler.getInstance().enableAll();
    }

    /**
     * Add a new listener to the Sledgehammer plugin
     *
     * @param listener {@link Listener}
     */
    public static void addListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().registerListener(instance, listener);
    }

    /**
     * Removes a listener
     *
     * @param listener {@link Listener}
     */
    public static void removeListener(Listener listener) {
        ProxyServer.getInstance().getPluginManager().unregisterListener(listener);
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerJoin(PostLoginEvent e) {
        if (e.getPlayer().hasPermission("sledgehammer.admin") && !ConfigHandler.getInstance().isAuthCodeConfigured()) {
            ChatUtil.sendAuthCodeWarning(e.getPlayer());
        }
    }
}
