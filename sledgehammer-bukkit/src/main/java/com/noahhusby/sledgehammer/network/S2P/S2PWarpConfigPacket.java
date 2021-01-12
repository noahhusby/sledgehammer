package com.noahhusby.sledgehammer.network.S2P;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.network.IS2PPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import org.bukkit.entity.Player;
import org.json.simple.JSONObject;

public class S2PWarpConfigPacket implements IS2PPacket {
    private final ProxyConfigAction action;
    private final Player player;
    private final String salt;
    private final JSONObject data;

    public S2PWarpConfigPacket(ProxyConfigAction action, Player player, String salt) {
        this(action, player, salt, new JSONObject());
    }

    public S2PWarpConfigPacket(ProxyConfigAction action, Player player, String salt, JSONObject data) {
        this.action = action;
        this.player = player;
        this.salt = salt;
        this.data = data;
    }

    @Override
    public String getPacketID() {
        return Constants.warpConfigID;
    }

    @Override
    public JSONObject getMessage(JSONObject data) {
        data.put("salt", salt);
        data.put("action", action.name());
        data.put("data", this.data);
        return data;
    }

    @Override
    public PacketInfo getPacketInfo() {
        return PacketInfo.build(getPacketID(), player);
    }

    public enum ProxyConfigAction {
        OPEN_CONFIG, CREATE_WARP, UPDATE_WARP, UPDATE_PLAYER_DEFAULT, WARP_UPDATE_LOCATION, REMOVE_WARP
    }
}
