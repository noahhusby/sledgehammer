/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - LocationRemovalScene.java
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

package com.noahhusby.sledgehammer.dialogs.scenes.setup;

import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.dialogs.components.location.LocationRemovalComponent;
import com.noahhusby.sledgehammer.dialogs.scenes.DialogScene;
import com.noahhusby.sledgehammer.datasets.Location;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.config.ServerInfo;

import java.util.List;

public class LocationRemovalScene extends DialogScene {

    private final ServerInfo server;
    private final DialogScene scene;

    public LocationRemovalScene(ServerInfo server, DialogScene scene) {
        this.server = server;
        this.scene = scene;
        registerComponent(new LocationRemovalComponent(server));
    }

    @Override
    public TextElement[] getTitle() {
        return new TextElement[]{new TextElement("Editing Locations - ", ChatColor.GRAY),
        new TextElement(server.getName(), ChatColor.RED)};
    }

    @Override
    public void onFinish() {
        List<Location> locations = ServerConfig.getInstance().getLocationsFromServer(server.getName());
        List<Location> newLocations = ServerConfig.getInstance().getLocationsFromServer(server.getName());
        Location l = locations.get(Integer.parseInt(getValue("locationremove").trim()));
        newLocations.remove(l);

        SledgehammerServer s = ServerConfig.getInstance().getServer(server.getName());
        s.setLocations(newLocations);

        ServerConfig.getInstance().pushServer(s);
        if(scene != null) {
            DialogHandler.getInstance().discardDialog(this);
            DialogHandler.getInstance().startDialog(getCommandSender(), scene);
            return;
        }

        String x = "";
        if(!l.city.equals("")) x+= ChatHelper.capitalize(l.city)+", ";
        if(!l.county.equals("")) x+= ChatHelper.capitalize(l.county)+", ";
        if(!l.state.equals("")) x+= ChatHelper.capitalize(l.state)+", ";
        if(!l.country.equals("")) x+= ChatHelper.capitalize(l.country);
        sender.sendMessage(ChatHelper.makeTextComponent(
                new TextElement("Successfully removed location: ", ChatColor.GRAY),
                new TextElement(ChatHelper.capitalize(l.detailType.name())+" - ", ChatColor.RED),
                new TextElement(x, ChatColor.GOLD)));
    }

    @Override
    public boolean isAdmin() {
        return true;
    }
}
