package github.lounode.rpgshop.shop.tradeobjects;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.gui.guis.EditorTradeObjectItemStacks;
import github.lounode.rpgshop.shop.Trade;
import github.lounode.rpgshop.shop.TradeType;
import github.lounode.rpgshop.utils.ItemPair;
import github.lounode.rpgshop.utils.ItemStackCounter;
import github.lounode.rpgshop.utils.ItemUtil;
import javafx.util.Pair;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * comments:<br>
 * 交易物件 - 物品(ItemStack)<br>
 * 来源: Minecraft
 * @author Lounode
 * @date 2024/05/14
 */
public class TradeObjectItemStacks extends TradeObject {
    private ItemStackCounter counter;
    public List<ItemStack> getItemStacks() {
        return counter.toList();
    }
    public void setItemStacks (List<ItemStack> itemStacks) {
        this.counter = new ItemStackCounter(itemStacks.toArray(new ItemStack[0]));
    }
    public ItemStackCounter getCounter () {
        return counter;
    }
    public TradeObjectItemStacks(List<ItemStack> itemStacks) {
        setItemStacks(itemStacks);
    }
    public TradeObjectItemStacks() {
        setItemStacks(new ArrayList<>());
    }


    public void edit (Player player) {
        EditorTradeObjectItemStacks editor = new EditorTradeObjectItemStacks(this);
        editor.open(player);
    }

    @Override
    public List<String> getFooters(TradeType type, Trade trade, Player viewer) {
        List<String> result = new ArrayList<>();

        List<Inventory> inventories = new ArrayList<>();
        inventories.add(viewer.getInventory());

        ItemStackCounter playerContents = new ItemStackCounter(inventories.toArray(new Inventory[0]));

        for (ItemPair pair : getCounter()) {
            ItemStack item = pair.getItemStack();
            int count = pair.getAmount();
            String itemDisplayName = ItemUtil.getDisplayName(item);

            String messages;
            if (playerContents.getCount(item) >= count) {
                messages = RPGShop.getInstance().configManager.getI18NMsg("SHOP.FOOTER.TRADEOBJ_PASS",
                        itemDisplayName,
                        String.valueOf(getCounter().getCount(item)));
            } else {
                messages = RPGShop.getInstance().configManager.getI18NMsg("SHOP.FOOTER.TRADEOBJ_DENY",
                        itemDisplayName,
                        String.valueOf(getCounter().getCount(item)));
            }

            if (checkIsDisplayMode(trade.canBuy(), trade.canSell(), type)) {
                messages = RPGShop.getInstance().configManager.getI18NMsg("SHOP.FOOTER.TRADEOBJ_NORM",
                        itemDisplayName,
                        String.valueOf(getCounter().getCount(item)));
            }
            result.add(messages);
        }

        return result;
    }

    @Override
    public boolean checkCanTrade(TradeType type, Player player) {
        int requireSlots = getItemStacks().size() + 1;

        switch (type){
            case BUY:
                if (!hasAvailableSlot(player, requireSlots)) {
                    player.sendMessage(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.MESSAGE_NEED_EMPTY_SLOTS",
                            String.valueOf(requireSlots)));
                    return false;
                }
                return true;
            case SELL:
                PlayerInventory inventory = player.getInventory();
                ItemStackCounter counterPlayer = new ItemStackCounter(inventory);
                Pair<Boolean, ItemStack> result = getCounter().isEnough(counterPlayer);
                if (!result.getKey()) {
                    ItemStack itemStack = result.getValue();
                    String itemDisplayName = ItemUtil.getDisplayName(itemStack);

                    player.sendMessage(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.MESSAGE_NOT_ENOUGH_CONTENTS",
                            String.valueOf(getCounter().getCount(itemStack)),
                            itemDisplayName,
                            String.valueOf(counterPlayer.getCount(itemStack))
                    ));
                    return false;
                }
                return true;
            default:
                return false;
        }
    }
    @Override
    public boolean executeInput (Player player) {
        PlayerInventory inventory = player.getInventory();

        for (ItemStack itemStack : getItemStacks()) {
            int requiredAmount = itemStack.getAmount();

            for (ItemStack item : inventory.getContents()) {
                if (item != null && item.isSimilar(itemStack)) {
                    int stackAmount = item.getAmount();

                    if (stackAmount >= requiredAmount) {
                        item.setAmount(stackAmount - requiredAmount);
                        break;
                    } else {
                        inventory.removeItem(item);
                        requiredAmount -= stackAmount;
                    }
                }
            }
        }
        return true;
    }
    @Override
    public boolean executeOutput (Player player) {
        PlayerInventory inventory = player.getInventory();

        for (ItemStack item : getItemStacks()) {
            inventory.addItem(item);
        }

        return true;
    }
    private boolean hasAvailableSlot(Player player, int needAvailableCount){
        Inventory inv = player.getInventory();
        int check = 0;
        for (int slot = 0; slot < 36; slot ++) {
            if(inv.getItem(slot) == null) {
                check++;
            }
        }
        return check >= needAvailableCount;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("counter", getCounter());

        return result;
    }

    public static TradeObjectItemStacks deserialize(Map<String, Object> args) {
        ItemStackCounter counter = (ItemStackCounter) args.get("counter");
        TradeObjectItemStacks result = new TradeObjectItemStacks();
        result.counter = counter;
        return result;
    }
    @Override
    public TradeObjectItemStacks clone() {
        TradeObjectItemStacks tradeObj = (TradeObjectItemStacks) super.clone();
        tradeObj.counter = this.counter.clone();


        return tradeObj;
    }
}
