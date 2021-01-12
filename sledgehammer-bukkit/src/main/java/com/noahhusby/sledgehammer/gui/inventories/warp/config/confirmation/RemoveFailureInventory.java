package com.noahhusby.sledgehammer.gui.inventories.warp.config.confirmation;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIChild;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIRegistry;
import com.noahhusby.sledgehammer.gui.inventories.warp.config.ConfigMenuController;
import org.bukkit.ChatColor;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class RemoveFailureInventory extends GUIChild {
    private final WarpConfigPayload payload;
    public RemoveFailureInventory(WarpConfigPayload payload) {
        this.payload = payload;
    }

    @Override
    public void init() {
        for(int x = 0; x < 27; x++) {
            ItemStack skull = SledgehammerUtil.getSkull(Constants.redExclamationMark, ChatColor.RED + "" + ChatColor.BOLD +
                    "Failed to remove warp!");

            ItemMeta meta = skull.getItemMeta();
            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "Click to continue");
            meta.setLore(lore);

            skull.setItemMeta(meta);

            inventory.setItem(x, skull);
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        GUIRegistry.register(new ConfigMenuController(getPlayer(), payload));
    }
}
