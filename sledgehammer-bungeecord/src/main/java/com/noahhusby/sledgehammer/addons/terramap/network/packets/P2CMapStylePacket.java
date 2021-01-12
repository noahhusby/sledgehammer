package com.noahhusby.sledgehammer.addons.terramap.network.packets;

import java.util.HashMap;
import java.util.Map;

import com.noahhusby.sledgehammer.addons.terramap.network.ForgeChannel;

import io.netty.buffer.ByteBuf;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;

/**
 * Sent to players joining the network to give them access to this proxy's custom map styles.
 * 
 * @see com.noahhusby.sledgehammer.addons.terramap.MapStyleRegistry
 * @author SmylerMC
 *
 */
public class P2CMapStylePacket implements IForgePacket {

	public String id;
	public long providerVersion;
	public String urlPattern;
	public Map<String, String> names;
	public Map<String, String> copyrights;
	public int minZoom;
	public int maxZoom;
	public int displayPriority;
	public boolean isAllowedOnMinimap;
	public String comment;
	
	public P2CMapStylePacket() {}

	public P2CMapStylePacket(
			String id,
			long providerVersion,
			String urlPattern,
			Map<String, String> names,
			Map<String, String> copyrights,
			int minZoom,
			int maxZoom,
			int displayPriority,
			boolean isAllowedOnMinimap,
			String comment) {
		this.id = id;
		this.providerVersion = providerVersion;
		this.urlPattern = urlPattern;
		this.names = names;
		this.copyrights = copyrights;
		this.minZoom = minZoom;
		this.maxZoom = maxZoom;
		this.displayPriority = displayPriority;
		this.isAllowedOnMinimap = isAllowedOnMinimap;
		this.comment = comment;
	}



	@Override
	public void decode(ByteBuf buf) {
		this.id = ForgeChannel.readStringFromBuf(buf);
		this.providerVersion = buf.readLong();
		this.urlPattern = ForgeChannel.readStringFromBuf(buf);
		int nameCount = buf.readInt();
		Map<String, String> names = new HashMap<String, String>();
		for(int i=0; i < nameCount; i++) {
			String key = ForgeChannel.readStringFromBuf(buf);
			String name = ForgeChannel.readStringFromBuf(buf);
			names.put(key, name);
		}
		this.names = names;
		int copyrightCount = buf.readInt();
		Map<String, String> copyrights = new HashMap<String, String>();
		for(int i=0; i < copyrightCount; i++) {
			String key = ForgeChannel.readStringFromBuf(buf);
			String copyright = ForgeChannel.readStringFromBuf(buf);
			copyrights.put(key, copyright);
		}
		this.copyrights = copyrights;
		this.minZoom = buf.readInt();
		this.maxZoom = buf.readInt();
		this.displayPriority = buf.readInt();
		this.isAllowedOnMinimap = buf.readBoolean();
		this.comment = ForgeChannel.readStringFromBuf(buf);
	}

	@Override
	public void encode(ByteBuf buf) {
		ForgeChannel.writeStringToBuf(this.id, buf);
		buf.writeLong(this.providerVersion);
		ForgeChannel.writeStringToBuf(this.urlPattern, buf);
		buf.writeInt(this.names.size());
		for(String key: this.names.keySet()) {
			ForgeChannel.writeStringToBuf(key, buf);
			ForgeChannel.writeStringToBuf(this.names.get(key), buf);
		}
		buf.writeInt(this.copyrights.size());
		for(String key: this.copyrights.keySet()) {
			ForgeChannel.writeStringToBuf(key, buf);
			ForgeChannel.writeStringToBuf(this.copyrights.get(key), buf);
		}
		buf.writeInt(this.minZoom);
		buf.writeInt(this.maxZoom);
		buf.writeInt(this.displayPriority);
		buf.writeBoolean(this.isAllowedOnMinimap);
		ForgeChannel.writeStringToBuf(this.comment, buf);
	}

	@Override
	public boolean processFromServer(String channel, Server fromServer, ProxiedPlayer toPlayer) {
		return false;
	}

	@Override
	public boolean processFromClient(String channel, ProxiedPlayer fromPlayer, Server toServer) {
		return false;
	}

}
