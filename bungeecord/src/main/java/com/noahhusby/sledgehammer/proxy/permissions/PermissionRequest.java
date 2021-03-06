/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - PermissionRequest.java
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

package com.noahhusby.sledgehammer.proxy.permissions;

import java.util.function.BiConsumer;

public class PermissionRequest {
    public final BiConsumer<PermissionCode, Boolean> response;
    public final String salt;
    public final long time;
    public final int timeout;

    public PermissionRequest(BiConsumer<PermissionCode, Boolean> response, String salt, long time, int timeout) {
        this.response = response;
        this.salt = salt;
        this.time = time;
        this.timeout = timeout;
    }

    public enum PermissionCode {
        PERMISSION, NO_PERMISSION, TIMED_OUT
    }
}
