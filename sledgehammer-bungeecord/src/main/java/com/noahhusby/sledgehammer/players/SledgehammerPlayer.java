/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - SledgehammerPlayer.java
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

package com.noahhusby.sledgehammer.players;


import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.addons.terramap.TerramapAddon;
import com.noahhusby.sledgehammer.addons.terramap.TerramapVersion;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.datasets.Point;
import net.md_5.bungee.api.*;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.config.ServerInfo;
import net.md_5.bungee.api.connection.PendingConnection;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.connection.Server;
import net.md_5.bungee.api.event.ServerConnectEvent;
import net.md_5.bungee.api.score.Scoreboard;

import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.util.*;

/**
 * An object representing a player on the network with Sledgehammer specific access.
 * Check ProxiedPlayer.java docs from Bungeecord for those methods
 */
@SuppressWarnings("deprecation")
public class SledgehammerPlayer implements ProxiedPlayer {

    private ProxiedPlayer player;

    private boolean flagged = false;
    private GameMode gameMode = GameMode.NONE;
    private Point location;
    private Point track;
    Map<String, Object> attributes = Maps.newHashMap();

    public SledgehammerPlayer(ProxiedPlayer player) {
        this.player = player;
    }

    /**
     * Updates the ProxiedPlayer object from the proxy
     */
    public void update() {
        this.player = ProxyServer.getInstance().getPlayer(player.getName());
    }

    @Override
    public String getDisplayName() {
        return player.getDisplayName();
    }

    @Override
    public void setDisplayName(String s) {
        player.setDisplayName(s);
    }

    @Override
    public void sendMessage(ChatMessageType chatMessageType, BaseComponent... baseComponents) {
        player.sendMessage(chatMessageType, baseComponents);
    }

    @Override
    public void sendMessage(ChatMessageType chatMessageType, BaseComponent baseComponent) {
        player.sendMessage(chatMessageType, baseComponent);
    }

    @Override
    public void connect(ServerInfo serverInfo) {
        player.connect(serverInfo);
    }

    @Override
    public void connect(ServerInfo serverInfo, ServerConnectEvent.Reason reason) {
        player.connect(serverInfo, reason);
    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback) {
        player.connect(serverInfo, callback);
    }

    @Override
    public void connect(ServerInfo serverInfo, Callback<Boolean> callback, ServerConnectEvent.Reason reason) {
        player.connect(serverInfo, callback, reason);
    }

    @Override
    public void connect(ServerConnectRequest serverConnectRequest) {
        player.connect(serverConnectRequest);
    }

    @Override
    public Server getServer() {
        return player.getServer();
    }

    @Override
    public int getPing() {
        return player.getPing();
    }

    @Override
    public void sendData(String s, byte[] bytes) {
        player.sendData(s, bytes);
    }

    @Override
    public PendingConnection getPendingConnection() {
        return player.getPendingConnection();
    }

    @Override
    public void chat(String s) {
        player.chat(s);
    }

    @Override
    public ServerInfo getReconnectServer() {
        return player.getReconnectServer();
    }

    @Override
    public void setReconnectServer(ServerInfo serverInfo) {
        player.setReconnectServer(serverInfo);
    }

    @Override
    public String getUUID() {
        return player.getUUID();
    }

    @Override
    public UUID getUniqueId() {
        return player.getUniqueId();
    }

    @Override
    public Locale getLocale() {
        return player.getLocale();
    }

    @Override
    public byte getViewDistance() {
        return player.getViewDistance();
    }

    @Override
    public ChatMode getChatMode() {
        return player.getChatMode();
    }

    @Override
    public boolean hasChatColors() {
        return player.hasChatColors();
    }

    @Override
    public SkinConfiguration getSkinParts() {
        return player.getSkinParts();
    }

    @Override
    public MainHand getMainHand() {
        return player.getMainHand();
    }

    @Override
    public void setTabHeader(BaseComponent baseComponent, BaseComponent baseComponent1) {
        player.setTabHeader(baseComponent, baseComponent1);
    }

    @Override
    public void setTabHeader(BaseComponent[] baseComponents, BaseComponent[] baseComponents1) {
        player.setTabHeader(baseComponents, baseComponents1);
    }

    @Override
    public void resetTabHeader() {
        player.resetTabHeader();
    }

    @Override
    public void sendTitle(Title title) {
        player.sendTitle(title);
    }

    @Override
    public boolean isForgeUser() {
        return player.isForgeUser();
    }

    @Override
    public Map<String, String> getModList() {
        return player.getModList();
    }

    @Override
    public Scoreboard getScoreboard() {
        return player.getScoreboard();
    }

    @Override
    public String getName() {
        return player.getName();
    }

    @Override
    public void sendMessage(String s) {
        player.sendMessage(s);
    }

    @Override
    public void sendMessages(String... strings) {
        player.sendMessages(strings);
    }

    @Override
    public void sendMessage(BaseComponent... baseComponents) {
        player.sendMessage(baseComponents);
    }

    @Override
    public void sendMessage(BaseComponent baseComponent) {
        player.sendMessage(baseComponent);
    }

    @Override
    public Collection<String> getGroups() {
        return player.getGroups();
    }

    @Override
    public void addGroups(String... strings) {
        player.addGroups(strings);
    }

    @Override
    public void removeGroups(String... strings) {
        player.removeGroups(strings);
    }

    @Override
    public boolean hasPermission(String s) {
        return player.hasPermission(s);
    }

    @Override
    public void setPermission(String s, boolean b) {
        player.setPermission(s, b);
    }

    @Override
    public Collection<String> getPermissions() {
        return player.getPermissions();
    }

    @Override
    public InetSocketAddress getAddress() {
        return player.getAddress();
    }

    @Override
    public SocketAddress getSocketAddress() {
        return player.getSocketAddress();
    }

    @Override
    public void disconnect(String s) {
        player.disconnect(s);
    }

    @Override
    public void disconnect(BaseComponent... baseComponents) {
        player.disconnect(baseComponents);
    }

    @Override
    public void disconnect(BaseComponent baseComponent) {
        player.disconnect(baseComponent);
    }

    @Override
    public boolean isConnected() {
        return player.isConnected();
    }

    @Override
    public Unsafe unsafe() {
        return player.unsafe();
    }

    /**
     * Checks if player is on a sledgehammer server
     * @return True if on a sledgehammer server, false if not
     */
    public boolean onSledgehammer() {
        return SledgehammerUtil.isSledgehammerServer(getServer().getInfo());
    }

    /**
     * Checks if the player is on a build server
     * @return True if on a build server, false if not
     */
    public boolean onEarthServer() {
    	Server playerServer = getServer();
    	if(playerServer == null) return false;
        SledgehammerServer server = ServerConfig.getInstance().getServer(playerServer.getInfo().getName());
        if(server == null) return false;
        return server.isEarthServer();
    }

    /**
     * Gets {@link SledgehammerServer} from player
     * @return {@link SledgehammerServer}
     */
    public SledgehammerServer getSledgehammerServer() {
        return ServerConfig.getInstance().getServer(getServer().getInfo().getName());
    }
    
    /**
     * @author SmylerMC
     * Do not block features depending on the result of this method, just test if the user has Forge instead.
     * This will only work if the user has logged onto a Forge server at least once, and will return false otherwise.
     * 
     * @return whether or not this player has a compatible version of Terramap installed
     */
    public boolean hasCompatibleTerramap() {
    	return TerramapAddon.MINIMUM_COMPATIBLE_VERSION.isOlderOrSame(TerramapVersion.getClientVersion(this));
    }

    /**
     * Gets the current location of the player
     * @return Location of player if known, null if not
     */
    public Point getLocation() {
        return location;
    }

    /**
     * Sets the current location of the player
     * @param p Location of player
     */
    public void setLocation(Point p) {
        if(getSledgehammerServer() != null) {
            if(getSledgehammerServer().isStealthMode()) {
                this.location = null;
                return;
            }
        }
        this.location = p;
    }

    /**
     * Gets the current tracking point for border teleportation
     * @return The current tracking point if known, null if not
     */
    public Point getTrackingPoint() {
        return track;
    }

    /**
     * Set the current tracking point for border teleportation
     * @param p Location of tracking point
     */
    public void setTrackingPoint(Point p) {
        this.track = p;
    }

    /**
     * Checks whether the player is flagged for border checking
     * @return True if flagged, false if not
     */
    public boolean isFlagged() {
        return flagged;
    }

    /**
     * Set whether the player is flagged for border checking
     * @param flagged True to flag, false to not
     */
    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }

    /**
     * Gets the current GameMode of the player
     * @return {@link GameMode}
     */
    public GameMode getGameMode() {
        return gameMode;
    }

    /**
     * Set the current GameMode of the player
     * @param gameMode GameMode of Player
     */
    public void setGameMode(GameMode gameMode) {
        this.gameMode = gameMode;
    }

    /**
     * Get the map of player attributes
     * @return Map of attributes
     */
    public Map<String, Object> getAttributes() {
        return attributes;
    }

    /**
     * Set the map of attributes
     * Only use this to set the entire map. To add, check, or remove an item, use {@link #getAttributes().put()}
     * @param attributes The new map of attributes
     */
    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    /**
     * Gets whether a specific attribute exists
     * @param key Attribute Key
     * @param object Attribute Value
     * @return True if the attribute exists and matches, false if the attribute doesn't exist or doesn't match
     */
    public boolean checkAttribute(String key, Object object) {
        if(attributes.get(key) == null) return false;
        return attributes.get(key).equals(object);
    }

    /**
     * Get SledgehammerPlayer from player name
     * @param s Name of player
     * @return {@link SledgehammerPlayer}
     */
    public static SledgehammerPlayer getPlayer(String s) {
        return PlayerManager.getInstance().getPlayer(s);
    }

    /**
     * Get SledgehammerPlayer by command sender
     * @param s CommandSender
     * @return {@link SledgehammerPlayer}
     */
    public static SledgehammerPlayer getPlayer(CommandSender s) {
        return PlayerManager.getInstance().getPlayer(s);
    }
}
