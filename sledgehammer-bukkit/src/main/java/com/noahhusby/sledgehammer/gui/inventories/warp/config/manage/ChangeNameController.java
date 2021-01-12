package com.noahhusby.sledgehammer.gui.inventories.warp.config.manage;

import com.noahhusby.sledgehammer.data.warp.Warp;
import com.noahhusby.sledgehammer.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.gui.inventories.anvil.AnvilController;
import org.bukkit.entity.Player;

public class ChangeNameController extends AnvilController {
    private final WarpConfigPayload payload;
    private final Warp warp;
    public ChangeNameController(AnvilController controller, WarpConfigPayload payload, Warp warp) {
        super(controller);
        this.payload = payload;
        this.warp = warp;
        init();
    }

    public ChangeNameController(Player player, WarpConfigPayload payload, Warp warp) {
        super(player);
        this.payload = payload;
        this.warp = warp;
        init();
    }

    @Override
    public void init() {
        openChild(new ChangeNameAnvil(payload, warp));
    }
}
