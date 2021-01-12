package com.noahhusby.sledgehammer.gui.inventories.warp.config.confirmation;

import com.noahhusby.sledgehammer.data.warp.Warp;
import com.noahhusby.sledgehammer.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIChild;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIController;
import org.bukkit.entity.Player;

public class ConfirmationController extends GUIController {
    private final WarpConfigPayload payload;
    private final Type type;
    private Warp warp;

    public ConfirmationController(Player player, WarpConfigPayload payload, Type type, Warp warp) {
        super(27, "Warp Confirmation", player);
        this.payload = payload;
        this.type = type;
        this.warp = warp;
        init();
    }

    public ConfirmationController(Player player, WarpConfigPayload payload, Type type) {
        super(27, "Warp Confirmation", player);
        this.payload = payload;
        this.type = type;
        init();
    }

    public ConfirmationController(GUIController controller, WarpConfigPayload payload, Type type) {
        super(controller);
        this.payload = payload;
        this.type = type;
        init();
    }

    @Override
    public void init() {
        GUIChild child = null;
        switch (type) {
            case ADD_SUCCESSFUL:
                child = new CreationSuccessInventory(payload);
                break;
            case ADD_FAILURE:
                child = new CreationFailureInventory(payload);
                break;
            case REMOVE_FAILURE:
                child = new RemoveFailureInventory(payload);
                break;
            case REMOVE_SUCCESSFUL:
                child = new RemoveSuccessInventory(payload);
                break;
            case HEAD_UPDATE:
                child = new HeadUpdateSuccessInventory(payload);
                break;
            case LOCATION_UPDATE:
                child = new LocationSuccessInventory(payload, warp);
                break;
        }

        child.initFromController(this, getPlayer(), getInventory());
        openChild(child);
    }

    public enum Type {
        REMOVE_SUCCESSFUL, REMOVE_FAILURE, ADD_SUCCESSFUL, ADD_FAILURE, HEAD_UPDATE, LOCATION_UPDATE
    }
}
