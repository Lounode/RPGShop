package github.lounode.rpgshop.shop;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.i18n.RPGI18N;
import github.lounode.rpgshop.shop.tradeobjects.TradeObject;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectItemStacks;
import org.bukkit.Material;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class Trade implements Cloneable, ConfigurationSerializable {
    private RPGShop plugin;
    private Shop shop;
    //private ItemStack item;
    private int slot = -1;
    private List<TradeObject> requires = new ArrayList<>();
    private List<TradeObject> rewards = new ArrayList<>();

    public void setBuy(boolean buy) {
        this.buy = buy;
    }

    public void setSell(boolean sell) {
        this.sell = sell;
    }

    public void setInfinity(boolean infinity) {
        this.infinity = infinity;
    }

    private boolean buy;
    private boolean sell;
    private boolean infinity;

    public Trade(int slot) {
        this.slot = slot;
        this.plugin = RPGShop.getInstance();
        this.buy = true;
        this.sell = true;
        this.infinity = true;
    }
    public void setShop(Shop shop) {
        this.shop = shop;
    }
    public boolean canBuy () { return buy;}
    public boolean canSell () { return sell;}
    public boolean canInfinity () {return infinity;}

    public ItemStack getShowDisplayItem(Player viewer) {
        ItemStack displayItem = getDisplayItem();

        ItemMeta displayItemMeta = displayItem.getItemMeta();
        List<String> itemOriginLore = displayItemMeta.getLore();
        if (itemOriginLore == null) {
            itemOriginLore = new ArrayList<>();
        }

        //Footer
        List<String> additionalFooter = new ArrayList<>();
        additionalFooter.add("§r");

        if (canBuy() && canSell()) {
            additionalFooter.add(RPGI18N.FOOTER_REQUIRE.get());
        } else if (canBuy() && !canSell()) {
            additionalFooter.add(RPGI18N.FOOTER_REQUIRE_BUY_ONLY.get());
        } else if (!canBuy() && canSell()) {
            additionalFooter.add(RPGI18N.FOOTER_REQUIRE_SELL_ONLY.get());
        } else {
            additionalFooter.add(RPGI18N.FOOTER_REQUIRE.get());
        }

        for (TradeObject require : getRequires()) {
            List<String> message = require.getFooters(TradeType.SELL,this, viewer);
            additionalFooter.addAll(message);
        }

        if (canBuy() && canSell()) {
            additionalFooter.add(RPGI18N.FOOTER_REWARD.get());
        } else if (canBuy() && !canSell()) {
            additionalFooter.add(RPGI18N.FOOTER_REWARD_BUY_ONLY.get());
        } else if (!canBuy() && canSell()) {
            additionalFooter.add(RPGI18N.FOOTER_REWARD_SELL_ONLY.get());
        } else {
            additionalFooter.add(RPGI18N.FOOTER_REWARD.get());
        }

        for (TradeObject reward : getRewards()) {
            List<String> message = reward.getFooters(TradeType.BUY,this, viewer);
            additionalFooter.addAll(message);
        }
        additionalFooter.add("§r");
        if (canBuy() && canSell()) {
            additionalFooter.add(RPGI18N.FOOTER_TUTORIAL.get());
        } else if (canBuy() && !canSell()) {
            additionalFooter.add(RPGI18N.FOOTER_TUTORIAL_BUY_ONLY.get());
        } else if (!canBuy() && canSell()) {
            additionalFooter.add(RPGI18N.FOOTER_TUTORIAL_SELL_ONLY.get());
        } else {
            additionalFooter.add(RPGI18N.FOOTER_TUTORIAL_DENY.get());
        }


        itemOriginLore.addAll(additionalFooter);
        displayItemMeta.setLore(itemOriginLore);
        displayItem.setItemMeta(displayItemMeta);
        return displayItem;
    }
    public ItemStack getEditDisplayItem(Player viewer) {
        ItemStack displayItem = getDisplayItem();

        ItemMeta displayItemMeta = displayItem.getItemMeta();
        List<String> itemOriginLore = displayItemMeta.getLore();
        if (itemOriginLore == null) {
            itemOriginLore = new ArrayList<>();
        }

        List<String> additionalFooter = new ArrayList<>();
        additionalFooter.add("§r");
        additionalFooter.add(RPGI18N.FOOTER_EDIT.get());
        additionalFooter.add("§r");
        additionalFooter.add(RPGI18N.FOOTER_REQUIRE.get());
        for (TradeObject require : getRequires()) {
            List<String> message = require.getFooters(TradeType.SELL,this, viewer, true);
            additionalFooter.addAll(message);
        }
        additionalFooter.add(RPGI18N.FOOTER_REWARD.get());
        for (TradeObject reward : getRewards()) {
            List<String> message = reward.getFooters(TradeType.BUY,this, viewer, true);
            additionalFooter.addAll(message);
        }



        itemOriginLore.addAll(additionalFooter);
        displayItemMeta.setLore(itemOriginLore);
        displayItem.setItemMeta(displayItemMeta);

        return displayItem;
    }
    public ItemStack getDisplayItem () {
        ItemStack displayItem = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);//YELLOW
        TradeObjectItemStacks rewardItemstack = getReward(TradeObjectItemStacks.class);
        if (rewardItemstack != null && rewardItemstack.getItemStacks().size() > 0) {
            displayItem = rewardItemstack.getItemStacks().get(0).clone();
        }
        return displayItem;
    }
    public int getSaleSlot() {
        return slot;
    }


    public <T extends TradeObject> T getRequire(Class<T> clazz) {
        for (TradeObject require : getRequires()) {
            if (clazz.isInstance(require)) {
                return clazz.cast(require);
            }
        }
        return null;
    }
    public <T extends TradeObject> T getReward(Class<T> clazz) {
        for (TradeObject reward : getRewards()) {
            if (clazz.isInstance(reward)) {
                return clazz.cast(reward);
            }
        }
        return null;
    }
    public List<TradeObject> getRequires() {
        return requires;
    }
    public List<TradeObject> getRewards() {
        return rewards;
    }
    public boolean addRequire(TradeObject require) {
        require.setTrade(this);
        return requires.add(require);
    }
    public boolean addReward(TradeObject reward) {
        reward.setTrade(this);
        return rewards.add(reward);
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new LinkedHashMap<String, Object>();
        result.put("slot", getSaleSlot());
        result.put("buy", canBuy());
        result.put("sell", canSell());
        result.put("infinity", canInfinity());
        result.put("requires", getRequires());
        result.put("rewards", getRewards());

        return result;
    }
    public static Trade deserialize(Map<String, Object> args) {
        int slot = (int)args.get("slot");

        Trade trade = new Trade(slot);
        trade.buy = (Boolean) args.get("buy");
        trade.sell = (Boolean) args.get("sell");
        trade.infinity = (Boolean) args.get("infinity");

        if (args.containsKey("requires")) {
            Object raw = args.get("requires");
            //Check is 'Trade' List
            if (raw instanceof List && !((List)raw).isEmpty() && ((List)raw).get(0) instanceof TradeObject) {
                List<TradeObject> requires = (List<TradeObject>) args.get("requires");

                for (TradeObject require : requires) {
                    trade.addRequire(require);
                }
            }
        }
        if (args.containsKey("rewards")) {
            Object raw = args.get("rewards");
            //Check is 'Trade' List
            if (raw instanceof List && !((List)raw).isEmpty() && ((List)raw).get(0) instanceof TradeObject) {
                List<TradeObject> rewards = (List<TradeObject>) args.get("rewards");

                for (TradeObject reward : rewards) {
                    trade.addReward(reward);
                }
            }
        }

        return trade;
    }
    @Override
    public Trade clone() {
        try {
            Trade trade = (Trade) super.clone();
            trade.shop = this.shop;

            trade.slot = this.slot;
            trade.buy = this.buy;
            trade.sell = this.sell;

            List<TradeObject> newRequires = new ArrayList<>();
            for(TradeObject require : requires) {
                TradeObject newRequire = require.clone();
                newRequire.setTrade(trade);
                newRequires.add(newRequire);
            }
            trade.requires = newRequires;

            List<TradeObject> newRewards = new ArrayList<>();
            for(TradeObject reward : rewards) {
                TradeObject newReward = reward.clone();
                newReward.setTrade(trade);
                newRewards.add(newReward);
            }
            trade.rewards = newRewards;

            trade.buy = this.buy;
            trade.sell = this.sell;
            trade.infinity = this.infinity;

            return trade;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }

    public Shop getShop() {
        return shop;
    }
}
