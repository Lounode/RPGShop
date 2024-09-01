package github.lounode.rpgshop.datasource;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.shop.Shop;
import github.lounode.spearmintlib.bukkit.BukkitPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class YAML extends DataSource {
    private List<Shop> shops = new ArrayList<>();

    public YAML(BukkitPlugin<?> plugin) {
        super(plugin);
    }

    @Override
    public void reloadShops() {
        File saveFolder = new File(getPlugin().getDataFolder(), "shops");
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
        getPlugin().getLogger().info(String.format("Successfully loaded %d shops.", loadedShops.size()));
    }

    @Override
    public void saveShops() {
        File saveFolder = new File(getPlugin().getDataFolder(), "shops");
        if (!saveFolder.exists()) {
            saveFolder.mkdirs();
        }
        for (Shop shop : shops) {
            File shopSave = new File(saveFolder, shop.getID() + ".yml");
            if (!shopSave.exists()) {
                getPlugin().saveResource("shop-template.yml", false);
                File template = new File(getPlugin().getDataFolder(), "shop-template.yml");
                template.renameTo(shopSave);
            }
            FileConfiguration shopConfigFile = YamlConfiguration.loadConfiguration(shopSave);

            shopConfigFile.set("Shop", shop);

            try {
                shopConfigFile.save(shopSave);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public boolean addShop(Shop shop) {
        if (getShop(shop.getID()) != null) {
            return false;
        }
        shops.add(shop);
        saveShops();
        return true;
    }

    @Override
    public boolean deleteShop(String id) {
        if (getShop(id) == null) {
            return false;
        }
        Shop shopToDelete = getShop(id);
        shops.remove(shopToDelete);
        saveShops();
        //Remove file
        File saveFolder = new File(getPlugin().getDataFolder(), "shops");
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
            return true;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public void updateShop(Shop shop) {
        if (getShop(shop.getID()) == null) {
            return;
        }
        shops.removeIf(s -> s.getID().equals(shop.getID()));
        shops.add(shop);
        saveShops();
    }

    @Override
    public Shop getShop(String id) {
        for(Shop shop : getShops()) {
            if (shop.getID().equals(id)) {
                return shop.clone();
            }
        }
        return null;
    }

    @Override
    public List<Shop> getShops() {
        return shops;
    }
}
