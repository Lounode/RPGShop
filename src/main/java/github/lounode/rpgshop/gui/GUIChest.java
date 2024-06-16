package github.lounode.rpgshop.gui;

import github.lounode.rpgshop.utils.Formater;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;

public abstract class GUIChest implements Listener {
    public GUIManager manager;
    protected Inventory inventory;
    protected HashMap<Integer, Button> buttons;
    public static final InventoryHolder holder = () -> null;
    public GUIChest(GUIManager manager) {
        this.manager = manager;
        this.buttons = new HashMap<>();
        this.inventory = Bukkit.createInventory(holder, 54, getTitle());
        create();
        Bukkit.getPluginManager().registerEvents(this, manager.plugin);
    }
    public Inventory getInventory() {
        return inventory;
    }
    public void create() {

    }

    public void open(Player player) {
        player.openInventory(getInventory());
    }
    public void close() {

    }
    public String getTitle() {
        return Formater.FormatMessage("&4&lNULL");
    }

    public boolean denyAnyClick() {
        return true;
    }
    public boolean setButton(int slot, Button button) {
        if (slot > getInventory().getSize()) {
            throw new IndexOutOfBoundsException("Invalid slot: " + slot);
        }
        if (isButtonSlot(slot)) {
            throw new IllegalArgumentException("Slot " + slot + " is already occupied by another button.");
        }
        Inventory inv = getInventory();
        inv.setItem(slot, button.getSkin());
        buttons.put(slot, button);
        return true;
    }
    public Button getButton(int slot) {
        return buttons.get(slot);
    }
    public boolean isButtonSlot(int slot) {
        return getButton(slot) != null;
    }
    public void onInventoryClick(InventoryClickEvent event) {
        if (!event.isCancelled() && holder.equals(event.getInventory().getHolder())) {
            if (denyAnyClick()) {
                event.setCancelled(true);
            }
            int slot = event.getRawSlot();
            if (isButtonSlot(slot)) {
                event.setCancelled(true);
                Button button = getButton(slot);
                //button.click(event);
            }
        }
    }
    @EventHandler
    void onInventoryClickEvent(InventoryClickEvent event) {
        onInventoryClick(event);
    }


}
