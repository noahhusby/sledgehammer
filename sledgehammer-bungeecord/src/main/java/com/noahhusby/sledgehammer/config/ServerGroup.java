/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - ServerGroup.java
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
import com.noahhusby.sledgehammer.SmartObject;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ServerGroup {

    @Expose
    @SerializedName("Id")
    private String ID;
    @Expose
    @SerializedName("HeadId")
    private String headID;
    @Expose
    @SerializedName("Name")
    private String name;
    @Expose
    @SerializedName("Servers")
    private List<String> servers;
    @Expose
    @SerializedName("Aliases")
    private List<String> aliases;

    public ServerGroup() {
        this("", "", "", new ArrayList<>(), new ArrayList<>());
    }

    public ServerGroup(String ID, String headID, String name, List<String> servers, List<String> aliases) {
        this.ID = ID;
        this.headID = headID;
        this.name = name;
        this.servers = servers;
        this.aliases = aliases;
    }

    /**
     * Gets the group's head ID
     * @return Head ID
     */
    public String getHeadID() {
        return headID;
    }

    /**
     * Gets the group's ID
     * @return Group's ID
     */
    public String getID() {
        return ID;
    }

    /**
     * Gets the group's name
     * @return Group's name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the group's head ID
     * @param headID Head ID
     */
    public void setHeadID(String headID) {
        this.headID = headID;
    }

    /**
     * Sets the group's ID
     * @param ID Group's ID
     */
    public void setID(String ID) {
        this.ID = ID;
    }

    /**
     * Sets the group's name
     * @param name Group's name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets the list of servers associated with the group
     * @return
     */
    public List<String> getServers() {
        return servers;
    }

    /**
     * Gets the list of alias tags associated with the group
     * @return
     */
    public List<String> getAliases() {
        return aliases;
    }
}
