package com.noahhusby.sledgehammer.gui.inventories.warp.config;

import com.noahhusby.sledgehammer.Sledgehammer;
import com.noahhusby.sledgehammer.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.gui.inventories.anvil.AnvilChild;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIRegistry;
import com.noahhusby.sledgehammer.network.S2P.S2PWarpConfigPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import net.wesjd.anvilgui.AnvilGUI;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.json.simple.JSONObject;

public class WarpNameEntryAnvil extends AnvilChild {

    private final WarpConfigPayload payload;

    public WarpNameEntryAnvil(WarpConfigPayload payload) {
        this.payload = payload;
    }

    @Override
    public AnvilGUI.Builder build(AnvilGUI.Builder builder) {
        builder.text("Enter Warp Name")
            .title("Create a warp")
            .plugin(Sledgehammer.sledgehammer);

        ItemStack warp = new ItemStack(Material.WOOL, 1, (byte) 4);
        builder.itemLeft(warp);

        return builder;
    }

    @Override
    public void onLeftItemClick() {
        GUIRegistry.register(new ConfigMenuController(getController().getPlayer(), payload));
    }

    @Override
    public void onRightItemClick() {

    }

    @Override
    public void onFinish() {
        switch (getCloseMode())  {
            case EXIT:
                break;
            case RIGHT:
            case LEFT:
                GUIRegistry.register(new ConfigMenuController(getController().getPlayer(), payload));
                break;
            case FINISH:
                if(getText().equals("")) {
                    GUIRegistry.register(new WarpNameEntryController(getController().getPlayer(), payload));
                } else {
                    JSONObject data = new JSONObject();
                    data.put("warpName", getText());
                    SledgehammerNetworkManager.getInstance().send(new S2PWarpConfigPacket(
                            S2PWarpConfigPacket.ProxyConfigAction.CREATE_WARP, getController().getPlayer(), payload.getSalt(),
                            data));
                }
                break;
        }
    }
}
