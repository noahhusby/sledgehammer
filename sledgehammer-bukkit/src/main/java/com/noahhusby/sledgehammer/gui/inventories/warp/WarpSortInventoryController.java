/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - WarpInventoryController.java
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

import com.noahhusby.sledgehammer.data.warp.WarpPayload;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIChild;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIController;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpSortInventoryController extends GUIController {
    private final List<AllWarpInventory> warpInventories = new ArrayList<>();
    private final WarpPayload payload;

    public WarpSortInventoryController(Player p, WarpPayload payload) {
        super(27, "Warp Sort", p);
        this.payload = payload;
        init();
    }

    @Override
    public void init() {
        GUIChild inventory = new WarpSortInventory(payload);
        inventory.initFromController(this, getPlayer(), getInventory());
        openChild(inventory);
    }

    public WarpPayload getPayload() {
        return payload;
    }
}
