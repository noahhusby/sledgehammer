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

package com.noahhusby.sledgehammer.config;


import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import com.google.common.collect.Maps;
import com.noahhusby.lib.data.sql.Credentials;
import com.noahhusby.lib.data.sql.MySQL;
import com.noahhusby.lib.data.storage.Storage;
import com.noahhusby.lib.data.storage.compare.CutComparator;
import com.noahhusby.lib.data.storage.compare.ValueComparator;
import com.noahhusby.lib.data.storage.handlers.LocalStorageHandler;
import com.noahhusby.lib.data.storage.handlers.SQLStorageHandler;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.MapStyleRegistry;
import com.noahhusby.sledgehammer.players.PlayerManager;
import com.noahhusby.sledgehammer.warp.WarpHandler;

public class ConfigHandler {
    private static ConfigHandler mInstance = null;

    public static ConfigHandler getInstance() {
        if(mInstance == null) mInstance = new ConfigHandler();
        return mInstance;
    }

    private File dataFolder;

    public static File warpFile;
    public static File serverFile;
    public static File localStorage;
    public static File attributeFile;
    public static File groupsFile;

    private ConfigHandler() { }

    public static net.minecraftforge.common.config.Configuration config;
    private boolean authCodeConfigured;

    public static String authenticationCode = "";
    public static int proxyId = 0;
    public static int proxyTotal = -1;
    public static String messagePrefix = "";
    public static boolean globalTpll = false;
    public static boolean replaceNotAvailable = false;
    public static boolean debug = false;

    public static String warpCommand = "";
    public static boolean localWarp = false;
    public static boolean warpMenuDefault = false;
    public static String warpMenuPage = "";
    public static boolean showPinnedOnBlank = false;

    public static boolean useSql;
    public static String sqlHost;
    public static int sqlPort;
    public static String sqlUser;
    public static String sqlPassword;
    public static String sqlDb;
    public static long autoLoad;

    public static boolean useOfflineMode = false;
    public static boolean borderTeleportation = false;
    public static int zoom = 0;

    public static boolean doesOfflineExist = false;

    public static boolean mapEnabled = false;
    public static String mapHost = "";
    public static String mapPort = "";

    public static String mapTitle = "";
    public static String mapSubtitle = "";

    public static double startingLon = 0.0;
    public static double startingLat = 0.0;
    public static int startingZoom = 0;
    public static int mapTimeout = 15;
    public static String mapLink = "";

    public static boolean terramapEnabled;
    public static boolean terramapSyncPlayers;
    public static int terramapSyncInterval;
    public static int terramapSyncTimeout;
    public static boolean terramapPlayersDisplayDefault;
    public static boolean terramapSendCustomMapsToClient;
    public static boolean terramapGlobalMap;
    public static boolean terramapGlobalSettings;
    public static String terramapProxyUUID;

    private String category;

    Map<String, List<String>> categories = Maps.newHashMap();

    /**
     * Creates initial data structures upon startup
     * @param dataFolder Sledgehammer plugin folder
     */
    public void init(File dataFolder) {
        this.dataFolder = dataFolder;
        localStorage = new File(dataFolder, "local");
        if(!localStorage.exists()) localStorage.mkdir();
        warpFile = new File(localStorage, "warps.json");
        serverFile = new File(localStorage, "servers.json");
        attributeFile = new File(localStorage, "attributes.json");
        groupsFile = new File(localStorage, "groups.json");

        if (!dataFolder.exists())
            dataFolder.mkdir();

        config = new net.minecraftforge.common.config.Configuration(new File(dataFolder, "sledgehammer.cfg"));

        loadData();
    }

    /**
     * Reloads all data/data fields. Called upon startup or reload
     */
    public void loadData() {
        Storage serverData = ServerConfig.getInstance().getServers();
        serverData.clearHandlers();
        if(serverData.getComparator() instanceof CutComparator)
            serverData.setComparator(new ValueComparator("Name"));

        Storage warpData = WarpHandler.getInstance().getWarps();
        warpData.clearHandlers();
        if(warpData.getComparator() instanceof CutComparator)
            warpData.setComparator(new ValueComparator("Id"));

        Storage attributeData = PlayerManager.getInstance().getAttributes();
        attributeData.clearHandlers();
        if(attributeData.getComparator() instanceof CutComparator)
            attributeData.setComparator(new ValueComparator("UUID"));

        Storage serverGroups = ServerConfig.getInstance().getGroups();
        serverGroups.clearHandlers();
        if(serverGroups.getComparator() instanceof CutComparator)
            serverGroups.setComparator(new ValueComparator("Id"));

        config.load();
        cat("General", "General options for sledgehammer");
        authenticationCode = config.getString(prop("Network Authentication Code"), "General", "",
                "Generate a new key using https://uuidgenerator.net/version4\n" +
                        "All corresponding sledgehammer clients must have the same code\n" +
                        "Don't share this key with anyone you don't trust as it will allow anybody to run any command on connected servers.");

        Pattern p = Pattern.compile("[0-9a-f]{8}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{4}-[0-9a-f]{12}$");
        authCodeConfigured = p.matcher(authenticationCode).matches();

        proxyId = config.getInt(prop("Proxy ID"), category, 0, 0, 100,
                "The Id of the proxy. The first proxy starts at zero. Leave at zero if this is a single-proxy configuration");
        proxyTotal = config.getInt(prop("Proxy Total"), category, -1, -1, 100,
                "The total amount of proxies, if a multi-server setup. Zero includes first proxy.\n" +
                        "Set this to -1 if this is a single-proxy configuaraion");
        messagePrefix = config.getString(prop("Message Prefix"), "General", "&9&lBTE &8&l> "
                , "The prefix of messages broadcasted to players from the proxy.");
        globalTpll = config.getBoolean(prop("Global Tpll"), "General", true, "Set this to false to disable global tpll. [/tpll & /cs tpll]");
        replaceNotAvailable = config.getBoolean(prop("Replace Not Available Message"), category, false,
                "When enabled, the default 'This command is not available!' is replaced with 'Unknown Command.'");
        debug = config.getBoolean(prop("Debug"), category, false, "Enable to see advanced logging information.");
        order();

        cat("MySQL Database", "Settings for the MySQL Database");
        useSql = config.getBoolean(prop("Enable SQL"), category, false, "Should SQL be used to synchronize/store data?");
        sqlHost = config.getString(prop("Host"), category, "127.0.0.1", "The host IP for the database.");
        sqlPort = config.getInt(prop("Port"), category, 3306, 0, 65535,"The port for the database.");
        sqlUser = config.getString(prop("Username"), category, "", "The username for the database.");
        sqlPassword = config.getString(prop("Password"), category, "", "The password for the database.");
        sqlDb = config.getString(prop("Database"), category, "", "The name of the database.");
        autoLoad = config.getInt(prop("Auto Load Time"), category, 30, 10, 3600, "How often SH should automatically refresh storage data (in seconds).");
        order();

        cat("Warps", "Options for warps");
        warpCommand = config.getString(prop("Warp Command"), category, "nwarp",
                "The command for network-wide warping. Leave blank to disable.");
        localWarp = config.getBoolean(prop("Local Mode"), category, false,
                "Local warp allows for each individual server to set their own warps.\n" +
                "Standard warp [false] is a shared pool of warps for the entire network.\n" +
                "All warps are accessible regardless of warp mode.");
        warpMenuDefault = config.getBoolean(prop("Default Warp Menu"), category, true,
                "This will open the interactive GUI by default when the warp command is called.\n" +
            "Otherwise `/[warp command] menu` is required to open the menu.");
        warpMenuPage = config.getString(prop("Default Menu Page"), category, "group",
                "This is the main page that will be shown upon the warp menu opening.\n" +
                        "Use `group` to show the warps on the current server (or groups of servers)\n" +
                        "Use `all` to show all the warps by default\n" +
                        "or Use `pinned` to show the pinned warps.");
        showPinnedOnBlank = config.getBoolean(prop("Pinned on Blank"), category, false,
                "If enabled, the warp GUI will show the pinned page if that server has no warps.");
        order();


        cat("Geography", "Options for OpenStreetMaps and Teleportation.");
        zoom = config.getInt(prop("Zoom level"), "Geography", 12, 1, 19,
                "Zoom detail level for OSM requests.");
        useOfflineMode = config.getBoolean(prop("OSM Offline Mode"), "Geography", false,
                "Set false for fetching the latest data from OSM (more up to date), or true for using a downloaded database.\n" +
                        "Please follow the guide on https://github.com/noahhusby/Sledgehammer/wiki/Border-Offline-Database about downloading and configuring the offline database.");
        borderTeleportation = config.getBoolean(prop("Auto Border Teleportation"), "Geography", false,
                "Set to false to disable automatic border teleportation, or true to enable it. Border teleportation requires the offline database to be configured.");
        order();

        /*
        cat("Map", "Options for sledgehammer's map");
        mapEnabled = config.getBoolean(prop("Enable"), "Map", false,
                "Set this to true to enable sledgehammer's map");
        mapEnabled = false;
        mapHost = config.getString(prop("Host"), "Map", "127.0.0.1",
                "The websocket url/ip where sledgehammer map is running");
        mapPort = config.getString(prop("Port"), "Map", "7000",
                "The port that the map websocket is running.\nThe websocket port can be changed in the map's config file");
        mapTitle = config.getString(prop("Title"), "Map", "A BTE Network", "");
        mapSubtitle = config.getString(prop("Subtitle"), "Map", "IP: bte-network.net", "");
        startingLon = config.getFloat(prop("Longitude"), "Map", 0, -90, 90,
                "The starting longitude when the map loads.");
        startingLat = config.getFloat(prop("Latitude"), "Map", 0, -90, 90,
                "The starting latitude when the map loads.");
        startingZoom = config.getInt(prop("Zoom"), "Map", 6, 5, 18,
                "The starting zoom when the map loads");
        mapTimeout = config.getInt(prop("Map Session Timeout"), "Map", 10, 5, 60,
                "How long (in minutes) a session will last before the player needs to invoke the map command again.");
        mapLink = config.getString(prop("Map Link"), "Map", "http://map.bte-network.net",
                "The direct http link for the map. This is the link that players will interact with.\n" +
                        "NOTE: You must put either http:// or https:// at the beginning");
                          order();

         */

        cat("Terramap", "Terramap Addon Configuration.");
        terramapEnabled = config.getBoolean(prop("Enabled"), category, true, "Enables Terramap integration");
        terramapSyncPlayers = config.getBoolean(prop("Sync Players"), category, true,
        		"Wether or not to synchronize players from server to client so everyone appears on the map, no matter the distance");
        terramapSyncInterval = config.getInt(prop("Sync Interval"), category, 500, 1, Integer.MAX_VALUE,
        		"The time interval, in milliseconds at which to synchronize players with clients");
        terramapSyncTimeout = config.getInt(prop("Sync Timeout"), category, 120000, 20000, Integer.MAX_VALUE,
                "The default time interval, in milliseconds, before a clients needs to register again to continue getting player position updates.");
        terramapPlayersDisplayDefault = config.getBoolean(prop("Player Display Default"), category, true,
        		"If player sync is enabled, sould players be displayed by default (true) or should they explicitly opt-in with /terrashow (false)");
        terramapSendCustomMapsToClient = config.getBoolean(prop("Send Custom Maps to Clients"), category, true,
        		"Set to false if you do not want to send custom maps to clients. This is only for testing, as if you don't want to send map styles to client, the first thing to do is to not configure any.");
        terramapGlobalMap = config.getBoolean(prop("Global Map"), category, true,
        		"Set this to false to only allow players to use the map when they are on an Earth world.");
        terramapGlobalSettings = config.getBoolean(prop("Global Settings"), category, false,
        		"Set this to true is you want client's settings to be saved for the entire network instead of per-world.");
        terramapProxyUUID = config.getString(prop("Proxy UUID"), "terramap", UUID.randomUUID().toString(), 
        		"A UUID v4 that will be used by Terramap clients to identify this network when saving settings. DO NOT PUT YOUR NETWORK AUTHENTIFICATION CODE IN HERE, THIS IS SHARED WITH CLIENTS! You want this to be the same on all your network's proxies. The default value is randomly generated.");

        order();
        if(config.hasChanged()) config.save();

        File f = new File(localStorage, "offline.bin");
        doesOfflineExist = f.exists();
        
        if(terramapEnabled) {        	
        	File customMaps = new File(dataFolder + "/" + MapStyleRegistry.FILENAME);
        	MapStyleRegistry.setConfigMapFile(customMaps);
        	MapStyleRegistry.loadFromConfigFile();
        }

        serverData.registerHandler(new LocalStorageHandler(ConfigHandler.serverFile));
        warpData.registerHandler(new LocalStorageHandler(ConfigHandler.warpFile));
        attributeData.registerHandler(new LocalStorageHandler(ConfigHandler.attributeFile));
        serverGroups.registerHandler(new LocalStorageHandler(ConfigHandler.groupsFile));

        if(useSql) {
            {
                SQLStorageHandler sqlStorageHandler = new SQLStorageHandler(new MySQL(
                        new Credentials(sqlHost, sqlPort, sqlUser, sqlPassword, sqlDb)), "Servers",
                        "Name,EarthServer,Nick,Locations,XOffset,ZOffset,StealthMode",
                        "TEXT(255),TEXT(255),TEXT(255),LONGTEXT,INT,INT,TEXT(255)");
                sqlStorageHandler.setPriority(100);
                serverData.registerHandler(sqlStorageHandler);
            }

            {
                SQLStorageHandler sqlStorageHandler = new SQLStorageHandler(new MySQL(
                        new Credentials(sqlHost, sqlPort, sqlUser, sqlPassword, sqlDb)), "Warps",
                        "Id,Name,Server,Pinned,Point,HeadId",
                        "INT,TEXT(255),TEXT(255),TEXT(255),MEDIUMTEXT,TEXT(255)");
                sqlStorageHandler.setPriority(100);
                warpData.registerHandler(sqlStorageHandler);
            }

            {
                SQLStorageHandler sqlStorageHandler = new SQLStorageHandler(new MySQL(
                        new Credentials(sqlHost, sqlPort, sqlUser, sqlPassword, sqlDb)), "Attributes",
                        "UUID,Attributes",
                        "TEXT(255),MEDIUMTEXT");
                sqlStorageHandler.setPriority(100);
                attributeData.registerHandler(sqlStorageHandler);
            }

            {
                SQLStorageHandler sqlStorageHandler = new SQLStorageHandler(new MySQL(
                        new Credentials(sqlHost, sqlPort, sqlUser, sqlPassword, sqlDb)), "ServerGroups",
                        "Id,HeadId,Name,Servers,Aliases",
                        "TEXT(255),TEXT(255),TEXT(255),MEDIUMTEXT,MEDIUMTEXT");
                sqlStorageHandler.setPriority(100);
                serverGroups.registerHandler(sqlStorageHandler);
            }
        }

        serverData.setAutoLoad(autoLoad, TimeUnit.SECONDS);
        warpData.setAutoLoad(autoLoad, TimeUnit.SECONDS);
        attributeData.setAutoLoad(autoLoad, TimeUnit.SECONDS);
        serverGroups.setAutoLoad(autoLoad, TimeUnit.SECONDS);

        Sledgehammer.sledgehammer.alternativeThreads.schedule(() -> {
            serverData.load(true);
            warpData.load(true);
            attributeData.load(true);
            serverGroups.load(true);
        }, 5, TimeUnit.SECONDS);
    }

    /**
     * Gets whether the authentication code is configured correctly
     * @return True if configured correctly, false if not
     */
    public boolean isAuthCodeConfigured() {
        return authCodeConfigured;
    }

    /**
     * Reloads the config
     */
    public void reload() {
        Sledgehammer.logger.info("Reloaded the config");
        loadData();
        Sledgehammer.sledgehammer.registerFromConfig();
    }

    /**
     * Migrates the data from local storage to databases
     */
    public void migrate() {
        ServerConfig.getInstance().getServers().migrate(0);
        ServerConfig.getInstance().getGroups().migrate(0);
        WarpHandler.getInstance().getWarps().migrate(0);
        PlayerManager.getInstance().getAttributes().migrate(0);
    }

    /**
     * Gets the file for the offline OSM database
     * @return {@link File}
     */
    public File getOfflineBin() {
        return new File(localStorage, "offline.bin");
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
}
