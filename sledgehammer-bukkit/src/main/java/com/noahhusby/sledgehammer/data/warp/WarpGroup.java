package com.noahhusby.sledgehammer.data.warp;

import com.noahhusby.sledgehammer.SmartObject;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class WarpGroup {
    private String name;
    private String id;
    private String headId;
    private List<Warp> warps;

    public WarpGroup(String name, String id, String headId, List<Warp> warps) {
        this.name = name;
        this.id = id;
        this.headId = headId;
        this.warps = warps;
    }

    public String getName() {
        return name;
    }

    public String getHeadId() {
        return headId;
    }

    public String getId() {
        return id;
    }

    public List<Warp> getWarps() {
        return warps;
    }

    public static WarpGroup fromJson(JSONObject object) {
        SmartObject group = SmartObject.fromJSON(object);

        JSONArray warpArray = (JSONArray) group.get("warps");
        List<Warp> warpList = new ArrayList<>();
        for(Object o : warpArray) {
            JSONObject waypoint = (JSONObject) o;
            warpList.add(Warp.fromWaypoint(waypoint));
        }

        return new WarpGroup(group.getString("name"), group.getString("id"), group.getString("headId"), warpList);
    }
}
