/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - BuildTeam.java
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

package com.noahhusby.sledgehammer.btenet.storage;

import com.noahhusby.lib.data.storage.Storable;
import org.json.simple.JSONObject;

import java.util.List;

public class BuildTeamServer implements Storable {
    private String shortName;
    private String buildTeam;

    public BuildTeamServer() {}

    public void setShortName(String name) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public void setBuildTeam(String buildTeam) {
        this.buildTeam = buildTeam;
    }

    public String getBuildTeam() {
        return buildTeam;
    }

    @Override
    public Storable load(JSONObject data) {
        BuildTeamServer bt = new BuildTeamServer();
        bt.buildTeam = (String) data.get("BuildTeam");
        bt.shortName = (String) data.get("ShortName");
        return bt;
    }

    @Override
    public JSONObject save(JSONObject data) {
        data.put("BuildTeam", buildTeam);
        data.put("ShortName", shortName);
        return data;
    }
}
