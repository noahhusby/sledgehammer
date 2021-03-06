package com.noahhusby.sledgehammer.server.gui.warp.config.confirmation;

import com.google.common.collect.Lists;
import com.noahhusby.sledgehammer.server.Constants;
import com.noahhusby.sledgehammer.server.SledgehammerUtil;
import com.noahhusby.sledgehammer.server.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.server.gui.GUIChild;
import com.noahhusby.sledgehammer.server.gui.GUIRegistry;
import com.noahhusby.sledgehammer.server.gui.warp.config.ConfigMenu;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

public class RemoveFailureInventory extends GUIChild {
    private final WarpConfigPayload payload;

    public RemoveFailureInventory(WarpConfigPayload payload) {
        this.payload = payload;
    }

    @Override
    public void init() {
        ItemStack skull = SledgehammerUtil.getSkull(Constants.redExclamationMark, ChatColor.RED + "" + ChatColor.BOLD +
                                                                                  "Failed to remove warp!");
        skull.setLore(Lists.newArrayList(ChatColor.BLUE + "Click to continue"));
        fillInventory(skull);
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        GUIRegistry.register(new ConfigMenu.ConfigMenuController(getPlayer(), payload));
    }
}
