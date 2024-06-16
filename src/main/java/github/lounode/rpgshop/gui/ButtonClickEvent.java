package github.lounode.rpgshop.gui;

import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;

public class ButtonClickEvent extends InventoryClickEvent {
    public ButtonClickEvent(InventoryClickEvent event, GUI gui, Button button) {
        super(event.getView(), event.getSlotType(), event.getRawSlot(), event.getClick(), event.getAction());
        this.gui = gui;
        this.button = button;
    }
    public ButtonClickEvent(InventoryView view, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action) {
        super(view, type, slot, click, action);
    }

    public ButtonClickEvent(InventoryView view, InventoryType.SlotType type, int slot, ClickType click, InventoryAction action, int key) {
        super(view, type, slot, click, action, key);
    }
    private GUI gui;
    private Button button;

    public GUI getGUI() {
        return gui;
    }
    public Player getPlayer() {
        return (Player) getView().getPlayer();
    }
    public Button getButton() {
        return button;
    }
    public int getRealSlotIndex () {
        if (!(getGUI() instanceof MultiPageInventory)) {
            return getRawSlot();
        }
        MultiPageInventory inv = (MultiPageInventory) getGUI();
        int index = (inv.getPage() - 1) * 45 + getRawSlot();
        return index;
    }
}
