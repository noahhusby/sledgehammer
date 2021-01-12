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

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.Addon;
import com.noahhusby.sledgehammer.addons.terramap.TerramapVersion.ReleaseType;
import com.noahhusby.sledgehammer.addons.terramap.commands.TerrashowCommand;
import com.noahhusby.sledgehammer.addons.terramap.network.ForgeChannel;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.P2CMapStylePacket;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.P2CSledgehammerHelloPacket;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync.C2PRegisterForUpdatePacket;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync.P2CPlayerSyncPacket;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync.P2CRegistrationExpiresPacket;
import com.noahhusby.sledgehammer.config.ConfigHandler;

import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.api.scheduler.ScheduledTask;

/**
 * Main Terramap addon class
 * 
 * @author SmylerMC
 *
 */
public class TerramapAddon extends Addon {
	
	public static TerramapAddon instance;
	
	public static final String TERRAMAP_MODID = "terramap";
	public static final String MAPSYNC_CHANNEL_NAME = "terramap:mapsync";
	public static final String SLEDGEHAMMER_CHANNEL_NAME = "terramap:sh"; // Forge does not support channel names longer than 20
	public static final TerramapVersion MINIMUM_COMPATIBLE_VERSION = new TerramapVersion(1, 0, 0, ReleaseType.BETA, 6, 0);
	
	public static final String PLAYER_SYNC_PERMISSION_NODE = "sledgehammer.terramap.playersync";
	public static final String TERRASHOW_BASE_PERMISSION_NODE = "sledgehammer.terramap.terrashow";
	public static final String TERRASHOW_SELF_PERMISSION_NODE = "sledgehammer.terramap.terrashow.self";
	public static final String TERRASHOW_OTHERS_PERMISSION_NODE = "sledgehammer.terramap.terrashow.other";
	
	public final ForgeChannel mapSyncChannel = new ForgeChannel(MAPSYNC_CHANNEL_NAME);
	public final ForgeChannel sledgehammerChannel = new ForgeChannel(SLEDGEHAMMER_CHANNEL_NAME);
	
	public final RemoteSynchronizer synchronizer = new RemoteSynchronizer();
	
	private Listener listener;
	private ScheduledTask syncTask;
	
	private UUID proxyUUID = new UUID(0, 0);

	
	public TerramapAddon() {
		instance = this;
	}

    @Override
    public void onEnable() {
    	this.mapSyncChannel.registerPacket(0, C2PRegisterForUpdatePacket.class);
    	this.mapSyncChannel.registerPacket(1, P2CPlayerSyncPacket.class);
    	this.mapSyncChannel.registerPacket(2, P2CRegistrationExpiresPacket.class);
    	this.sledgehammerChannel.registerPacket(0, P2CSledgehammerHelloPacket.class);
    	this.sledgehammerChannel.registerPacket(2, P2CMapStylePacket.class);
    	try {
    		this.proxyUUID = UUID.fromString(ConfigHandler.terramapProxyUUID);
    	} catch(IllegalArgumentException e) {
    		Sledgehammer.logger.warning("Failed to parse Terramap proxy uuid. Will be using 0.");
    	}
    	this.listener = new TerramapAddonEventHandler();
    	Sledgehammer.addListener(this.listener);
    	if(ConfigHandler.terramapSyncPlayers) {
    		this.syncTask = Sledgehammer.sledgehammer.getProxy().getScheduler().schedule(Sledgehammer.sledgehammer, this.synchronizer::syncPlayers, 0, ConfigHandler.terramapSyncInterval, TimeUnit.MILLISECONDS);
    	}
    	ProxyServer.getInstance().getPluginManager().registerCommand(Sledgehammer.sledgehammer, new TerrashowCommand());
    	Sledgehammer.logger.info("Enabled Terramap integration addon");
    }

    @Override
    public void onPluginMessage(PluginMessageEvent event) {
        if(event.getTag().equals(MAPSYNC_CHANNEL_NAME)) {
        	mapSyncChannel.process(event);
        } else if(event.getTag().equals(SLEDGEHAMMER_CHANNEL_NAME)) {
        	sledgehammerChannel.process(event);
        }
    }

    @Override
	public void onDisable() {
		this.mapSyncChannel.resetPacketRegistration();
		this.sledgehammerChannel.resetPacketRegistration();
		this.proxyUUID = new UUID(0, 0);
		this.synchronizer.unregisterAllPlayers();
		Sledgehammer.terminateListener(this.listener);
		if(this.syncTask != null) Sledgehammer.sledgehammer.getProxy().getScheduler().cancel(this.syncTask);
		// We don't need to unregister commands, Sledgehammer should be taking care of it already when the plugin gets enabled
		Sledgehammer.logger.info("Disabled Terramap integration add-on");
	}

	@Override
    public String[] getMessageChannels() {
        return new String[]{SLEDGEHAMMER_CHANNEL_NAME, MAPSYNC_CHANNEL_NAME};
    }
    
    public UUID getProxyUUID() {
    	return this.proxyUUID;
    }
    
}