/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - GUIRegistry.java
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

import com.noahhusby.sledgehammer.gui.inventories.anvil.AnvilController;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;

import java.util.ArrayList;
import java.util.List;

public class GUIRegistry {
    private final static List<IController> registeredControllers = new ArrayList<>();

    public static void register(IController c) {
        removeExpiredControllers(c);
        registeredControllers.add(c);
    }

    public static void onInventoryClick(InventoryClickEvent e) {
        for(IController i : registeredControllers) {
            if(i instanceof AnvilController) continue;
            GUIController gui = (GUIController) i;
            if(gui.getInventory().equals(e.getInventory())){
                gui.onInventoryClick(e);
                return;
            }
        }
    }

    public static void onInventoryClose(InventoryCloseEvent e) {

    }

    private static void removeExpiredControllers(IController newListener) {
        List<IController> expired = new ArrayList<>();
        for(IController i : registeredControllers) {
            if (i.getPlayer() != null) {
                if(i.getPlayer().getName().equalsIgnoreCase(newListener.getPlayer().getName())) expired.add(i);
            }
        }

        for(IController i : expired) {
            registeredControllers.remove(i);
        }
    }
}
