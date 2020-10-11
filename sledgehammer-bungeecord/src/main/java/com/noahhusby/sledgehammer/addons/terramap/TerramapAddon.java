/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - TerramapAddon.java
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

package com.noahhusby.sledgehammer.addons.terramap;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.Addon;
import com.noahhusby.sledgehammer.addons.terramap.packets.C2PRegisterForUpdatePacket;
import com.noahhusby.sledgehammer.addons.terramap.packets.P2CSledgehammerHelloPacket;

import net.md_5.bungee.api.event.PluginMessageEvent;

public class TerramapAddon extends Addon {
	
	public static TerramapAddon instance;
	
	public static final String MAPSYNC_CHANNEL_NAME = "terramap:mapsync";
	public static final String SLEDGEHAMMER_CHANNEL_NAME = "terramap:sh"; // Forge does not support channel names longer than 20
	
	public final ForgeChannel mapSyncChannel = new ForgeChannel(MAPSYNC_CHANNEL_NAME);
	public final ForgeChannel sledgehammerChannel = new ForgeChannel(SLEDGEHAMMER_CHANNEL_NAME);
	
	public TerramapAddon() {
		instance = this;
	}

    @Override
    public void onEnable() {
    	this.mapSyncChannel.registerPacket(0, C2PRegisterForUpdatePacket.class);
    	this.sledgehammerChannel.registerPacket(0, P2CSledgehammerHelloPacket.class);
    	Sledgehammer.setupListener(new EventHandler());
    	Sledgehammer.logger.info("Enabled Terramap integration addon");
    }

    @Override
    public void onPluginMessage(PluginMessageEvent event) {
    	//FIXME called twice for each packet
        if(event.getTag().equals(MAPSYNC_CHANNEL_NAME)) {
        	mapSyncChannel.process(event);
        } else if(event.getTag().equals(SLEDGEHAMMER_CHANNEL_NAME)) {
        	sledgehammerChannel.process(event);
        }
    }

    @Override
    public String[] getMessageChannels() {
        return new String[]{SLEDGEHAMMER_CHANNEL_NAME, MAPSYNC_CHANNEL_NAME};
    }
}
