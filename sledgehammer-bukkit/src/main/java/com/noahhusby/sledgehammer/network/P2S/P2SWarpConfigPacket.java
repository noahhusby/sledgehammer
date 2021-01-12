package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.data.warp.Warp;
import com.noahhusby.sledgehammer.data.warp.WarpConfigPayload;
import com.noahhusby.sledgehammer.data.warp.WarpGroup;
import com.noahhusby.sledgehammer.gui.inventories.general.GUIRegistry;
import com.noahhusby.sledgehammer.gui.inventories.warp.config.ConfigMenuController;
import com.noahhusby.sledgehammer.gui.inventories.warp.config.confirmation.ConfirmationController;
import com.noahhusby.sledgehammer.network.IP2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.bukkit.Bukkit;
import org.json.simple.JSONObject;

public class P2SWarpConfigPacket implements IP2SPacket {
    @Override
    public String getPacketID() {
        return Constants.warpConfigID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        ServerConfigAction action = ServerConfigAction.valueOf(data.getString("action"));
        WarpConfigPayload payload = WarpConfigPayload.fromPayload(data);
        switch(action) {
            case OPEN_CONFIG:
                GUIRegistry.register(new ConfigMenuController(Bukkit.getPlayer(info.getSender()), payload));
                break;
            case ADD_FAILURE:
            case ADD_SUCCESSFUL:
            case REMOVE_SUCCESSFUL:
            case REMOVE_FAILURE:
                GUIRegistry.register(new ConfirmationController(Bukkit.getPlayer(info.getSender()),
                        payload, ConfirmationController.Type.valueOf(action.name())));
                break;
            case LOCATION_UPDATE:
                JSONObject d = (JSONObject) data.get("data");
                for(WarpGroup wg : payload.getGroups()) {
                    for(Warp w : wg.getWarps()) {
                        if(w.getId() == SledgehammerUtil.JsonUtils.toInt(d.get("warpId"))) {
                            GUIRegistry.register(new ConfirmationController(Bukkit.getPlayer(info.getSender()),
                                    payload, ConfirmationController.Type.LOCATION_UPDATE, w));
                            return;
                        }
                    }
                }
        }
    }

    public enum ServerConfigAction {
        OPEN_CONFIG, REMOVE_SUCCESSFUL, REMOVE_FAILURE, ADD_SUCCESSFUL, ADD_FAILURE, LOCATION_UPDATE
    }
}
