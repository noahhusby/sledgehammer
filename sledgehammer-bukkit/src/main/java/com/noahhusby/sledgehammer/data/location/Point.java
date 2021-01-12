/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - Point.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.data.location;

import org.json.simple.JSONObject;

public class Point {
    public final String x;
    public final String y;
    public final String z;
    public final String pitch;
    public final String yaw;

    public Point(String x, String y, String z, String pitch, String yaw) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
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
