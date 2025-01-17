package io.github.toberocat.core.utility.gui;

import io.github.toberocat.core.utility.ObjectPair;
import io.github.toberocat.core.utility.callbacks.Callback;
import io.github.toberocat.core.utility.gui.slot.Slot;

import java.util.ArrayList;
import java.util.List;

public class GUISettings {
    private boolean dragable;
    private boolean clickable;
    private boolean quitIcon;
    private Callback quitCallback;
    private List<ObjectPair<Integer, Slot>> extraSlots;

    public GUISettings() {
        this.dragable = false;
        this.clickable = false;
        this.quitIcon = false;
        extraSlots = new ArrayList<>();
    }

    public Callback getQuitCallback() {
        return quitCallback;
    }

    public List<ObjectPair<Integer, Slot>> getExtraSlots() {
        return extraSlots;
    }

    public GUISettings setQuitCallback(Callback quitCallback) {
        this.quitCallback = quitCallback;
        return this;
    }

    public boolean isQuitIcon() {
        return quitIcon;
    }

    public GUISettings setQuitIcon(boolean quitIcon) {
        this.quitIcon = quitIcon;
        return this;
    }

    public GUISettings setDragable(boolean dragable) {
        this.dragable = dragable;
        return this;
    }

    public GUISettings setClickable(boolean clickable) {
        this.clickable = clickable;
        return this;
    }

    public boolean isDragable() {
        return dragable;
    }

    public boolean isClickable() {
        return clickable;
    }
}
