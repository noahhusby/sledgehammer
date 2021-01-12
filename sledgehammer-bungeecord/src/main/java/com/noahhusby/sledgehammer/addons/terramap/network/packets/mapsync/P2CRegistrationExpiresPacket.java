package com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync;

import com.noahhusby.sledgehammer.addons.terramap.network.packets.IForgePacket;
import com.noahhusby.sledgehammer.config.ConfigHandler;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

/**
 * Sent to players some time after they registered for updates with a {@link C2PRegisterForUpdatePacket},
 * in order to inform them they need to register if they wish to keep getting {@link P2CPlayerSyncPacket}s.
 * The exact expiration time can be configured in {@link ConfigHandler#terramapSyncTimeout}.
 * 
 * @see com.noahhusby.sledgehammer.addons.terramap.RemoteSynchronizer
 * @author SmylerMC
 *
 */
public class P2CRegistrationExpiresPacket implements IForgePacket {

	public P2CRegistrationExpiresPacket() {}

	@Override
	public void encode(ByteBuf buf) {}

	@Override
	public void decode(ByteBuf buf) {}

	@Override
	public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
		// Never received here
		return ConfigHandler.terramapSyncPlayers;
	}

	@Override
	public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
		// Never received here
		return ConfigHandler.terramapSyncPlayers;
	}
	
}
