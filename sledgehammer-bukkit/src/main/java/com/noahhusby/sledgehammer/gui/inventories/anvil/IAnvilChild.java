package com.noahhusby.sledgehammer.gui.inventories.anvil;

import net.wesjd.anvilgui.AnvilGUI;

public interface IAnvilChild {
    AnvilGUI.Builder build(AnvilGUI.Builder builder);
    void onLeftItemClick();
    void onRightItemClick();
    void onFinish();
}
