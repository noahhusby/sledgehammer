/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - Attribute.java
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

package com.noahhusby.sledgehammer.players;

import com.google.common.collect.Maps;
import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.*;

public class Attribute {
    @Expose
    @SerializedName("UUID")
    private UUID uuid;
    @Expose
    @SerializedName("Attributes")
    private Map<String, Object> attributes;

    public Attribute() {
        this(UUID.randomUUID(), Maps.newHashMap());
    }

    public Attribute(UUID uuid, Map<String, Object> attributes) {
        this.uuid = uuid;
        this.attributes = attributes;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public UUID getUuid() {
        return uuid;
    }
}
