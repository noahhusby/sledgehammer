/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - GameMode.java
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

public enum GameMode {

    NONE(-1, ""),

    CREATIVE(1, "creative"),

    SURVIVAL(0, "survival"),

    ADVENTURE(2, "adventure"),

    SPECTATOR(3, "spectator");
	
    private final int value;
    private final String name;
    
    GameMode(final int value, final String name) {
        this.value = value;
        this.name = name;
    }
    
    @Override
	public String toString() {
    	return this.name;
    }
    
    public int getId() {
    	return this.value;
    }
    
}
