/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - GUIController.java
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

package com.noahhusby.sledgehammer.gui.inventories.general;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;

public class GUIController implements IController {
    private IGUIChild currentChild = null;
    private final Inventory inventory;
    private final Player player;

    public GUIController(int size, String name, Player player) {
        inventory = Bukkit.createInventory(null, size, name);
        player.openInventory(inventory);
        this.player = player;
    }

    public GUIController(GUIController controller) {
        this.player = controller.player;
        this.inventory = controller.inventory;
    }

    public void onInventoryClick(InventoryClickEvent e) {
        currentChild.onInventoryClick(e);
    }

    public void openChild(IGUIChild child) {
        if(child == null) return;
        this.currentChild = child;
        inventory.setContents(child.getInventory().getContents());
        player.updateInventory();
    }

    public Inventory getInventory() {
        return inventory;
    }

    public Player getPlayer() {
        return player;
    }

    public void close() {
        player.closeInventory();
    }

    public void init() {}
}
