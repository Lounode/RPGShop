package github.lounode.rpgshop.shop;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.datasource.DataSource;
import github.lounode.rpgshop.datasource.YAML;
import lombok.Getter;

import java.util.List;

/**
 * RPG商店总管理
 *
 * @author Lounode
 * @date 2024/09/01
 */
public class ShopManager {
    @Getter
    private RPGShop plugin;
    private YAML ymlDataSource;
    private DataSource sqlDatasource;

    public ShopManager(RPGShop plugin) {
        this.plugin = plugin;
        ymlDataSource = new YAML(plugin);
    }

    /**
     * 增
     *
     * @param shop shop
     * @return boolean
     * @author Lounode
     * @date 2024/08/28
     */
    public boolean addShop(Shop shop) {
        return getCurrentDataSource().addShop(shop);
    }

    /**
     * 删
     *
     * @param id id
     * @return boolean
     * @author Lounode
     * @date 2024/08/28
     */
    public boolean deleteShop(String id) {
        return getCurrentDataSource().deleteShop(id);
    }

    /**
     * 改
     *
     * @param shop shop
     * @author Lounode
     * @date 2024/08/28
     */
    public void updateShop(Shop shop) {
        getCurrentDataSource().updateShop(shop);
    }

    /**
     * 查
     *
     * @param id id
     * @return {@link Shop }
     * @author Lounode
     * @date 2024/08/28
     */
    public Shop getShop(String id) {
        return getCurrentDataSource().getShop(id);
    }

    public List<Shop> getShops() {
        return getCurrentDataSource().getShops();
    }

    public void saveShops() {
        getCurrentDataSource().saveShops();
    }

    public boolean reloadShops() {
        getCurrentDataSource().reloadShops();

        return true;
    }
    public boolean rollBackChanges() {
        reloadShops();
        return true;
    }

    private DataSource getCurrentDataSource() {
        return ymlDataSource;
    }

}
