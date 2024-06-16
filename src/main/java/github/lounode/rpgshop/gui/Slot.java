package github.lounode.rpgshop.gui;

import org.bukkit.inventory.ItemStack;

public class Slot {
    private Button button;
    private ItemStack item;
    public ItemStack getItemStack() {
        if (item != null) {
            return item;
        }
        if (button != null) {
            return button.getSkin();
        }
        return null;
    }
    public Slot (ItemStack item) {
        this.item = item;
    }
    public Slot () {}
    public Button getButton() {
        return button;
    }
    public boolean setButton(Button button) {
        this.button = button;
        return true;
    }
    public boolean setItem (ItemStack item) {
        this.item = item;
        return true;
    }
}
