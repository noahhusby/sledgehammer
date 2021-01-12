/*
 * Copyright (c) 2020 Noah Husby
 * Sledgehammer [Bungeecord] - DialogScene.java
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

package com.noahhusby.sledgehammer.dialogs.scenes;

import com.google.common.collect.Maps;
import com.noahhusby.sledgehammer.dialogs.components.IDialogComponent;
import com.noahhusby.sledgehammer.dialogs.toolbars.DefaultToolbar;
import com.noahhusby.sledgehammer.dialogs.toolbars.IToolbar;
import com.noahhusby.sledgehammer.dialogs.DialogHandler;
import com.noahhusby.sledgehammer.chat.ChatHelper;
import com.noahhusby.sledgehammer.chat.TextElement;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.CommandSender;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import java.util.Map;

public abstract class DialogScene implements IDialogScene {

    protected IDialogComponent currentComponent;
    protected CommandSender sender;

    protected final DialogHandler dialogHandler = DialogHandler.getInstance();
    private final Map<Integer, IDialogComponent> components = Maps.newHashMap();

    @Override
    public void init(CommandSender commandSender) {
        this.sender = commandSender;
        this.currentComponent = components.get(0);
        dialogHandler.progressDialog(this, false);
        onInitialization();
    }

    protected void registerComponent(IDialogComponent d) {
        components.put(components.size(), d);
    }

    @Override
    public IDialogComponent getCurrentComponent() {
        return currentComponent;
    }

    @Override
    public CommandSender getCommandSender() {
        return sender;
    }

    @Override
    public void onMessage(String m) {
        for(Map.Entry<String, String> x : getToolbar().getTools().entrySet()) {
            if(x.getKey().equals(m.toLowerCase().trim())) {
                onToolbarAction(m);
                return;
            }
        }

        progressDialog(m);
    }

    @Override
    public IToolbar getToolbar() {
        return new DefaultToolbar();
    }

    @Override
    public void onToolbarAction(String m) {
        if(m.equals("exit")) {
            dialogHandler.discardDialog(this);
            getCommandSender().sendMessage();
            getCommandSender().sendMessage(ChatHelper.makeTextComponent(new TextElement("Exited the dialog!", ChatColor.RED)));
        }
    }

    @Override
    public boolean isAdmin() {
        return false;
    }

    @Override
    public TextElement[] getTitle() {
        return null;
    }

    @Override
    public void onComponentFinish() { }

    @Override
    public void onInitialization() { }

    public void progressDialog(String m) {
        progressDialog(m, false);
    }

    public void progressDialog(String m, boolean override) {
        if(currentComponent.validateResponse(m)) {
            currentComponent.setValue(m);
            if(currentComponent.isManual() && !override) {
                onComponentFinish();
                return;
            }
        } else {
            dialogHandler.progressDialog(this, true);
            return;
        }

        int x = components.size();
        for(Map.Entry<Integer, IDialogComponent> c : components.entrySet()) {
            if(c.getValue() == currentComponent) {
                if((c.getKey() + 1) >= x) {
                    dialogHandler.discardDialog(this);
                    onFinish();
                    return;
                }
                currentComponent = components.get(c.getKey()+1);
                dialogHandler.progressDialog(this, false);
                return;
            }
        }
        dialogHandler.progressDialog(this, false);
    }

    public String getValue(String key) {
        for(Map.Entry<Integer, IDialogComponent> s : components.entrySet()) {
            if(s.getValue().getKey().toLowerCase().equals(key.toLowerCase())) {
                return s.getValue().getValue();
            }
        }

        return "";
    }
}
