package com.noahhusby.sledgehammer.chat;

import com.google.common.collect.Maps;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import java.util.Map;

public class ChatHandler implements Listener {
    private static ChatHandler instance = null;
    public static ChatHandler getInstance() {
        return instance == null ? instance = new ChatHandler() : instance;
    }

    private Map<Player, ChatResponse> entries = Maps.newHashMap();

    public void startEntry(Player player, String message, ChatResponse response) {
        entries.remove(player);

        entries.put(player, response);
        for(int i = 0; i < 18; i++) {
            player.sendMessage();
        }

        player.sendMessage(ChatColor.RED + "--------------------------------------------------");
        player.sendMessage(message);
        player.sendMessage(ChatColor.GRAY + "Send " + ChatColor.GOLD + "'cancel' " + ChatColor.GRAY + "to cancel.");
        player.sendMessage(ChatColor.RED + "--------------------------------------------------");
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent e) {
        boolean cancel = entries.containsKey(e.getPlayer());
        if(!cancel) return;

        e.setCancelled(true);
        boolean cancelled = e.getMessage().equalsIgnoreCase("cancel");
        entries.get(e.getPlayer()).onResponse(!cancelled, e.getMessage());
        entries.remove(e.getPlayer());
    }

    @EventHandler
    public void onDisconnect(PlayerQuitEvent e) {
        entries.remove(e.getPlayer());
    }

}
