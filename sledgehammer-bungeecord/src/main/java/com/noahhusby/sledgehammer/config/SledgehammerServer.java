/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerServer.java
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

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.noahhusby.sledgehammer.datasets.Location;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class SledgehammerServer {
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("Nick")
    private String friendlyName;
    @Expose
    @SerializedName("EarthServer")
    private boolean earthServer;
    @Expose
    @SerializedName("Locations")
    private List<Location> locations = new ArrayList<>();
    @Expose
    @SerializedName("XOffset")
    private int xOffset;
    @Expose
    @SerializedName("ZOffset")
    private int zOffset;
    @Expose
    @SerializedName("StealthMode")
    private boolean stealthMode;
    private String shVersion = null;

    public SledgehammerServer() {}

    public SledgehammerServer(String name) {
        this.name = name;
        this.friendlyName = name;
    }

    /**
     * Gets name of the server
     * @return Name of server
     */
    public String getName() {
        return name;
    }

    /**
     * Gets friendly nickname for server
     * @return Nickname of server
     */
    public String getFriendlyName() {
        return friendlyName;
    }

    /**
     * Sets the friendly nickname for server
     * @param name Nickname for server
     */
    public void setFriendlyName(String name) {
        this.friendlyName = name;
    }

    /**
     * Gets whether the server is a build server
     * @return True if a build server, false if not
     */
    public boolean isEarthServer() {
        return earthServer;
    }

    /**
     * Sets whether the server is a build server
     * @param earth True if a build server, false if not
     */
    public void setEarthServer(boolean earth) {
        this.earthServer = earth;
    }

    /**
     * Gets the list of locations on the server
     * @return List of locations
     */
    public List<Location> getLocations() {
        return locations;
    }

    /**
     * Sets the list of locations on the server
     * @param locations List of locations
     */
    public void setLocations(List<Location> locations) {
        this.locations = locations;
    }

    /**
     * Sets the x offset for tpll requests
     * @param xOffset
     */
    public void setxOffset(int xOffset) {
        this.xOffset = xOffset;
    }

    /**
     * Gets the x offset for tpll requests
     * @return X Offset
     */
    public int getxOffset() {
        return xOffset;
    }

    /**
     * Sets the z offset for tpll requests
     * @param zOffset
     */
    public void setzOffset(int zOffset) {
        this.zOffset = zOffset;
    }

    /**
     * Gets the z offset for tpll requests
     * @return Z Offset
     */
    public int getzOffset() {
        return zOffset;
    }

    /**
     * Gets the version of the sledgehammer plugin
     * @return Sledgehammer Version
     */
    public String getSledgehammerVersion() {
        return shVersion;
    }

    /**
     * Initializes server from init packet
     * @param version Sledgehammer Version
     */
    public void initialize(String version) {
        this.shVersion = version;
    }

    /**
     * Gets whether the server is initialized
     * @return True if initialized, false if not
     */
    public boolean isInitialized() {
        return shVersion != null;
    }

    /**
     * Gets {@link ServerInfo}
     * @return {@link ServerInfo}
     */
    public ServerInfo getServerInfo() {
        return ProxyServer.getInstance().getServerInfo(name);
    }

    /**
     * Gets the group assigned to this server
     * @return Returns the associated group, or a new group if none exists
     */
    public ServerGroup getGroup() {
        for(ServerGroup g : ServerConfig.getInstance().getGroups())
            if(g.getServers().contains(name)) return g;
        return new ServerGroup(name, "", friendlyName, Collections.singletonList(name), new ArrayList<>());
    }

    /**
     * Gets whether stealth mode is enabled
     * @return True if enabled, false if disabled
     */
    public boolean isStealthMode() {
        return stealthMode;
    }

    /**
     * Sets whether stealth mode should be enabled
     * @param stealthMode True if players should be hidden, false if not
     */
    public void setStealthMode(boolean stealthMode) {
        this.stealthMode = stealthMode;
    }
}
