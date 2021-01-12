package com.noahhusby.sledgehammer.addons.terramap.network;

import java.util.HashMap;
import java.util.Map;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.addons.terramap.network.packets.IForgePacket;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.protocol.DefinedPacket;

/**
 * Implements a Forge plugin channel. Encodes, decodes, receives and dispatches {@link IForgePacket}.
 * Takes care of adding discrimators at the beginning of packets.
 * 
 * @author SmylerMC
 *
 */
public class ForgeChannel {

	private final String channelName;
	private final Map<Integer, Class<? extends IForgePacket>> packetMap = new HashMap<Integer, Class<? extends IForgePacket>>();
	private final Map<Class<? extends IForgePacket>, Integer> discriminatorMap = new HashMap<Class<? extends IForgePacket>, Integer>();
	
	public ForgeChannel(String channelName) {
		this.channelName = channelName;
	}

	/**
	 * Processes the {@link PluginMessageEvent}: decodes the forge packet using the appropriate registered {@link IForgePacket}, and calls it corresponding handling method.
	 * 
	 * @param event - Event to process
	 */
	public void process(PluginMessageEvent event) {
		if(!event.getTag().equals(this.channelName)) {
			Sledgehammer.logger.warning("Asked to process a channel from channel: " + event.getTag() + " in " + this.channelName + " channel handler!");
			return;
		}
		try {
			ByteBuf stream = Unpooled.copiedBuffer(event.getData());
			int discriminator = stream.readByte();
			ProxiedPlayer player;
			Server server;
			boolean player2server = true;
			if(event.getSender() instanceof ProxiedPlayer && event.getReceiver() instanceof Server) {
				player = (ProxiedPlayer)event.getSender();
				server = (Server)event.getReceiver();
				player2server = true;
			} else if(event.getSender() instanceof Server && event.getReceiver() instanceof ProxiedPlayer) {
				player = (ProxiedPlayer)event.getReceiver();
				server = (Server)event.getSender();
				player2server = false;
			} else {
				Sledgehammer.logger.warning(
						"Got an unknow combination of sender/receiver in Forge channel " + this.channelName + " channel. " +
						"Sender " + event.getSender().getClass() +
						", Receiver: " + event.getReceiver().getClass() +
						", Packet discriminator " + discriminator);
				return;
			}
			Class<? extends IForgePacket> clazz = packetMap.get(discriminator);
			if(clazz == null) {
				if(player2server) {
					throw new PacketEncodingException("Received an unregistered packet from player" + player.getName() + "/" + player.getUniqueId() + "/" + player.getSocketAddress() + " for server" + server.getInfo().getName() + "! Discriminator: " + discriminator);
				} else if(event.getSender() instanceof Server && event.getReceiver() instanceof ProxiedPlayer) {
					throw new PacketEncodingException("Received an unregistered packet from server " + server.getInfo().getName() + " for player " + player.getName() + "/" + player.getUniqueId() + "/" + player.getSocketAddress() + "! Discriminator: " + discriminator);
				}
			}
			IForgePacket packetHandler = clazz.newInstance();
			packetHandler.decode(stream);
			boolean cancel = false;
			if(player2server) {
				cancel = packetHandler.processFromClient(this.channelName, player, server);
			} else {
				cancel = packetHandler.processFromServer(this.channelName, server, player);
			}
			if(cancel) event.setCancelled(cancel);
		} catch(Exception e) {
			Sledgehammer.logger.warning("Failed to process a Forge packet!");
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the given packet to the given player
	 * @param pkt - Packet to send
	 * @param to - Player to send the packet to
	 */
	public void send(IForgePacket pkt, ProxiedPlayer to) {
		try {
			to.sendData(this.channelName, this.encode(pkt));
		} catch(Exception e) {
			Sledgehammer.logger.warning("Failed to send a Forge packet to player " + to.getName() + "/" + to.getUniqueId() + " in channel " + this.channelName + " : " + e);
		}
	}
	
	/**
	 * Sends the given packet to the given players
	 * @param pkt - Packet to send
	 * @param to - Players to send the packet to
	 */
	public void send(IForgePacket pkt, ProxiedPlayer... to) {
		int sent = 0;
		try {
			byte[] data = this.encode(pkt);
			for(ProxiedPlayer player: to) {
				player.sendData(this.channelName, data);
				sent++;
			}
		} catch(Exception e) {
			Sledgehammer.logger.warning("Failed to send a Forge packet to " + (to.length - sent) + " players in channel " + this.channelName + " : " + e);
			e.printStackTrace();
		}
	}
	
	/**
	 * Sends the given packet to the given server
	 * @param pkt - Packet to send
	 * @param to - Server to send the packet to
	 */
	public void send(IForgePacket pkt, Server to) {
		try {
			to.sendData(this.channelName, this.encode(pkt));
		} catch(Exception e) {
			Sledgehammer.logger.warning("Failed to send a Forge packet to server " + to.getInfo().getName() + " in channel " + this.channelName + " : " + e);
		}
	}
	
	private byte[] encode(IForgePacket pkt) throws PacketEncodingException {
		if(!discriminatorMap.containsKey(pkt.getClass())) {
			throw new PacketEncodingException("Could not encode packet of class " + pkt.getClass().getCanonicalName() + " as it has not been registered to this channel");
		}
		int discriminator = discriminatorMap.get(pkt.getClass());
		ByteBuf stream = Unpooled.buffer();
		stream.writeByte(discriminator);
		pkt.encode(stream);
		return stream.array();
	}

	/**
	 * Registers a packet class
	 * 
	 * @param discriminator - The discriminator to use when sending this packet
	 * @param clazz - the {@link IForgePacket} implementing class
	 */
	public void registerPacket(int discriminator, Class<? extends IForgePacket> clazz) {
		packetMap.put(discriminator, clazz);
		discriminatorMap.put(clazz, discriminator);
	}
	
	/**
	 * Unregisters all packets
	 */
	public void resetPacketRegistration() {
		this.discriminatorMap.clear();
		this.packetMap.clear();
	}
	
	/**
	 * Encodes a String to a byte buffer using the [size (varint) | string (utf-8)] format
	 * @param str - String to write
	 * @param buf - Buffer to write to
	 */
	public static void writeStringToBuf(String str, ByteBuf buf) {
		DefinedPacket.writeString(str, buf);
	}
	
	/**
	 * Reads a String from a bte buffer uing the [size (varint) | string (utf-8)] format
	 * @param buf
	 * @return
	 */
	public static String readStringFromBuf(ByteBuf buf) {
		return DefinedPacket.readString(buf);
	}

}
