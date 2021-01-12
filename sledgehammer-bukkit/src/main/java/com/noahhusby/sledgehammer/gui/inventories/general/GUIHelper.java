package com.noahhusby.sledgehammer.gui.inventories.general;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class GUIHelper {
    public static ItemStack generateExit() {
        ItemStack exit = new ItemStack(Material.BARRIER);
        ItemMeta m = exit.getItemMeta();
        m.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Close");
        exit.setItemMeta(m);

        return exit;
    }

    public static ItemStack generateCompass() {
        ItemStack compass = new ItemStack(Material.COMPASS);
        ItemMeta m = compass.getItemMeta();

        m.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Warp Menu");
        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "View other warps");
        m.setLore(lore);

        compass.setItemMeta(m);

        return compass;
    }

    public static ItemStack generateWarpSort() {
        ItemStack sort = new ItemStack(Material.HOPPER, 1);
        ItemMeta sortMeta = sort.getItemMeta();
        sortMeta.setDisplayName(ChatColor.RED + "" + ChatColor.BOLD + "Sort");
        sort.setItemMeta(sortMeta);
        return sort;
    }

    public static ItemStack generateWarpAnvil() {
        ItemStack anvil = new ItemStack(Material.ANVIL, 1);
        ItemMeta meta = anvil.getItemMeta();
        meta.setDisplayName(ChatColor.LIGHT_PURPLE + "" + ChatColor.BOLD + "Configure Warps");
        anvil.setItemMeta(meta);

        return anvil;
    }
}
