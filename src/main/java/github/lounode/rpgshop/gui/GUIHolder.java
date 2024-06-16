package github.lounode.rpgshop.gui;

import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

public class GUIHolder implements InventoryHolder {
    GUI gui;
    public GUI getGUI() {
        return this.gui;
    }
    @Override
    public Inventory getInventory() {
        return null;
    }
    public GUIHolder (GUI gui) {
        this.gui = gui;
    }
}
