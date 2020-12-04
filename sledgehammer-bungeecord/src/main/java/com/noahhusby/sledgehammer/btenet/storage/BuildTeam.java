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
import com.noahhusby.sledgehammer.btenet.BTENet;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class BuildTeam implements Storable {
    private String ID;
    private String name;
    private String owners;
    private String headID;
    private List<BuildTeamServer> servers;

    public BuildTeam() {}

    public void setID(String ID) {
        this.ID = ID;
    }

    public String getID() {
        return ID;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setOwners(String owners) {
        this.owners = owners;
    }

    public String getOwners() {
        return owners;
    }

    public void setHeadID(String ID) {
        this.headID = ID;
    }

    public String getHeadID() {
        return headID;
    }

    public List<BuildTeamServer> getServers() {
        return servers;
    }

    @Override
    public Storable load(JSONObject data) {
        BuildTeam bt = new BuildTeam();
        bt.headID = (String) data.get("headid");
        bt.owners = (String) data.get("owners");
        bt.name = (String) data.get("name");
        bt.ID = (String) data.get("id");

        List<BuildTeamServer> servers = new ArrayList<>();
        for(BuildTeamServer server : BTENet.getInstance().getBuildTeamServers())
            if(server.getBuildTeam().equalsIgnoreCase(bt.ID)) servers.add(server);

        bt.servers = servers;
        return bt;
    }

    @Override
    public JSONObject save(JSONObject data) {
        data.put("id", ID);
        data.put("name", name);
        data.put("owners", owners);
        data.put("headid", headID);
        return data;
    }
}
