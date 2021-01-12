/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Warp.java
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

package com.noahhusby.sledgehammer.data.warp;

import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.SmartObject;
import org.json.simple.JSONObject;

public class Warp {

    private PinnedMode pinned;
    private String server;
    private String headID;
    private String name;
    private int id;

    public Warp() {
        this(-1, "", "", PinnedMode.NONE, "");
    }

    public Warp(int id, String name, String server, PinnedMode pinned, String headID) {
        this.id = id;
        this.name = name;
        this.server = server;
        this.pinned = pinned;
        this.headID = headID;
    }

    /**
     * Sets the ID of the warp
     * @param id Warp ID
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Gets the ID of the warp
     * @return Warp ID
     */
    public int getId() {
        return id;
    }

    /**
     * Sets name of the warp
     * @param name Warp Name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Gets name of the warp
     * @return Warp Name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the server for the warp
     * @param server Warp Server
     */
    public void setServer(String server) {
        this.server = server;
    }

    /**
     * Gets the server for the warp
     * @return Warp Server
     */
    public String getServer() {
        return server;
    }

    /**
     * Sets whether the warp should be pinned or not
     * @param pinned True for pinned, false for not
     */
    public void setPinnedMode(PinnedMode pinned) {
        this.pinned = pinned;
    }

    /**
     * Gets whether the warp is pinned
     * @return True if pinned, false if not
     */
    public PinnedMode getPinnedMode() {
        return pinned;
    }

    /**
     * Sets the Head ID for the warp
     * @param headID Warp Head ID
     */
    public void setHeadID(String headID) {
        this.headID = headID;
    }

    /**
     * Gets the Head ID for the warp
     * @return Warp Head ID
     */
    public String getHeadID() {
        return headID;
    }

    public static Warp fromWaypoint(JSONObject object) {
        SmartObject waypoint = SmartObject.fromJSON(object);
        return new Warp(SledgehammerUtil.JsonUtils.toInt(waypoint.get("Id")), waypoint.getString("Name"),
                waypoint.getString("Server"), PinnedMode.valueOf(waypoint.getString("Pinned")),
                waypoint.getString("HeadId"));
    }

    public JSONObject toJson() {
        JSONObject data = new JSONObject();
        data.put("id", id);
        data.put("name", name);
        data.put("headId", headID);
        data.put("pinned", pinned.name());
        return data;
    }

    public enum PinnedMode {
        NONE, LOCAL, GLOBAL
    }
}
