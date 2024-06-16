package github.lounode.rpgshop.shop;

import github.lounode.rpgshop.RPGShop;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ShopManager {
    private RPGShop plugin;
    private List<Shop> shops = new ArrayList<>();
    private List<Shop> shopsBackup = new ArrayList<>();
    public void onEnable(RPGShop plugin) {
        this.plugin = plugin;
    }
    public void onDisable() {

    }
    public boolean createShop(CommandSender sender, String id, int row, String title) {
        if (getShop(id) != null) {
            return false;
        }
        Shop shop = new Shop(id, row, title);
        shops.add(shop);

        saveShops();
        return true;
    }
    public boolean rollBackChanges() {
        reloadFormDisk();
        return true;
    }
    public boolean saveShops() {
        File saveFolder = new File(plugin.getDataFolder(), "shops");
        if (!saveFolder.exists()) {
            saveFolder.mkdirs();
        }
        for (Shop shop : shops) {
            File shopSave = new File(saveFolder, shop.getID() + ".yml");
            if (!shopSave.exists()) {
                plugin.saveResource("shop-template.yml", false);
                File template = new File(plugin.getDataFolder(), "shop-template.yml");
                template.renameTo(shopSave);
            }
            FileConfiguration shopConfigFile = YamlConfiguration.loadConfiguration(shopSave);

            shopConfigFile.set("Shop", shop);

            try {
                shopConfigFile.save(shopSave);
            } catch (IOException e) {
                e.printStackTrace();
                return false;
            }
        }
        return true;
    }
    public boolean deleteShop(String id) {
        return true;
    }
    public boolean renameShop(String id, String newName) {
        return true;
    }
    public boolean reloadFormDisk() {
        File saveFolder = new File(plugin.getDataFolder(), "shops");
        if (!saveFolder.exists()) {
            saveFolder.mkdirs();
        }

        List<Shop> loadedShops = new ArrayList<>();
        File[] files = saveFolder.listFiles();
        for (File file: files) {
            if (!file.getName().endsWith(".yml")) {
                continue;
            }

            FileConfiguration shopConfig = YamlConfiguration.loadConfiguration(new File(saveFolder, file.getName()));

            Shop shop = (Shop) shopConfig.get("Shop");

            loadedShops.add(shop);
        }

        shops = loadedShops;
        refreshBackup();

        plugin.getLogger().info(String.format("Successfully loaded %d shops.", getStableShops().size()));

        return true;
    }
    private void refreshBackup() {
        List<Shop> backup = new ArrayList<>();

        for(Shop shop : shops) {
            backup.add(shop.clone());
        }
        shopsBackup = backup;
    }
    public List<Shop> getShops () {
        return shops;
    }
    public List<Shop> getStableShops () {
        return shopsBackup;
    }
    public Shop getShop(String id) {
        for(Shop shop : getShops()) {
            if (shop.getID().equals(id)) {
                return shop;
            }
        }
        return null;
    }

}
