/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - WarpInventory.java
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
 * You should have received a copy of the GNU General Public License
 * along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.gui.inventories.warp;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.data.warp.Warp;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIChild;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIHelper;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIRegistry;
import com.noahhusby.sledgehammer.network.S2P.S2PWarpConfigPacket;
import com.noahhusby.sledgehammer.network.S2P.S2PWarpPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class PinnedWarpInventory extends GUIChild {
    private final int page;
    private final List<Warp> warps;

    private Inventory inventory;

    public PinnedWarpInventory(int page, List<Warp> warps) {
        this.page = page;
        this.warps = warps;
    }

    @Override
    public void init() {
        this.inventory = getInventory();
        int total_pages = (int) Math.ceil(warps.size() / 27.0);

        for(int x = 0; x < 54; x++) {
            ItemStack glass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (byte) 15);

            ItemMeta meta = glass.getItemMeta();
            meta.setDisplayName(ChatColor.RESET+"");
            meta.setDisplayName(null);
            glass.setItemMeta(meta);

            inventory.setItem(x, glass);
        }

        inventory.setItem(4, SledgehammerUtil.getSkull(Constants.lampHead, ChatColor.YELLOW + "" + ChatColor.BOLD + "Pinned Warps"));
        inventory.setItem(40, GUIHelper.generateCompass());
        inventory.setItem(45, GUIHelper.generateWarpSort());
        inventory.setItem(49, GUIHelper.generateExit());

        if(((PinnedWarpInventoryController) getController()).getPayload().isEditAccess())
            inventory.setItem(46, GUIHelper.generateWarpAnvil());

        boolean paged = false;
        if(page != 0) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowLeftHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Previous Page");
            inventory.setItem(51, head);
            paged = true;
        }

        if(warps.size() > (page + 1) * Constants.warpsPerPage) {
            ItemStack head = SledgehammerUtil.getSkull(Constants.arrowRightHead, ChatColor.AQUA + "" + ChatColor.BOLD + "Next Page");
            inventory.setItem(53, head);
            paged = true;
        }

        if(paged) {
            inventory.setItem(52, SledgehammerUtil.NumberHeads.getHead((page + 1), ChatColor.GREEN +
                    "" + ChatColor.BOLD + "Page " + (page + 1)));
        }

        int min = page * 27;
        int max = min + 27;

        if(max > warps.size()) {
            max = min + (warps.size() - (page * 27));
        }

        int current = 9;
        for(int x = min; x < max; x++) {
            Warp warp = warps.get(x);

            String headId = warp.getHeadID();
            if(headId.equals("")) headId = Constants.yellowWoolHead;
            ItemStack item = SledgehammerUtil.getSkull(headId, ((warp.getPinnedMode() == Warp.PinnedMode.GLOBAL
            || warp.getPinnedMode() == Warp.PinnedMode.LOCAL) ? ChatColor.GOLD : ChatColor.BLUE)
                    + "" + ChatColor.BOLD + warp.getName());

            ItemMeta meta = item.getItemMeta();

            List<String> lore = new ArrayList<>();
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.DARK_GRAY + "Server: " + warp.getServer());
            lore.add(ChatColor.DARK_GRAY + "> " + ChatColor.GREEN + "Click to warp.");
            lore.add(ChatColor.BLUE + "" + ChatColor.STRIKETHROUGH + "------------------");
            lore.add(ChatColor.GRAY + "ID: " + warp.getId());
            meta.setLore(lore);
            item.setItemMeta(meta);

            inventory.setItem(current, item);
            current++;
        }
    }

    @Override
    public void onInventoryClick(InventoryClickEvent e) {
        e.setCancelled(true);
        if(e.getCurrentItem() == null) return;
        if(e.getCurrentItem().getItemMeta() == null) return;
        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        PinnedWarpInventoryController controller = (PinnedWarpInventoryController) getController();

        if(e.getCurrentItem().getItemMeta().getDisplayName() == null) return;

        if(e.getSlot() == 40) {
            GUIRegistry.register(new WarpMenuInventoryController(getController(), controller.getPayload()));
            return;
        }

        if(e.getSlot() == 45) {
            controller.close();
            GUIRegistry.register(new WarpSortInventoryController(getPlayer(), controller.getPayload()));
            return;
        }

        if(e.getSlot() == 46 && controller.getPayload().isEditAccess()) {
            controller.close();
            SledgehammerNetworkManager.getInstance().send(new S2PWarpConfigPacket(S2PWarpConfigPacket.ProxyConfigAction.OPEN_CONFIG,
                    getPlayer(), controller.getPayload().getSalt()));
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Previous Page")) {
            controller.openChild(controller.getChildByPage(page-1));
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Next Page")) {
            controller.openChild(controller.getChildByPage(page+1));
            return;
        }

        if(ChatColor.stripColor(e.getCurrentItem().getItemMeta().getDisplayName()).equalsIgnoreCase("Close")) {
            controller.close();
            return;
        }


        if(e.getSlot() > 8 && e.getSlot() < 36) {
            ItemMeta meta = e.getCurrentItem().getItemMeta();
            int id = -1;
            List<String> lore = meta.getLore();
            for(String s : lore) {
                if(s.contains("ID:"))
                    id = new Long(ChatColor.stripColor(s).replaceAll("[^\\d.]", "")).intValue();
            }

            SledgehammerNetworkManager.getInstance().send(new S2PWarpPacket(player, controller.getPayload(), id));

            controller.close();
            return;
        }
    }

    public int getPage() {
        return page;
    }
}
