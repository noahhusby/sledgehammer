/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - ServerWarpInventoryController.java
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

package com.noahhusby.sledgehammer.gui.inventories.warp.config;

import com.noahhusby.sledgehammer.data.warp.Warp;
import com.noahhusby.sledgehammer.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.data.warp.WarpGroup;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIController;
import com.noahhusby.sledgehammer.gui.inventories.general.IGUIChild;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class ManageGroupWarpInventoryController extends GUIController {
    private final List<ManageGroupWarpInventory> warpInventories = new ArrayList<>();
    private final WarpConfigPayload payload;
    private final String groupId;

    public ManageGroupWarpInventoryController(Player p, WarpConfigPayload payload, String groupId) {
        super(45, "Select a warp to manage", p);
        this.payload = payload;
        this.groupId = groupId;
        init();
    }

    public ManageGroupWarpInventoryController(GUIController controller, WarpConfigPayload payload, String groupId) {
        super(controller);
        this.payload = payload;
        this.groupId = groupId;
        init();
    }

    @Override
    public void init() {
        WarpGroup group = null;
        for(WarpGroup g : payload.getGroups())
            if(g.getId().equals(groupId)) group = g;

        List<Warp> warps = group.getWarps();

        int total_pages = (int) Math.ceil(warps.size() / 27.0);
        if(total_pages == 0) total_pages = 1;
        for(int x = 0; x < total_pages; x++) {
            ManageGroupWarpInventory w = new ManageGroupWarpInventory(x, warps, group);
            w.initFromController(this, getPlayer(), getInventory());
            warpInventories.add(w);
        }

        openChild(getChildByPage(0));
    }

    public IGUIChild getChildByPage(int page) {
        for(ManageGroupWarpInventory w : warpInventories) {
            if(w.getPage() == page) return w;
        }

        return null;
    }

    public WarpConfigPayload getPayload() {
        return payload;
    }
}
