package com.noahhusby.sledgehammer.addons.terramap.network.packets;

import java.util.UUID;

import com.noahhusby.sledgehammer.addons.terramap.network.ForgeChannel;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync.PlayerSyncStatus;
import com.noahhusby.sledgehammer.config.ConfigHandler;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

/**
 * Sent to clients joining the network to inform them of this proxy's specific settings, such as:
 *  <ul>
 *  <li>The Sledgehammer version</li>
 *  <li>Whether or not players are being synchronized. See {@link ConfigHandler#terramapSyncPlayers}</li>
 *  <li>Whether or not to enable the map on all worlds and not just on Terra121's. See {@link ConfigHandler#terramapGlobalMap}</li>
 *  <li>Whether or not to save settings per world or for the whole network. See See {@link ConfigHandler#terramapGlobalSettings}</li>
 *  <li>Whether or not warps are supported.</li>
 *  <li>The proxy UUID. See {@link ConfigHandler#terramapProxyUUID}.</li>
 *  </ul>
 * @author SmylerMC
 *
 */
public class P2CSledgehammerHelloPacket implements IForgePacket {
	
	public String version = "";
	public PlayerSyncStatus syncPlayers = PlayerSyncStatus.DISABLED;
	public PlayerSyncStatus syncSpectators = PlayerSyncStatus.DISABLED;
	public boolean globalmap = true; // If true, the Terramap allows users to open the map on non-terra worlds
	public boolean globalSettings = false; // Should settings and preferences be saved for the whole network (true) or per server (false)
	public boolean hasWarpSupport = false; // Do this server have warp support
	public UUID proxyUUID = new UUID(0, 0);
	
	public P2CSledgehammerHelloPacket(String version, PlayerSyncStatus syncPlayers, PlayerSyncStatus syncSpectators, boolean globalMap, boolean globalSettings, boolean hasWarpSupport, UUID proxyUUID) {
		this.version = version;
		this.syncPlayers = syncPlayers;
		this.syncSpectators = syncSpectators;
		this.globalmap = globalMap;
		this.globalSettings = globalSettings;
		this.hasWarpSupport = hasWarpSupport;
		this.proxyUUID = proxyUUID;
	}
	
	public P2CSledgehammerHelloPacket() {}

	@Override
	public void encode(ByteBuf buf) {
		ForgeChannel.writeStringToBuf(this.version, buf);
		buf.writeByte(this.syncPlayers.VALUE);
		buf.writeByte(this.syncSpectators.VALUE);
		buf.writeBoolean(this.globalmap);
		buf.writeBoolean(this.globalSettings);
		buf.writeBoolean(this.hasWarpSupport);
		buf.writeLong(this.proxyUUID.getLeastSignificantBits());
		buf.writeLong(this.proxyUUID.getMostSignificantBits());
	}

	@Override
	public void decode(ByteBuf buf) {
		this.version = ForgeChannel.readStringFromBuf(buf);
		this.syncPlayers = PlayerSyncStatus.getFromNetworkCode(buf.readByte());
		this.syncSpectators = PlayerSyncStatus.getFromNetworkCode(buf.readByte());
		this.globalmap = buf.readBoolean();
		this.globalSettings = buf.readBoolean();
		this.hasWarpSupport = buf.readBoolean();
		long leastUUID = buf.readLong();
		long mostUUID = buf.readLong();
		this.proxyUUID = new UUID(mostUUID, leastUUID);
	}

	@Override
	public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
		return false; // We should never receive that, only send
	}

	@Override
	public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
		return false; // We should never receive that, only send
	}

}
