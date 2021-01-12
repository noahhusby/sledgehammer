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

package com.noahhusby.sledgehammer.gui.inventories.warp.config.manage;

import com.noahhusby.sledgehammer.data.warp.Warp;
import com.noahhusby.sledgehammer.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.data.warp.WarpGroup;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIController;
import org.bukkit.entity.Player;

public class ManageWarpInventoryController extends GUIController {

    private WarpConfigPayload payload;
    private Warp warp;

    public ManageWarpInventoryController(Player p, WarpConfigPayload payload, int warpId) {
        super(27, "Edit Warp Settings", p);
        this.payload = payload;
        for(WarpGroup wg : payload.getGroups())
            for(Warp w : wg.getWarps())
                if(w.getId() == warpId) this.warp = w;
        init();
    }

    public ManageWarpInventoryController(Player p, WarpConfigPayload payload, Warp warp) {
        super(27, "Edit Warp Settings", p);
        this.payload = payload;
        this.warp = warp;
        init();
    }

    public ManageWarpInventoryController(GUIController controller, WarpConfigPayload payload, Warp warp) {
        super(controller);
        this.payload = payload;
        this.warp = warp;
        init();
    }


    @Override
    public void init() {
        ManageWarpInventory warpConfig = new ManageWarpInventory(getPayload(), warp);
        warpConfig.initFromController(this, getPlayer(), getInventory());
        openChild(warpConfig);
    }

    public WarpConfigPayload getPayload() {
        return payload;
    }

}
