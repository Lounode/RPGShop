package github.lounode.rpgshop.gui;

import github.lounode.rpgshop.RPGShop;
import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.InventoryHolder;

import java.util.HashMap;

public class GUIManager {
    @Getter
    private RPGShop plugin;
    private HashMap<Player, HashMap<InventoryHolder, GUIChest>> guis = new HashMap<>();

    public void onEnable(RPGShop plugin) {
        this.plugin = plugin;
    }

    public void show(Player player, GUIChest gui) {
        Inventory inv = gui.getInventory();
        gui.open(player);

        guis.put(player, new HashMap<>());
        guis.get(player).put(inv.getHolder(), gui);

    }
    public void reopen(Player player) {

    }
    public boolean back() {
        return false;
    }
    public GUIChest getGUI (Player player, InventoryHolder holder) {
        return null;
    }

    public GUIChest create(GUIChest guiChest) {
        return guiChest;
    }
}
