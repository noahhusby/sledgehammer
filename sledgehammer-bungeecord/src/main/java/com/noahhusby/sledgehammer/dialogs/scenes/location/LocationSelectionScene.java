/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationSelectionScene.java
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

package com.noahhusby.sledgehammer.dialogs.scenes.location;

import com.noahhusby.sledgehammer.dialogs.components.location.LocationSelectionComponent;
import com.noahhusby.sledgehammer.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.network.P2S.P2SWarpGUIPacket;
import net.md_5.bungee.api.config.ServerInfo;

public class LocationSelectionScene extends DialogScene {

    private final ServerInfo server;
    private final DialogScene scene;

    public LocationSelectionScene(ServerInfo server) {
        this(server, null);
    }

    public LocationSelectionScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new LocationSelectionComponent());
    }

    @Override
    public void onFinish() {
        switch (getValue("locationmode").toLowerCase().trim()) {
            case "city":
                DialogHandler.getInstance().startDialog(getCommandSender(), new CityScene(server, scene));
                break;
            case "county":
                DialogHandler.getInstance().startDialog(getCommandSender(), new CountyScene(server, scene));
                break;
            case "state":
                DialogHandler.getInstance().startDialog(getCommandSender(), new StateScene(server, scene));
                break;
            case "country":
                DialogHandler.getInstance().startDialog(getCommandSender(), new CountryScene(server, scene));
                break;
        }
    }



    @Override
    public boolean isAdmin() {
        return true;
    }
}
