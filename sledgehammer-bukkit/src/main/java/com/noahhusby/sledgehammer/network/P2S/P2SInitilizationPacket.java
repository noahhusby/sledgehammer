/*
 * Copyright (c) 2020 Noah Husby
 * sledgehammer - P2SInitilizationPacket.java
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

package com.noahhusby.sledgehammer.network.P2S;

import com.noahhusby.sledgehammer.*;
import com.noahhusby.sledgehammer.network.P2SPacket;
import com.noahhusby.sledgehammer.network.PacketInfo;
import com.noahhusby.sledgehammer.network.S2P.S2PInitializationPacket;

public class P2SInitilizationPacket extends P2SPacket {
    @Override
    public String getPacketID() {
        return Constants.initID;
    }

    @Override
    public void onMessage(PacketInfo info, SmartObject data) {
        Sledgehammer.bungeecordName = info.getServer();
        getManager().send(new S2PInitializationPacket(SledgehammerUtil.getPlayerFromName(info.getSender())));
    }
}
