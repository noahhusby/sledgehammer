/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - WarpCommand.java
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
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import com.noahhusby.sledgehammer.commands.fragments.warps.*;
import com.noahhusby.sledgehammer.config.ConfigHandler;
import com.noahhusby.sledgehammer.config.ServerConfig;
import com.noahhusby.sledgehammer.config.ServerGroup;
import com.noahhusby.sledgehammer.config.SledgehammerServer;
import com.noahhusby.sledgehammer.network.P2S.P2STeleportPacket;
import com.noahhusby.sledgehammer.network.P2S.P2SWarpGUIPacket;
import com.noahhusby.sledgehammer.network.SledgehammerNetworkManager;
import com.noahhusby.sledgehammer.permissions.PermissionHandler;
import com.noahhusby.sledgehammer.permissions.PermissionRequest;
import com.noahhusby.sledgehammer.players.SledgehammerPlayer;
import com.noahhusby.sledgehammer.warp.Warp;
import com.noahhusby.sledgehammer.warp.WarpHandler;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.ArrayList;
import java.util.List;

public class WarpCommand extends WarpFragmentManager implements TabExecutor {

    public WarpCommand(String name) {
        super(name, "sledgehammer.warp");
        registerCommandFragment(new WarpSetFragment());
        registerCommandFragment(new WarpRemoveFragment());
        registerCommandFragment(new WarpListFragment());
        registerCommandFragment(new WarpMenuFragment());
        if(ConfigHandler.mapEnabled)
            registerCommandFragment(new WarpMapFragment());
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        if(!(sender instanceof ProxiedPlayer)) {
            sender.sendMessage(ChatConstants.issueByPlayer);
            return;
        }

        if(!isAllowed(sender)) {
            sender.sendMessage(ChatConstants.getNotAvailable());
            return;
        }

        if(executeFragment(sender, args)) return;

        PermissionHandler.getInstance().check(SledgehammerPlayer.getPlayer(sender), "sledgehammer.warp",(code, global) -> {
            if(code == PermissionRequest.PermissionCode.PERMISSION) {
                run(sender, args);
                return;
            }
            sender.sendMessage(ChatConstants.noPermission);
        });
    }

    private void run(CommandSender sender, String[] args) {
        if(args.length == 0) {
            if(ConfigHandler.warpMenuDefault) {
                SledgehammerServer server = SledgehammerPlayer.getPlayer(sender).getSledgehammerServer();
                boolean openGUI = (server != null);

                if(openGUI)
                    if(!server.isInitialized()) openGUI = false;

                if(openGUI) {
                    PermissionHandler.getInstance().check(SledgehammerPlayer.getPlayer(sender), "sledgehammer.warp.edit", (code, global) -> {
                        SledgehammerNetworkManager.getInstance().send(new P2SWarpGUIPacket(sender.getName(),
                                SledgehammerUtil.getServerFromSender(sender).getName(),
                                code == PermissionRequest.PermissionCode.PERMISSION));
                    });
                    return;
                }
            }

            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Usage: /nwarp <warp>", ChatColor.RED)));
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Use ", ChatColor.GRAY),
                    new TextElement(String.format("/%s list", ConfigHandler.warpCommand), ChatColor.BLUE),
                    new TextElement(" to see the available warps.", ChatColor.GRAY)));
            return;
        }

        List<String> aliases = new ArrayList<>();
        for(ServerGroup group : ServerConfig.getInstance().getGroups())
            aliases.addAll(group.getAliases());

        if(aliases.contains(args[0])) {
            for(ServerGroup g : ServerConfig.getInstance().getGroups()) {
                if(g.getAliases().contains(args[0])) {
                    PermissionHandler.getInstance().check(SledgehammerPlayer.getPlayer(sender), "sledgehammer.warp.edit", (code, global) -> {
                        SledgehammerNetworkManager.getInstance().send(new P2SWarpGUIPacket(sender.getName(),
                                SledgehammerUtil.getServerFromSender(sender).getName(),
                                code == PermissionRequest.PermissionCode.PERMISSION, g.getID()));

                    });
                    return;
                }
            }
        }

        List<Warp> warps = new ArrayList<>();
        for(Warp w : WarpHandler.getInstance().getWarps())
            if(w.getPinnedMode() == Warp.PinnedMode.GLOBAL || !ConfigHandler.localWarp
            || w.getServer().equalsIgnoreCase(SledgehammerUtil.getServerFromSender(sender).getName())) warps.add(w);

        String warpName = SledgehammerUtil.getRawArguments(args);
        Warp warp = null;
        for(Warp w : warps)
            if(w.getName().equalsIgnoreCase(warpName)) warp = w;

        if(warp == null) {
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("That warp does not exist!", ChatColor.RED)));
            return;
        }

        if(SledgehammerUtil.getServerFromSender(sender) != SledgehammerUtil.getServerByName(warp.getServer())) {
            SledgehammerPlayer.getPlayer(sender).connect(SledgehammerUtil.getServerByName(warp.getServer()));
            sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Sending you to ", ChatColor.GRAY), new TextElement(warp.getServer(), ChatColor.RED)));
        }

        sender.sendMessage(ChatHelper.makeTitleTextComponent(new TextElement("Warping to ", ChatColor.GRAY), new TextElement(warp.getName(), ChatColor.RED)));
        getNetworkManager().send(new P2STeleportPacket(sender.getName(), warp.getServer(), warp.getPoint()));
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender sender, String[] args) {
        boolean tab = PermissionHandler.getInstance().isAdmin(sender) || sender.hasPermission("sledgehammer.warp");
        if(tab) {
            SledgehammerServer s = SledgehammerPlayer.getPlayer(sender).getSledgehammerServer();
            if(s == null) return new ArrayList<>();
            ServerGroup group = s.getGroup();

            List<String> tabbedWarps = new ArrayList<>();
            for(Warp w : WarpHandler.getInstance().getWarps())
                if(group.getServers().contains(w.getServer()))
                    tabbedWarps.add(w.getName());

            for(ServerGroup g : ServerConfig.getInstance().getGroups())
                tabbedWarps.addAll(g.getAliases());

            List<String> completion = new ArrayList<>();
            SledgehammerUtil.copyPartialMatches(args[0], tabbedWarps, completion);
            return completion;
        }

        return new ArrayList<>();
    }
}
