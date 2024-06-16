package github.lounode.rpgshop.utils;

import org.bukkit.Material;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BookMeta;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * comments:<br>
 * 一些给 {@link Item} 和 {@link ItemStack} 用的静态方法
 *
 * @author Lounode
 * @date 2024/05/15
 */
public class ItemUtil {
    /**
     * comments:<br>
     * 增强 {@link ItemMeta#getDisplayName}<br>
     * 在无自定义名称的情况下返回大写内建ID<br>
     * 对于成书在无自定义名称的情况下特定的返回其标题 {@link BookMeta#getTitle()}
     *
     * @param itemStack {@link ItemStack}
     * @return {@link String } 物品名
     * @author Lounode
     * @date 2024/05/15
     */
    public static String getDisplayName(ItemStack itemStack) {
        String itemDisplayName = itemStack.getItemMeta().getDisplayName();
        if (itemDisplayName == null) {
            itemDisplayName = itemStack.getType().name();
            //Book Title
            if (itemStack.getType() == Material.WRITTEN_BOOK) {
                BookMeta meta = (BookMeta) itemStack.getItemMeta();
                itemDisplayName = meta.getTitle();
            }
        }
        return itemDisplayName;
    }
}
