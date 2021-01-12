/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - OpenStreetMaps.java
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

package com.noahhusby.sledgehammer.datasets;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import net.md_5.bungee.api.config.ServerInfo;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Map;

public class OpenStreetMaps {
    private static OpenStreetMaps instance = null;

    public static OpenStreetMaps getInstance() {
        return instance == null ? instance = new OpenStreetMaps() : instance;
    }

    private OpenStreetMaps() {}

    public void init() {
        try {
            if(ConfigHandler.borderTeleportation && ConfigHandler.getInstance().getOfflineBin().exists())
                offlineGeocoder = new ReverseGeocoder(ConfigHandler.getInstance().getOfflineBin());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private ReverseGeocoder offlineGeocoder;

    /**
     * Gets a server from geographical coordinates
     * @param lon Longitude
     * @param lat Latitude
     * @return Returns {@link ServerInfo} if a valid region is found, or null if not
     */
    public ServerInfo getServerFromLocation(double lon, double lat) {
        return getServerFromLocation(lon, lat, ConfigHandler.useOfflineMode);
    }

    /**
     * Gets a server from geographical coordinates
     * @param lon Longitude
     * @param lat Latitude
     * @param offline True to use offline database
     * @return Returns {@link ServerInfo} if a valid region is found, or null if not
     */
    public ServerInfo getServerFromLocation(double lon, double lat, boolean offline) {
        Location location = offline ? getOfflineLocation(lon, lat) : getLocation(lon, lat);
        if(location == null) return null;
        Map<Location.detail, ServerInfo> serverInfoMap = Maps.newHashMap();

        for (SledgehammerServer s : ServerConfig.getInstance().getServers()) {
            if (!s.isEarthServer()) continue;
            if(s.getLocations() == null || s.getLocations().isEmpty()) continue;
            for (Location l : s.getLocations()) {
                switch (l.detailType) {
                    case city:
                        if (location.city.equalsIgnoreCase(l.city) &&
                                (location.state.equalsIgnoreCase(l.state) ||
                                        location.country.equalsIgnoreCase(l.country))) {
                            serverInfoMap.put(l.detailType, s.getServerInfo());
                            continue;
                        }
                        break;
                    case county:
                        if (!l.country.equals("")) {
                            if (location.county.equalsIgnoreCase(l.county) &&
                                    location.state.equalsIgnoreCase(l.state) &&
                                    location.country.equalsIgnoreCase(l.country)) {
                                serverInfoMap.put(l.detailType, s.getServerInfo());
                                continue;
                            }
                        } else {
                            if (location.county.equalsIgnoreCase(l.county) &&
                                    location.state.equalsIgnoreCase(l.state)) {
                                serverInfoMap.put(l.detailType, s.getServerInfo());
                                continue;
                            }
                        }
                        break;
                    case state:
                        if (location.state.equalsIgnoreCase(l.state) &&
                                location.country.equalsIgnoreCase(l.country)) {
                            serverInfoMap.put(l.detailType, s.getServerInfo());
                            continue;
                        }
                        break;
                    case country:
                        if (location.country.equalsIgnoreCase(l.country)) {
                            serverInfoMap.put(l.detailType, s.getServerInfo());
                            continue;
                        }
                        break;
                }
            }
        }

        if (serverInfoMap.get(Location.detail.city) != null)
            return serverInfoMap.get(Location.detail.city);

        if (serverInfoMap.get(Location.detail.county) != null)
            return serverInfoMap.get(Location.detail.county);

        if (serverInfoMap.get(Location.detail.state) != null)
            return serverInfoMap.get(Location.detail.state);

        if (serverInfoMap.get(Location.detail.country) != null)
            return serverInfoMap.get(Location.detail.country);

        return null;
    }

    /**
     * Generates {@link Location} from geographical coordinates
     * @param lon Longitude
     * @param lat Latitude
     * @return {@link Location}
     */
    public Location getLocation(double lon, double lat) {
        return getLocation(lon, lat, ConfigHandler.zoom);
    }

    /**
     * Generates {@link Location} from geographical coordinates
     * @param lon Longitude
     * @param lat Latitude
     * @param zoom Zoom level
     * @return {@link Location}
     */
    public Location getLocation(double lon, double lat, int zoom) {
        try {
            String fullRequest = Constants.nominatimAPI.replace("{zoom}", String.valueOf(zoom)) + "&lon="+lon+"&accept-language=en&lat="+lat;

            URL url = new URL(fullRequest);
            HttpURLConnection con = (HttpURLConnection)url.openConnection();
            con.setRequestProperty("User-Agent", Constants.PLUGINID+"/"+Constants.VERSION);
            con.setRequestProperty("Accept", "application/json");

            try(BufferedReader br = new BufferedReader(
                    new InputStreamReader(con.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }

                JSONParser parser = new JSONParser();
                JSONObject geocode = (JSONObject) parser.parse(response.toString());
                JSONObject address = (JSONObject) geocode.get("address");

                String city = (String) address.get("city");
                if(city == null && (address.get("town") != null)) {
                    city = (String) address.get("town");
                }
                String county = (String) address.get("county");
                String state = (String) address.get("state");
                if(state == null && (address.get("territory") != null)) {
                    state = (String) address.get("territory");
                }
                String country = (String) address.get("country");

                return new Location(Location.detail.none, city, county, state, country);
            }
        } catch (IOException | ParseException | NullPointerException e) {
            return null;
        }
    }

    /**
     * Get location from geographical coordinates from an offline bin
     * @param lon Longitude
     * @param lat Latitude
     * @return {@link Location}
     */
    public Location getOfflineLocation(double lon, double lat) {
        String[] data = offlineGeocoder.lookup((float) lon, (float) lat);
        String city = null;
        String county = null;
        String state = null;
        String country = null;
        for(int x = 0; x < data.length; x++) {
            OfflineDataField o = getDataField(data[x]);
            if (o.type.equalsIgnoreCase("city")) {
                city = o.data;
            } else if(o.type.equalsIgnoreCase("county")) {
                county = o.data;
            } else if(o.type.equalsIgnoreCase("state")) {
                state = o.data;
            } else if(o.type.equalsIgnoreCase("country")) {
                country = o.data;
            } else if (o.admin.equals("8") && city == null) {
                city = o.data;
            } else if(o.admin.equals("6") && county == null) {
                county = o.data;
            } else if(o.admin.equals("4") && state == null) {
                state = o.data;
            } else if(o.admin.equals("2") && country == null) {
                country = o.data;
            }
        }

        Location l = new Location(Location.detail.none, city, county, state, country);
        return l;
    }

    /**
     * Gets estimated data from offline OSM database
     * @param f The parsed offline data field
     * @return {@link OfflineDataField}
     */
    private static OfflineDataField getDataField(String f) {
        if(f == null) return null;
        String[] data = f.trim().replaceAll(" ", "space").replaceAll("\\s+", ";;")
                .replaceAll("space", " ").trim().split(";;");

        String a = "";
        String b = "";
        String c = "";

        if(data.length > 1) a = data[1];
        if(data.length > 2) b = data[2];

        c = data[data.length-1].trim();

        return new OfflineDataField(a, b, c);
    }
}
