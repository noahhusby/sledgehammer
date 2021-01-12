package com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync;

import com.noahhusby.sledgehammer.addons.terramap.RemoteSynchronizer;
import com.noahhusby.sledgehammer.addons.terramap.TerramapAddon;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.IForgePacket;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.players.PlayerManager;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

/**
 * Packet received from players that wish to start or stop receiving player position updates.
 * 
 * @see com.noahhusby.sledgehammer.addons.terramap.RemoteSynchronizer
 * @author SmylerMC
 *
 */
public class C2PRegisterForUpdatePacket implements IForgePacket {
	
	public boolean register;

	@Override
	public void encode(ByteBuf buf) {
		buf.writeBoolean(this.register);
	}

	@Override
	public void decode(ByteBuf stream) {
		this.register = stream.readBoolean();
	}

	@Override
	public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
		// Should never receive this from a server
		return ConfigHandler.terramapSyncPlayers;
	}

	@Override
	public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
		if(!ConfigHandler.terramapSyncPlayers) return false;
		SledgehammerPlayer player = PlayerManager.getInstance().getPlayer(fromPlayer.getName());
		if(this.register) {
			if(RemoteSynchronizer.hasSyncPermission(player)) TerramapAddon.instance.synchronizer.registerPlayer(player);
		} else {
			TerramapAddon.instance.synchronizer.unregisterPlayer(player);
		}
		return true; // Do not send to server
	}

}
