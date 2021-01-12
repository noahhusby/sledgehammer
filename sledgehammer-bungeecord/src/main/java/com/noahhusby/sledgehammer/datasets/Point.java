/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Point.java
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

import com.google.gson.annotations.Expose;
import org.json.simple.JSONObject;

public class Point {
    @Expose
    public final String x;
    @Expose
    public final String y;
    @Expose
    public final String z;
    @Expose
    public final String yaw;
    @Expose
    public final String pitch;

    public Point() {
        this("", "", "", "", "");
    }

    public Point(String x, String y, String z, String yaw, String pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public JSONObject getJSON() {
        JSONObject o = new JSONObject();

        o.put("x", x);
        o.put("y", y);
        o.put("z", z);
        o.put("pitch", pitch);
        o.put("yaw", yaw);

        return o;
    }
}
