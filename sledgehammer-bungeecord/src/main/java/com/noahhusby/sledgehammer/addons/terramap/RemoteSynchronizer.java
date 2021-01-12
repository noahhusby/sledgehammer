package com.noahhusby.sledgehammer.addons.terramap;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync.P2CPlayerSyncPacket;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.mapsync.P2CRegistrationExpiresPacket;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.players.PlayerManager;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;

import net.md_5.bungee.api.connection.ProxiedPlayer;

/**
 * Handles sending player position updates to registered players
 * @author SmylerMC
 *
 */
public class RemoteSynchronizer {

	private Map<UUID, RegisteredForUpdatePlayer> playersToUpdate = new HashMap<UUID, RegisteredForUpdatePlayer>();

	/**
	 * Sends position updates to registered players
	 */
	public void syncPlayers() {
		synchronized(this.playersToUpdate) {
			if(this.playersToUpdate.size() <= 0) return;
			long ctime = System.currentTimeMillis();
			List<SledgehammerPlayer> playersToSend = new ArrayList<SledgehammerPlayer>();
			List<SledgehammerPlayer> players = PlayerManager.getInstance().getPlayers();
			for(int i=0; i< players.size(); i++) {
				SledgehammerPlayer player = players.get(i);
				if(!player.onEarthServer()) continue;
				if(!PlayerDisplayPreferences.shouldDisplayPlayer(player)) continue;
				if(player.getLocation() == null) continue; // New players can have this set to null
				playersToSend.add(player);
			}
			SledgehammerPlayer[] players2send2 = new SledgehammerPlayer[this.playersToUpdate.size()];
			int i = 0;
			for(RegisteredForUpdatePlayer player: this.playersToUpdate.values()) {
				players2send2[i++] = player.player;
			}
			P2CPlayerSyncPacket pkt = new P2CPlayerSyncPacket(playersToSend.toArray(new SledgehammerPlayer[playersToSend.size()]));
			TerramapAddon.instance.mapSyncChannel.send(pkt, players2send2);
			for(RegisteredForUpdatePlayer player: this.playersToUpdate.values()) {
				if(ctime - player.lastRegisterTime > ConfigHandler.terramapSyncTimeout - 10000 && !player.noticeSent) {
					Sledgehammer.logger.fine("Sending registration expires notice to " + player.player.getName());
					TerramapAddon.instance.mapSyncChannel.send(new P2CRegistrationExpiresPacket(), player.player);
					player.noticeSent = true;
				}
			}
			for(RegisteredForUpdatePlayer player: this.playersToUpdate.values()) {
				if(ctime - player.lastRegisterTime > ConfigHandler.terramapSyncTimeout) {
					Sledgehammer.logger.fine("Unregistering " + player.player.getName() + " from map update as it did not renew its registration");
					this.playersToUpdate.remove(player.player.getUniqueId());
					TerramapAddon.instance.mapSyncChannel.send(new P2CRegistrationExpiresPacket(), player.player);
				}
			}
		}
	}

	/**
	 * Registers a players so it gets position updates
	 * @param player - The player
	 */
	public void registerPlayer(SledgehammerPlayer player) {
		Sledgehammer.logger.fine("Registering player for map updates: " + player.getName());
		synchronized(this.playersToUpdate) {
			TerramapAddon.instance.synchronizer.playersToUpdate.put(player.getUniqueId(), new RegisteredForUpdatePlayer(player, System.currentTimeMillis()));
		}
	}

	/**
	 * Unregisters a player so it stops getting position updates
	 * @param player - The player
	 */
	public void unregisterPlayer(ProxiedPlayer player) {
		Sledgehammer.logger.fine("Unregistering player for map updates: " + player.getName());
		synchronized(this.playersToUpdate) {
			TerramapAddon.instance.synchronizer.playersToUpdate.remove(player.getUniqueId());
		}
	}

	/**
	 * Unregisters all players so they stop getting map updates
	 */
	public void unregisterAllPlayers() {
		synchronized(this.playersToUpdate) {
			this.playersToUpdate.clear();
		}
	}
	
	public static boolean hasSyncPermission(ProxiedPlayer player) {
		return player.hasPermission(TerramapAddon.PLAYER_SYNC_PERMISSION_NODE) || PermissionHandler.getInstance().isAdmin(player);
	}

	private static class RegisteredForUpdatePlayer {

		public SledgehammerPlayer player;
		public long lastRegisterTime;
		boolean noticeSent = false;

		public RegisteredForUpdatePlayer(SledgehammerPlayer player, long time) {
			this.player = player;
			this.lastRegisterTime = time;
		}

	}

}
