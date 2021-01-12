/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - AddonManager.java
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

package com.noahhusby.sledgehammer.addons;

import com.noahhusby.sledgehammer.Sledgehammer;
import net.md_5.bungee.api.event.PluginMessageEvent;
import net.md_5.bungee.api.plugin.Listener;
import net.md_5.bungee.event.EventHandler;
import net.md_5.bungee.event.EventPriority;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AddonManager implements Listener {
    private static AddonManager mInstance = null;

    public static AddonManager getInstance() {
        if(mInstance == null) mInstance = new AddonManager();
        return mInstance;
    }

    private AddonManager() {
        Sledgehammer.addListener(this);
    }

    List<IAddon> addons = new ArrayList<>();

    public void registerAddon(IAddon addon) {
        addons.add(addon);
    }

    public void onEnable() {
        addons.forEach(IAddon::onEnable);
    }

    public void onDisable() {
        addons.forEach(IAddon::onDisable);
        addons.clear(); // If the plugin is reloading, new instances of the add-ons will be registered again
    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPluginMessage(PluginMessageEvent e) {
        IAddon addon = getServerByChannel(e.getTag());
        if(addon != null) addon.onPluginMessage(e);
    }

    private IAddon getServerByChannel(String channel) {
        for(IAddon a : addons) {
            if(a.getMessageChannels() != null) {
                if(Arrays.asList(a.getMessageChannels()).contains(channel)) return a;
            }
        }

        return null;
    }


}
