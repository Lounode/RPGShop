package github.lounode.rpgshop.datasource;

import github.lounode.rpgshop.shop.Shop;
import github.lounode.spearmintlib.bukkit.BukkitPlugin;
import lombok.Getter;

import java.util.List;

public abstract class DataSource {
    @Getter
    private BukkitPlugin<?> plugin;

    public DataSource(BukkitPlugin<?> plugin) {
        this.plugin = plugin;
    }

    public abstract void saveShops();
    public abstract void reloadShops();

    public abstract boolean addShop(Shop shop);
    public abstract boolean deleteShop(String id);
    public abstract void updateShop(Shop shop);

    public abstract Shop getShop(String id);
    public abstract List<Shop> getShops();
}
