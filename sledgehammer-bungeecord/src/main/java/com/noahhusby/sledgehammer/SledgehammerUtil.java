/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerUtil.java
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

import com.google.gson.Gson;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.datasets.projection.GeographicProjection;
import com.noahhusby.sledgehammer.datasets.projection.ModifiedAirocean;
import com.noahhusby.sledgehammer.datasets.projection.ScaleProjection;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import org.apache.commons.lang3.Validate;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Random;

public class SledgehammerUtil {

    private static final GeographicProjection projection = new ModifiedAirocean();
    private static final GeographicProjection uprightProj = GeographicProjection.orientProjection(projection, GeographicProjection.Orientation.upright);
    private static final ScaleProjection scaleProj = new ScaleProjection(uprightProj, Constants.SCALE, Constants.SCALE);

    /**
     * Gets the geographical location from in-game coordinates
     * @param x X-Axis in-game
     * @param z Z-Axis in-game
     * @return The geographical location (Long, Lat)
     */
    public static double[] toGeo(double x, double z) {
        return scaleProj.toGeo(x, z);
    }

    /**
     * Gets in-game coordinates from geographical location
     * @param lon Geographical Longitude
     * @param lat Geographic Latitude
     * @return The in-game coordinates (x, z)
     */
    public static double[] fromGeo(double lon, double lat) {
        return scaleProj.fromGeo(lon, lat);
    }
    
    /**
     * Gets the geographic projection used to convert coordinates
     * @author SmylerMC
     * @return a BTE compatible projection
     */
    public static GeographicProjection getBTEProjection() {
    	return scaleProj;
    }

    /**
     * Gets Bungeecord server by it's name
     * @param name The name of the BungeeCord server
     * @return The Bungeecord server. Result will be null if no matching server is found.
     */
    public static ServerInfo getServerByName(String name) {
        return ProxyServer.getInstance().getServerInfo(name);
    }

    /**
     * Gets the current Bungeecord server that a player is on
     * @param sender The sender
     * @return The Bungeecord server. Result will be null if no matching server is found.
     */
    public static ServerInfo getServerFromSender(CommandSender sender) {
        if(!(sender instanceof ProxiedPlayer)) return null;
        return ((ProxiedPlayer) sender).getServer().getInfo();
    }

    /**
     * Gets Bungeecord server from player name
     * @param name The name of the player
     * @return The Bungeecord server. Result will be null if no matching server is found.
     * @deprecated As of release 0.4, replaced by {@link #getServerNameByPlayer(ProxiedPlayer)}
     */
    @Deprecated public static ServerInfo getServerFromPlayer(String name) {
        return ProxyServer.getInstance().getPlayer(name).getServer().getInfo();
    }

    /**
     * Gets name of Bungeecord server from player
     * @param player The name of the player
     * @return The name of the Bungeecord server. Result will be null if no matching server is found.
     * @deprecated As of release 0.4, replaced by {@link #getServerFromSender(CommandSender)}
     */
    @Deprecated public static String getServerNameByPlayer(ProxiedPlayer player) {
        return player.getServer().getInfo().getName();
    }

    /**
     * Checks if a Bungeecord server is a sledgehammer server
     * @param server The Bungeecord server
     * @return True if the Bungeecord server is a Sledgehammer server, False if not
     */
    public static boolean isSledgehammerServer(ServerInfo server) {
        for(SledgehammerServer s : ServerConfig.getInstance().getServers()) {
            if(s.getServerInfo() == null) continue;
            if(s.getServerInfo().equals(server)) return true;
        }
        return false;
    }

    /**
     * Checks if a Bungeecord server is a sledgehammer server
     * @param name The name of the Bungeecord server
     * @return True if the Bungeecord server is a Sledgehammer server, False if not
     * @deprecated As of release 0.4, replaced by {@link #isSledgehammerServer(ServerInfo)}
     */
    @Deprecated public static boolean isSledgehammerServer(String name) {
        for(SledgehammerServer s : ServerConfig.getInstance().getServers()) {
            if(s.getName().equals(name)) return true;
        }
        return false;
    }

    /**
     * Checks if player is within a build region
     * @param player The player
     * @return True if they are within the region, false if not
     */
    public static boolean inEarthRegion(SledgehammerPlayer player) {
        double[] geo = toGeo(Double.parseDouble(player.getLocation().x), Double.parseDouble(player.getLocation().z));
        return !(geo == null || geo.length < 1 || Double.isNaN(geo[0]) || Double.isNaN(geo[1]));
    }

    /**
     * Checks if an incoming request matches the Sledgehammer authentication code
     * @param u The incoming authentication code
     * @return True if codes math, false if not
     */
    public static boolean isGenuineRequest(String u) {
        try {
            return u.equals(ConfigHandler.authenticationCode);
        } catch (Exception e) {
            Sledgehammer.logger.info("Error occurred while parsing incoming authentication command!");
            return false;
        }
    }

    /**
     * Gets all objects in a string array above a given index
     * @param args Initial array
     * @param index Starting index
     * @return Selected array
     */
    public static String[] selectArray(String[] args, int index) {
        List<String> array = new ArrayList<>();
        for(int i = index; i < args.length; i++)
            array.add(args[i]);

        return array.toArray(array.toArray(new String[array.size()]));
    }

    /**
     * Gets a space seperated string from an array
     * @param args A string array
     * @return The space seperated String
     */
    public static String getRawArguments(String[] args) {
        if(args.length == 0) return "";
        if(args.length == 1) return args[0];

        StringBuilder arguments = new StringBuilder(args[0]);

        for(int x = 1; x < args.length; x++)
            arguments.append(" ").append(args[x]);

        return arguments.toString();
    }

    /**
     * Generates a random 6-character string
     * @return Random string
     */
    public static String getSaltString() {
        String SALTCHARS = "ABCDEFGHIJKLMNOPQRSTUVWXYZ1234567890";
        StringBuilder salt = new StringBuilder();
        Random rnd = new Random();
        while (salt.length() < 6) {
            int index = (int) (rnd.nextFloat() * SALTCHARS.length());
            salt.append(SALTCHARS.charAt(index));
        }
        String saltStr = salt.toString();
        return saltStr;
    }

    public String getLastArgument(String[] a) {
        return a.length == 0 ? null : a[a.length - 1];
    }


    public static <T extends Collection<? super String>> T copyPartialMatches(final String token, final Iterable<String> originals, final T collection) throws UnsupportedOperationException, IllegalArgumentException {
        Validate.notNull(token, "Search token cannot be null");
        Validate.notNull(collection, "Collection cannot be null");
        Validate.notNull(originals, "Originals cannot be null");

        for (String string : originals) {
            if (startsWithIgnoreCase(string, token)) {
                collection.add(string);
            }
        }

        return collection;
    }

    public static boolean startsWithIgnoreCase(final String string, final String prefix) throws IllegalArgumentException, NullPointerException {
        Validate.notNull(string, "Cannot check a null string for a match");
        if (string.length() < prefix.length()) {
            return false;
        }
        return string.regionMatches(true, 0, prefix, 0, prefix.length());
    }

    public static class JsonUtils {
        public static Gson gson = new Gson();
        public static JSONObject toObject(String s) {
            try {
                return (JSONObject) new JSONParser().parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static JSONArray toArray(Object o) {
            return toArray((String) o);
        }

        public static JSONArray toArray(String s) {
            try {
                return (JSONArray) new JSONParser().parse(s);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            return null;
        }

        public static int fromBoolean(boolean b) {
            return b ? 1 : 0;
        }

        public static boolean fromBooleanValue(long l) {
            return new Long(l).intValue() != 0;
        }

        public static int toInt(Object val) {
            int x = 0;
            if(val instanceof Long) {
                x = ((Long) val).intValue();
            } else if(val instanceof Double) {
                x = ((Double) val).intValue();
            } else {
                x = (int) val;
            }
            return x;
        }
    }
}
