package github.lounode.rpgshop.utils;

import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * comments:<br>
 * 更大的ItemStack，{@link #amount} 支持使用 {@link Integer} 作为更大的参数<br>
 * 同时在Yaml里序列化时对比 {@link Map} 拥有对人类更友好的可读性<br>
 * 支持单一 {@link ItemStack} 生成Pair和手动指定数量的构造方法
 *
 * @author Lounode
 * @date 2024/05/15
 */
public class ItemPair implements Cloneable, ConfigurationSerializable {
    private ItemStack itemStack;
    private int amount;

    /**
     * comments:<br>
     * 根据物品组自动生成数量
     *
     * @param itemStack
     * @author Lounode
     * @date 2024/05/18
     */
    public ItemPair(ItemStack itemStack) {
        this.amount = itemStack.getAmount();
        this.itemStack = itemStack;

        itemStack.setAmount(1);
    }

    /**
     * comments:<br>
     * 手动指定数量，原本的物品数量将被忽略
     *
     * @param itemStack
     * @param amount    数量
     * @author Lounode
     * @date 2024/05/18
     */
    public ItemPair(ItemStack itemStack, int amount) {
        this.amount = amount;
        this.itemStack = itemStack;

        itemStack.setAmount(1);
    }

    /**
     * comments:<br>
     * 获取物品组
     *
     * @return {@link ItemStack }
     * @author Lounode
     * @date 2024/05/18
     */
    public ItemStack getItemStack() {
        return itemStack;
    }

    /**
     * comments:<br>
     * 设置物品组
     *
     * @param itemStack
     * @author Lounode
     * @date 2024/05/18
     */
    public void setItemStack(ItemStack itemStack) {
        this.itemStack = itemStack;
    }

    /**
     * comments:<br>
     * 获取物品数量
     *
     * @return int 物品数量
     * @author Lounode
     * @date 2024/05/18
     */
    public int getAmount() {
        return amount;
    }

    /**
     * comments:<br>
     * 设置物品数量
     *
     * @param amount 新物品数量
     * @author Lounode
     * @date 2024/05/18
     */
    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("item", getItemStack());
        result.put("amount", getAmount());
        return result;
    }

    public static ItemPair deserialize(Map<String, Object> args) {
        ItemStack itemStack = (ItemStack) args.get("item");
        int amount = (int) args.get("amount");

        ItemPair result = new ItemPair(itemStack, amount);
        return result;
    }
    @Override
    public ItemPair clone() {
        ItemStack newItemStack = this.itemStack.clone();
        int newAmount = this.amount;
        ItemPair result = new ItemPair(newItemStack, newAmount);

        return result;
    }
}
