/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - BTENet.java
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

import com.noahhusby.lib.data.sql.Credentials;
import com.noahhusby.lib.data.sql.ISQLDatabase;
import com.noahhusby.lib.data.sql.MySQL;
import com.noahhusby.lib.data.storage.StorageList;
import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.btenet.storage.BuildTeam;
import com.noahhusby.sledgehammer.btenet.storage.BuildTeamSQLHandler;
import com.noahhusby.sledgehammer.btenet.storage.BuildTeamServer;
import com.noahhusby.sledgehammer.btenet.storage.BuildTeamServerSQLHandler;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.warp.Warp;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class BTENet {
    private static BTENet instance = null;

    public static BTENet getInstance() {
        return instance == null ? instance = new BTENet() : instance;
    }

    private BTENet(){}

    private StorageList<BuildTeam> buildTeams = new StorageList<>(BuildTeam.class);
    private StorageList<BuildTeamServer> buildTeamServers = new StorageList<>(BuildTeamServer.class);

    public void onEnable() {
        BTEConfig.getInstance().init(Sledgehammer.sledgehammer.getDataFolder());

        ISQLDatabase buildTeamDatabase = new MySQL(new Credentials(BTEConfig.sqlHost,
                BTEConfig.sqlPort, BTEConfig.sqlUser, BTEConfig.sqlPassword, BTEConfig.sqlDb));

        buildTeams.registerHandler(new BuildTeamSQLHandler(buildTeamDatabase, "BuildTeams"));
        buildTeams.setAutoLoad(30, TimeUnit.SECONDS);

        buildTeamServers.registerHandler(new BuildTeamServerSQLHandler(buildTeamDatabase, "BuildTeamServers"));
        buildTeamServers.setAutoLoad(30, TimeUnit.SECONDS);

        buildTeams.load(true);
        buildTeamServers.load(true);

        Executors.newSingleThreadScheduledExecutor().schedule(() -> {
            System.out.println("Testing");
            for(BuildTeam bt : buildTeams)
                System.out.println(bt.save(new JSONObject()).toJSONString());
        }, 12, TimeUnit.SECONDS);
    }

    public void onDisable() {

    }

    public List<BuildTeamServer> getBuildTeamServers() {
        return buildTeamServers;
    }

    public JSONObject generateGUIPayload() {
        JSONObject o = new JSONObject();
        JSONArray waypoints = new JSONArray();
        for(Warp w : WarpHandler.getInstance().getWarps()) {
            JSONObject wa = new JSONObject();
            BuildTeam bt = getBuildTeamFromServer(w.server);
            if(bt == null) continue;

            wa.put("name", w.name);
            wa.put("pinned", w.pinned);
            wa.put("server", bt.getName());
            wa.put("head", bt.getHeadID());
            waypoints.add(wa);
        }

        o.put("web", ConfigHandler.mapEnabled);
        o.put("waypoints", waypoints);

        return o;
    }

    private BuildTeam getBuildTeamFromServer(String server) {
        for(BuildTeam bt : buildTeams)
            for(BuildTeamServer bts : bt.getServers())
                if(bts.getShortName().equals(server)) return bt;

        return null;
    }
}
