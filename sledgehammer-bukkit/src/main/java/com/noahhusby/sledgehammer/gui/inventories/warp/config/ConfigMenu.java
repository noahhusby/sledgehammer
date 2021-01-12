package com.noahhusby.sledgehammer.gui.inventories.warp.config;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIChild;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIRegistry;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ConfigMenu extends GUIChild {

    public ConfigMenu() {}

    @Override
    public void init() {
        for(int x = 0; x < 27; x++) {
            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);

            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(ChatColor.RESET+"");
            meta.setDisplayName(null);
            glass.setItemMeta(meta);

            inventory.setItem(x, glass);
        }

        inventory.setItem(13, SledgehammerUtil.getSkull(Constants.limePlusHead, ChatColor.GREEN + "" + ChatColor.BOLD + "Create new warp"));
        inventory.setItem(15, SledgehammerUtil.getSkull(Constants.goldenExclamationHead, ChatColor.GOLD + "" + ChatColor.BOLD + "Manage Warps"));

    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;
        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;
        if (e.getSlot() == 13) {
            GUIRegistry.register(new WarpNameEntryController(getPlayer(), ((ConfigMenuController) controller).getPayload()));
            return;
        }
        if(e.getSlot() == 15) {
            GUIRegistry.register(new ManageGroupInventoryController(getPlayer(), ((ConfigMenuController) controller).getPayload()));
            return;
        }
    }


}
