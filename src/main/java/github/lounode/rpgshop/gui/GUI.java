package github.lounode.rpgshop.gui;

import github.lounode.rpgshop.gui.events.GUICloseCallback;
import github.lounode.rpgshop.gui.events.GUICloseEvent;
import github.lounode.rpgshop.gui.events.GUIOpenCallback;
import github.lounode.rpgshop.gui.events.GUIOpenEvent;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public abstract class GUI implements Listener {
    @Getter
    private GUIManager manager;
    private List<GUIOpenCallback> openCallbacks;
    private List<GUICloseCallback> closeCallbacks;
    @Getter
    protected Inventory inventory;
    @Setter
    @Getter
    private List<Slot> slots;
    private GUIHolder _holder;
    @Setter
    boolean denyAnyClick;
    @Setter
    boolean denyGUIClick;
    @Setter
    boolean denyInvClick;
    @Getter
    private String title;
    @Getter
    private int size;
    public GUI (GUIManager manager, int size, String title) {
        this.manager = manager;
        this._holder = new GUIHolder(this);
        this.slots = new ArrayList<>();
        this.size = size;
        this.title = title;
        this.denyAnyClick = true;

        this.openCallbacks = new ArrayList<>();
        this.closeCallbacks = new ArrayList<>();

        resetSlots();
        Bukkit.getPluginManager().registerEvents(this, manager.getPlugin());
    }

    public void open(Player player) {
        onOpen();
        inventory = Bukkit.createInventory(getHolder(), getNextMultipleOfNine(getSize()), title);

        for(int i = 0; i < getSize(); i++) {
            Slot slot = getSlots().get(i);
            ItemStack item = slot.getItemStack();
            if (item != null) {
                inventory.setItem(i, item);
            }
        }

        player.openInventory(inventory);
    }
    public int getNextMultipleOfNine(int number) {
        return (number % 9 == 0) ? number : ((number / 9) + 1) * 9;
    }

    public boolean addListener (GUIOpenCallback callback) {
        return this.openCallbacks.add(callback);
    }
    public boolean onOpen() {
        for(GUIOpenCallback callback : openCallbacks) {
            callback.execute(new GUIOpenEvent(this));
        }
        return true;
    }
    public boolean setButton(int slotIndex, Button button) {
        if (slotIndex > getSize()) {
            throw new IndexOutOfBoundsException("Invalid slot: " + slotIndex);
        }

        Slot slot = getSlots().get(slotIndex);
        slot.setButton(button);

        return true;

    }
    public boolean addListener (GUICloseCallback callback) {
        return this.closeCallbacks.add(callback);
    }
    public boolean onClose() {
        for(GUICloseCallback callback : closeCallbacks) {
            callback.execute(new GUICloseEvent(this));
        }
        return true;
    }
    public InventoryHolder getHolder() {
        return _holder;
    }

    public void resetSlots() {
        List<Slot> empty = new ArrayList<>();
        for (int i = 1; i <= getSize(); i++) {
            empty.add(new Slot());
        }
        setSlots(empty);
    }

}
