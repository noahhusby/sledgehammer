package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2PPacket;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class S2PPermissionPacket extends S2PPacket {

    private final boolean permission;
    private final Player player;
    private final String salt;

    public S2PPermissionPacket(Player player, String salt, boolean permission) {
        this.player = player;
        this.permission = permission;
        this.salt = salt;
    }

    @Override
    public String getPacketID() {
        return Constants.permissionCheckID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("salt", salt);
        data.put("permission", permission);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player);
    }
}
