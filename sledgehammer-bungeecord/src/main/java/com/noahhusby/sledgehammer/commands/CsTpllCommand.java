/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - CsTpllCommand.java
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
 *  You should have received a copy of the GNU General Public License
 *  along with Sledgehammer.  If not, see <https://github.com/noahhusby/Sledgehammer/blob/master/LICENSE/>.
 */

package com.noahhusby.sledgehammer.commands;

import com.noahhusby.sledgehammer.SledgehammerUtil;
import com.noahhusby.sledgehammer.chat.ChatConstants;
import com.noahhusby.sledgehammer.commands.data.Command;
import com.noahhusby.sledgehammer.network.P2S.P2SCommandPacket;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;

public class CsTpllCommand extends Command {

    public CsTpllCommand() {
        super("cs", "");
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if (!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatConstants.issueByPlayer);
        }

        if (args.length == 0) {
            getNetworkManager().send(new P2SCommandPacket(sender.getName(), SledgehammerUtil.getServerFromSender(sender).getName(), "cs"));
            return;
        } else if (!args[0].equals("tpll")) {
            getNetworkManager().send(new P2SCommandPacket(sender.getName(), SledgehammerUtil.getServerFromSender(sender).getName(), "cs",
                    SledgehammerUtil.getRawArguments(args)));
            return;
        }

        ArrayList<String> dataList = new ArrayList<>();
        for(int x = 1; x < args.length; x++) dataList.add(args[x]);
        String[] data = dataList.toArray(new String[dataList.size()]);

        new TpllCommand().execute(sender, data);
    }
}
