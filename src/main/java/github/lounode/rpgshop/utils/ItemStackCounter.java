package github.lounode.rpgshop.utils;

import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.*;


/**
 * comments:<br>
 * 一种用来存储大量 {@link ItemStack} 的类列表数据结构<br>
 * 支持对传入的 {@link ItemStack} 数组进行增删改查，并支持快速统计符合条件的 {@link ItemStack} 的数量
 *
 * @author Lounode
 * @date 2024/05/14
 */
public class ItemStackCounter implements Iterable<ItemPair>, Cloneable, ConfigurationSerializable {
    private final List<ItemPair> items = new ArrayList<>();
    private ItemStackCounter (List<ItemPair> items) {
        this.items.addAll(items);
    }

    public ItemStackCounter (ItemStack itemStack) {
        add(itemStack);
    }
    public ItemStackCounter (Inventory inventory) {
        add(inventory);
    }
    public ItemStackCounter (Inventory[] inventories) {
        add(inventories);
    }
    public ItemStackCounter (ItemStack[] itemStacks) {
        add(itemStacks);
    }
    public void add(ItemStack itemStack) {
        if (itemStack == null || itemStack.getType() == Material.AIR) {
            return;
        }

        int amount = itemStack.getAmount();
        ItemStack key = itemStack.clone();
        key.setAmount(1);

        ItemPair countItem = getSimilarKey(itemStack);
        if (countItem != null) {
            countItem.setAmount(countItem.getAmount() + amount);
        } else {
            items.add(new ItemPair(key, amount));
        }
    }
    public void add(ItemStack[] itemStacks) {
        for (ItemStack itemStack : itemStacks) {
            add(itemStack);
        }
    }
    public void add(Inventory inventory) {
        ItemStack[] itemStacks = inventory.getContents();
        add(itemStacks);
    }
    public void add(Inventory[] inventories) {
        for (Inventory inventory : inventories) {
            add(inventory);
        }
    }
    public boolean remove(ItemStack itemStack) {
        if (getSimilarKey(itemStack) != null) {
            for (ItemPair pair : items) {
                if (pair.getItemStack().isSimilar(itemStack)) {
                    return items.remove(pair);
                }
            }
        }
        return false;
    }
    public boolean delete(ItemStack itemStack, int amount) {
        ItemPair countItem = getSimilarKey(itemStack);
        if (countItem == null) {
            return false;
        }
        if (countItem.getAmount() < amount) {
            throw new IllegalArgumentException("Not enough amount");
        }
        countItem.setAmount(countItem.getAmount() - amount);
        if (countItem.getAmount() <= 0) {
            remove(itemStack);
        }
        return true;
    }

    /**
     * comments:<br>
     * 获取物品在其中的数量
     *
     * @param itemStack 待匹配
     * @return int 物品数量(可以超过64)
     * @author Lounode
     * @date 2024/05/18
     */
    public int getCount (ItemStack itemStack) {
        ItemPair countItem = getSimilarKey(itemStack);
        if (countItem == null) {
            return 0;
        }
        return countItem.getAmount();
    }

    /**
     * comments:<br>
     * 检查它是否被传入的另一个 {@link ItemStackCounter} 包含<br>
     * 即检查每个 {@link ItemStack} 是否在传入的 {@link ItemStackCounter} 中，并且数量是否足够。
     *
     * @param compareCounter 要比较的 {@link ItemStackCounter}
     * @return {@link Pair }<{@link Boolean }, {@link ItemStack }> - 如果当前对象被包含则返回 (true, null)，否则返回 (false, 缺少的第一个 {@link ItemStack})
     * @auther Lounode
     * @date 2024/05/18
     */
    public Pair<Boolean, ItemStack> isEnough(ItemStackCounter compareCounter) {
        for (ItemPair pair : items) {
            ItemStack itemStack = pair.getItemStack();
            int amount = pair.getAmount();

            ItemPair content = compareCounter.getSimilarKey(itemStack);
            if (content == null) {
                return new Pair<>(false, itemStack);
            }
            if (content.getAmount() >= amount) {
                continue;
            }
            return new Pair<>(false, itemStack);
        }
        return new Pair<>(true, null);
    }

    /**
     * comments:<br>
     * 匹配获取与输入相同的物品组的Pair
     *
     * @param itemStack 待匹配
     * @return {@link ItemPair }
     * @author Lounode
     * @date 2024/05/18
     */
    public ItemPair getSimilarKey (ItemStack itemStack) {
        for (ItemPair pair : items) {
            if (pair.getItemStack().isSimilar(itemStack)) {
                return pair;
            }
        }
        return null;
    }

    /**
     * comments:<br>
     * 获取有几类物品
     *
     * @return int 不同的物品数量
     * @author Lounode
     * @date 2024/05/18
     */
    public int size() {
        return items.size();
    }

    /**
     * comments:<br>
     * 获取实际含有几个物品组(依照上限64划分)
     *
     * @return int
     * @author Lounode
     * @date 2024/05/18
     */
    public int sizeItemStack() {
        return toList().size();
    }
    public void clear() {
        items.clear();
    }
    public boolean isEmpty() {
        return items.isEmpty();
    }

    /**
     * comments:<br>
     * 将其转换为一个物品组列表，大于64的物品自动生成多个物品组
     *
     * @return {@link List }<{@link ItemStack }>
     * @author Lounode
     * @date 2024/05/18
     */
    public List<ItemStack> toList() {
        List<ItemStack> result = new ArrayList<>();
        for (ItemPair pair : items) {
            ItemStack itemStack = pair.getItemStack();
            int amount = pair.getAmount();

            while (amount > 0) {
                int stackSize = Math.min(amount, itemStack.getMaxStackSize());
                ItemStack item = itemStack.clone();
                item.setAmount(stackSize);
                result.add(item);
                amount -= stackSize;
            }
        }
        return result;
    }

    /**
     * comments:<br>
     * 将其转换为一个物品组数组，大于64的物品自动生成多个物品组
     *
     * @return {@link ItemStack[] }
     * @author Lounode
     * @date 2024/05/18
     */
    public ItemStack[] toArray() {
        return toList().toArray(new ItemStack[0]);
    }

    @Override
    public Iterator<ItemPair> iterator() {
        return items.iterator();
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<>(16);
        result.put("items", items);

        return result;
    }

    public static ItemStackCounter deserialize(Map<String, Object> args) {
        List<ItemPair> item = (List<ItemPair>) args.get("items");
        ItemStackCounter result = new ItemStackCounter(item);
        return result;
    }
    @Override
    public ItemStackCounter clone() {
        List<ItemPair> newItems = new ArrayList<>();
        for (ItemPair pair : items) {
            newItems.add(pair.clone());
        }
        ItemStackCounter counter = new ItemStackCounter(newItems);

        return counter;
    }
}
