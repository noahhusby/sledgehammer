package com.noahhusby.sledgehammer.gui.inventories.anvil;

public abstract class AnvilChild implements IAnvilChild {
    private CloseMode closeMode = CloseMode.EXIT;
    private AnvilController controller;
    private String text;

    public void setCloseMode(CloseMode closeMode) {
        this.closeMode = closeMode;
    }

    public CloseMode getCloseMode() {
        return closeMode;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getText() {
        return text;
    }


    public void setController(AnvilController controller) {
        this.controller = controller;
    }

    public AnvilController getController() {
        return controller;
    }

    public enum CloseMode {
        FINISH, EXIT, LEFT, RIGHT
    }
}

