/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ConfigHandler.java
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

package com.noahhusby.sledgehammer.btenet;

import com.google.common.collect.Maps;
import com.noahhusby.lib.data.sql.Credentials;
import com.noahhusby.lib.data.sql.MySQL;
import com.noahhusby.lib.data.storage.Storage;
import com.noahhusby.lib.data.storage.handlers.LocalStorageHandler;
import com.noahhusby.lib.data.storage.handlers.SQLStorageHandler;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.players.PlayerManager;
import com.noahhusby.sledgehammer.warp.WarpHandler;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public class BTEConfig {
    private static BTEConfig instance = null;

    public static BTEConfig getInstance() {
        return instance == null ? instance = new BTEConfig() : instance;
    }

    private File dataFolder;

    private BTEConfig() { }

    public static net.minecraftforge.common.config.Configuration config;

    public static String sqlHost;
    public static int sqlPort;
    public static String sqlUser;
    public static String sqlPassword;
    public static String sqlDb;

    private String category;

    Map<String, List<String>> categories = Maps.newHashMap();

    /**
     * Creates initial data structures upon startup
     * @param dataFolder Sledgehammer plugin folder
     */
    public void init(File dataFolder) {
        this.dataFolder = dataFolder;

        createConfig();
        config = new net.minecraftforge.common.config.Configuration(new File(dataFolder, "buildtheearth.cfg"));
        loadData();
        config.save();
    }

    /**
     * Reloads all data/data fields. Called upon startup or reload
     */
    public void loadData() {

        cat("MySQL Database", "Settings for the MySQL Database");
        sqlHost = config.getString(prop("Host"), category, "127.0.0.1", "The host IP for the database.");
        sqlPort = config.getInt(prop("Port"), category, 3306, 0, 65535,"The port for the database.");
        sqlUser = config.getString(prop("Username"), category, "", "The username for the database.");
        sqlPassword = config.getString(prop("Password"), category, "", "The password for the database.");
        sqlDb = config.getString(prop("Database"), category, "", "The name of the database.");

        order();

    }

    /**
     * Reloads the config
     */
    public void reload() {
        Sledgehammer.logger.info("Reloaded the config");
        loadData();
        Sledgehammer.sledgehammer.registerFromConfig();
    }

    private String prop(String n) {
        categories.get(category).add(n);
        return n;
    }

    private void cat(String category, String comment) {
        this.category = category;
        if(!categories.containsKey(category)) {
            categories.put(category, new ArrayList<>());
        }
        config.addCustomCategoryComment(category, comment);
    }

    private void order() {
        config.setCategoryPropertyOrder(category, categories.get(category));
    }

    private void createConfig() {
        if (!dataFolder.exists()) dataFolder.mkdir();
    }
}
