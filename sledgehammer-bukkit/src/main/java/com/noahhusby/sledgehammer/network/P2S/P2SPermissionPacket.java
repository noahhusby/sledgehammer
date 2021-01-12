package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.Constants;
import com.noahhusby.sledgehammer.SmartObject;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2P.S2PPermissionPacket;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class P2SPermissionPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.permissionCheckID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        String player = data.getString("player");
        String salt = data.getString("salt");
        String permission = data.getString("permission");

        boolean permissionResponse = false;

        Player p = Bukkit.getPlayer(player);
        if(p != null) {
            permissionResponse = p.hasPermission(permission);
        }

        getManager().send(new S2PPermissionPacket(p, salt, permissionResponse));
    }
}
