package github.lounode.rpgshop.gui;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;

public class Slot {
    @Setter
    @Getter
    private Button button;
    @Setter
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
}
