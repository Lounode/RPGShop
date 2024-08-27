package github.lounode.rpgshop.shop;

import github.lounode.rpgshop.RPGShop;
import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShopManager {
    private RPGShop plugin;
    @Getter
    private List<Shop> shops = new ArrayList<>();
    private List<Shop> shopsBackup = new ArrayList<>();
    public void onEnable(RPGShop plugin) {
        this.plugin = plugin;
    }
    public void onDisable() {

    }
    public Shop createShop(CommandSender sender, String id, int row, String title) {
        if (getShop(id) != null) {
            return null;
        }
        Shop shop = new Shop(id, row, title);
        shops.add(shop);

        saveShops();
        return shop;
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
        if (getShop(id) == null) {
            return false;
        }
        Shop shopToDelete = getShop(id);
        shops.remove(shopToDelete);
        saveShops();
        //Remove file
        File saveFolder = new File(plugin.getDataFolder(), "shops");
        File trashBin = new File(saveFolder, "trash-bin");
        if (!trashBin.exists()) {
            trashBin.mkdirs();
        }
        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd-HH-mm-ss");
        Date date = new Date();
        String time = sdf.format(date);

        File shopFile = new File(saveFolder, id + ".yml");
        File shopFileTrash = new File(trashBin, id + time + ".yml");
        try {
            Files.move(shopFile.toPath(), shopFileTrash.toPath());
        } catch (IOException e) {
            e.printStackTrace();
        }

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
            try {
                FileConfiguration shopConfig = YamlConfiguration.loadConfiguration(new File(saveFolder, file.getName()));

                Shop shop = (Shop) shopConfig.get("Shop");

                loadedShops.add(shop);
            } catch (IllegalArgumentException e) {
                RPGShop.getInstance().getLogger().info("Loading error skipping shop: " + file.getName());
            }

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
