/**
 * Copy-Edited from SXAttribute
 * Author: Saukiya
 */

package github.lounode.rpgshop.utils;

import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.RegisteredServiceProvider;


/**
 * comments:<br>
 * 部分复制并修改于 Sakiya 的 SXAttribute 中的VaultAPI
 *
 * @author Saukiya、Lounode
 * @date 2024/05/18
 */
public class VaultAPI {
    private static Economy economy = null;

    /**
     * 初始化VaultAPI
     *
     * @throws NullPointerException NullPointerException
     */
    public static void setup() throws NullPointerException {
        RegisteredServiceProvider<Economy> registeredServiceProvider = Bukkit.getServicesManager().getRegistration(Economy.class);
        if (registeredServiceProvider == null) {
            throw new NullPointerException();
        }
        economy = registeredServiceProvider.getProvider();
    }

    /**
     * 获取玩家金币
     *
     * @param player OfflinePlayer
     * @return double
     */
    public static double get(OfflinePlayer player) {
        return economy.getBalance(player);
    }

    /**
     * 检查玩家是否拥有相应金币
     *
     * @param player OfflinePlayer
     * @param money  double
     * @return boolean
     */
    public static boolean has(OfflinePlayer player, double money) {
        return money <= get(player);
    }

    /**
     * 给予玩家金币
     *
     * @param player OfflinePlayer
     * @param money  double
     */
    public static void give(OfflinePlayer player, double money) {
        economy.depositPlayer(player, money);
    }

    /**
     * 扣取玩家金币
     *
     * @param player OfflinePlayer
     * @param money  double
     */
    public static void take(OfflinePlayer player, double money) {
        economy.withdrawPlayer(player, money);
    }

    /**
     * comments:<br>
     * 获取复数类型的通货符号
     *
     *
     * @return {@link String } (Vault默认: $, 具体内容参考经济插件配置)
     * @author Lounode
     * @date 2024/05/18
     */
    public static String getCurrencyNamePlural() {
        return economy.currencyNamePlural();
    }

    /**
     * comments:<br>
     * 获取单数类型的通货符号
     *
     * @return {@link String } (Vault默认: $, 具体内容参考经济插件配置)
     * @author Lounode
     * @date 2024/05/18
     */
    public static String getCurrencyNameSingular() {
        return economy.currencyNameSingular();
    }
}
