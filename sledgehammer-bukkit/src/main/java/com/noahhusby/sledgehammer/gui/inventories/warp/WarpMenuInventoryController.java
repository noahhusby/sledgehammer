/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - SetServerWarpInventoryController.java
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

import com.noahhusby.sledgehammer.data.warp.WarpGroup;
import com.noahhusby.sledgehammer.data.warp.WarpPayload;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIController;
import com.noahhusby.sledgehammer.gui.inventories.general.IGUIChild;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class WarpMenuInventoryController extends GUIController {
    private final List<WarpMenuInventory> warpInventories = new ArrayList<>();
    private final WarpPayload payload;

    public WarpMenuInventoryController(Player p, WarpPayload payload) {
        super(54, "Warps", p);
        this.payload = payload;
        init();
    }

    public WarpMenuInventoryController(GUIController controller, WarpPayload payload){
        super(controller);
        this.payload = payload;
        init();
    }

    @Override
    public void init() {
        List<WarpGroup> groups = payload.getGroups();

        int total_pages = (int) Math.ceil(groups.size() / 27.0);
        if(total_pages == 0) total_pages = 1;
        for(int x = 0; x < total_pages; x++) {
            WarpMenuInventory w = new WarpMenuInventory(x, groups);
            w.initFromController(this, getPlayer(), getInventory());
            warpInventories.add(w);
        }

        openChild(getChildByPage(0));
    }

    public IGUIChild getChildByPage(int page) {
        for(WarpMenuInventory w : warpInventories) {
            if(w.getPage() == page) return w;
        }

        return null;
    }

    public WarpPayload getPayload() {
        return payload;
    }
}
