package com.noahhusby.sledgehammer.data.warp;

import com.noahhusby.sledgehammer.SmartObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WarpConfigPayload {
    private List<WarpGroup> groups;
    private String requestGroup;
    private String salt;
    private boolean admin;
    private boolean local;
    private JSONObject data;

    public WarpConfigPayload(String requestGroup, String salt, boolean local, boolean admin, List<WarpGroup> groups, JSONObject data) {
        this.groups = groups;
        this.requestGroup = requestGroup;
        this.salt = salt;
        this.admin = admin;
        this.local = local;
        this.data = data;
    }

    public String getRequestGroup() {
        return requestGroup;
    }

    public boolean isAdmin() {
        return admin;
    }

    public boolean isLocal() {
        return local;
    }

    public List<WarpGroup> getGroups() {
        return groups;
    }

    public String getSalt() {
        return salt;
    }

    public JSONObject getData() {
        return data;
    }

    public static WarpConfigPayload fromPayload(SmartObject object) {
        JSONArray groups = (JSONArray) object.get("groups");
        List<WarpGroup> warpGroups = new ArrayList<>();
        for(Object o : groups)
            warpGroups.add(WarpGroup.fromJson((JSONObject) o));

        return new WarpConfigPayload(object.getString("requestGroup"),
                object.getString("salt"), object.getBoolean("local"),
                object.getBoolean("admin"), warpGroups, (JSONObject) object.get("data"));
    }
}
