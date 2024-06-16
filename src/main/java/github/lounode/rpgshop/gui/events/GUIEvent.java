package github.lounode.rpgshop.gui.events;

import github.lounode.rpgshop.gui.GUI;

public class GUIEvent {
    GUI gui;
    public GUIEvent(GUI gui) {
        this.gui = gui;
    }

    public GUI getGUI() {
        return gui;
    }
}
