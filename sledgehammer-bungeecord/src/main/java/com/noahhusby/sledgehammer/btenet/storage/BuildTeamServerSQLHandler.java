/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - BuildTeamSQLHandler.java
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

import com.noahhusby.lib.data.sql.ISQLDatabase;
import com.noahhusby.lib.data.sql.actions.Result;
import com.noahhusby.lib.data.sql.actions.Row;
import com.noahhusby.lib.data.sql.actions.Select;
import com.noahhusby.lib.data.storage.handlers.StorageHandler;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class BuildTeamServerSQLHandler implements StorageHandler {
    private final ScheduledExecutorService executor = Executors.newScheduledThreadPool(1);
    private ISQLDatabase database;

    private boolean initialized = false;
    private final String table;
    private int priority = 0;

    public BuildTeamServerSQLHandler(ISQLDatabase database, String table) {
        this.database = database;
        this.table = table;
        executor.scheduleAtFixedRate(this::onLoop, 0, 5, TimeUnit.SECONDS);
    }

    public void setDatabase(ISQLDatabase database) {
        this.database = database;
    }

    public ISQLDatabase getDatabase() {
        return database;
    }

    @Override
    public void save(JSONArray array, boolean keyed) { }

    @Override
    public JSONArray load(boolean keyed) {
        if(!initialized) return new JSONArray();
        Result result = database.select(new Select(table, "*", ""));
        JSONArray array = new JSONArray();

        for(Row r : result.getRows()) {
            JSONObject object = new JSONObject();
            object.put("ShortName", r.get("ShortName"));
            object.put("BuildTeam", r.get("BuildTeam"));
            array.add(object);
        }

        return array;
    }

    @Override
    public int getPriority() {
        return priority;
    }

    @Override
    public void setPriority(int priority) {
        this.priority = priority;
    }

    @Override
    public boolean isAvailable() {
        return database.isConnected();
    }

    private void onLoop() {
        if(!initialized) {
            if(!getDatabase().isConnected()) {
                getDatabase().connect();
                return;
            }

            initialized = true;
        }
    }
}
