package github.lounode.rpgshop.gui.guis;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.gui.Button;
import github.lounode.rpgshop.gui.ButtonClickEvent;
import github.lounode.rpgshop.gui.MultiPageInventory;
import github.lounode.rpgshop.shop.Shop;
import github.lounode.rpgshop.shop.Trade;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectExpLevel;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectItemStacks;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectMoney;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectPlayerPoints;
import github.lounode.rpgshop.utils.VaultAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EditorTrade {
    private Trade trade;
    public EditorTrade (Trade trade) {
        this.trade = trade;
    }
    public void open(Player editor) {
        MultiPageInventory editTradeGUI = new MultiPageInventory(RPGShop.getInstance().guiManager, 54,
                RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.TITLE_NEW",
                        String.valueOf(trade.getSaleSlot()), trade.getShop().getTitle()), false);
        //Init
        ItemStack fill = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);//GRAY
        ItemMeta glassMeta = fill.getItemMeta();
        glassMeta.setDisplayName("§r");
        fill.setItemMeta(glassMeta);

        Button fillBtn = new Button(fill);

        for (int i = 0; i < 54; i++) {
            editTradeGUI.setButton(i, fillBtn);
        }
        //Buttons
        //Save
        ItemStack skinSave = new ItemStack(Material.SLIME_BALL);
        ItemMeta saveMeta = skinSave.getItemMeta();
        saveMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_SAVE"));
        skinSave.setItemMeta(saveMeta);

        Button save = new Button(skinSave);
        save.addClickListener(this::btnEventSave);
        editTradeGUI.setButton(53, save);
        //Cancel
        ItemStack skinCancel = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = skinCancel.getItemMeta();
        cancelMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_CANCEL"));
        skinCancel.setItemMeta(cancelMeta);

        Button cancel = new Button(skinCancel);
        cancel.addClickListener(this::btnEventCancel);
        editTradeGUI.setButton(52, cancel);
        //Info
        ItemStack skinInfo = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = skinInfo.getItemMeta();
        infoMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_INFO"));
        skinInfo.setItemMeta(infoMeta);

        Button info = new Button(skinInfo);
        editTradeGUI.setButton(13, info);
        //Need
        ItemStack skinNeed = new ItemStack(Material.CHEST);
        ItemMeta needMeta = skinNeed.getItemMeta();
        needMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_NEED"));

        needMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        TradeObjectItemStacks itemRequire = trade.getRequire(TradeObjectItemStacks.class);
        List<String> itemStackLore = new ArrayList<>();
        itemStackLore.add("§r");
        if (itemRequire == null) {
            itemStackLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_ITEM_REQUIRE_UNSET"));
        } else {
            needMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
            itemStackLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_ITEM_REQUIRE"));

        }
        needMeta.setLore(itemStackLore);

        skinNeed.setItemMeta(needMeta);

        Button need = new Button(skinNeed);
        need.addClickListener(this::btnEventEditNeedItems);
        editTradeGUI.setButton(10, need);
        //Reward
        ItemStack skinReward = new ItemStack(Material.WORKBENCH);
        ItemMeta rewardMeta = skinReward.getItemMeta();
        rewardMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_REWARD"));

        rewardMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        TradeObjectItemStacks itemReward = trade.getRequire(TradeObjectItemStacks.class);
        List<String> itemLore = new ArrayList<>();
        itemLore.add("§r");
        if (itemReward == null) {
            itemLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_ITEM_REWARD_UNSET"));
        } else {
            rewardMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
            itemLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_ITEM_REWARD"));

        }
        rewardMeta.setLore(itemLore);

        skinReward.setItemMeta(rewardMeta);

        Button reward = new Button(skinReward);
        reward.addClickListener(this::btnEventEditRewardItems);
        editTradeGUI.setButton(16, reward);

        // Need-Coin
        if (RPGShop.getInstance().isVault()) {
            ItemStack coinItem = new ItemStack(Material.GOLD_NUGGET);
            ItemMeta coinMeta = coinItem.getItemMeta();
            coinMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            coinMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_NEED_COIN"));

            TradeObjectMoney moneyRequire = trade.getRequire(TradeObjectMoney.class);
            List<String> moneyLore = new ArrayList<>();
            moneyLore.add("§r");
            if (moneyRequire == null) {
                moneyLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_MONEY_UNSET"));
            } else {
                coinMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
                moneyLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_MONEY",
                        String.valueOf(moneyRequire.getMoney()),
                        VaultAPI.getCurrencyNamePlural()
                ));
            }

            coinMeta.setLore(moneyLore);
            coinItem.setItemMeta(coinMeta);

            Button needCoinButton = new Button(coinItem);
            needCoinButton.addClickListener(this::btnEventEditMoney);
            editTradeGUI.setButton(29, needCoinButton);
        }

        // Need-Points
        if (RPGShop.getInstance().isPlayerPoints()) {
            ItemStack pointsItem = new ItemStack(Material.DIAMOND);
            ItemMeta pointsMeta = pointsItem.getItemMeta();
            pointsMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_NEED_POINTS"));

            TradeObjectPlayerPoints pointsRequire = trade.getRequire(TradeObjectPlayerPoints.class);
            List<String> pointsLore = new ArrayList<>();
            pointsLore.add("§r");
            if (pointsRequire == null) {
                pointsLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_POINTS_UNSET"));
            } else {
                pointsMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
                pointsLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_POINTS",
                        String.valueOf(pointsRequire.getPoints()),
                        VaultAPI.getCurrencyNamePlural()
                ));
            }
            pointsMeta.setLore(pointsLore);

            pointsItem.setItemMeta(pointsMeta);

            Button needPointsButton = new Button(pointsItem);
            needPointsButton.addClickListener(this::btnEventEditPoints);
            editTradeGUI.setButton(33, needPointsButton);
        }


        // Need-Exp
        ItemStack expItem = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta expMeta = expItem.getItemMeta();
        expMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_NEED_EXP"));

        TradeObjectExpLevel expRequire = trade.getRequire(TradeObjectExpLevel.class);
        List<String> expLore = new ArrayList<>();
        expLore.add("§r");
        if (expRequire == null) {
            expLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_EXP_UNSET"));
        } else {
            expMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
            expLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_EXP",
                    String.valueOf(expRequire.getExp()),
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_EXP_TYPE_EXP")
            ));
        }
        expMeta.setLore(expLore);

        expItem.setItemMeta(expMeta);

        Button needExpButton = new Button(expItem);
        needExpButton.addClickListener(this::btnEventEditExp);
        editTradeGUI.setButton(31, needExpButton);

        // IsBuy
        ItemStack buyItem = new ItemStack(Material.EMERALD);
        ItemMeta buyMeta = buyItem.getItemMeta();
        buyMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_IS_BUY"));
        buyMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (trade.canBuy()) {
            buyMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
        }

        List<String> buyLore = new ArrayList<>();
        buyLore.add("§r");
        if (trade.canBuy()) {
            buyLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_CAN_BUY") +
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_STATUS_ENABLED")
            );
        } else {
            buyLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_CAN_BUY") +
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_STATUS_DISABLED")
            );
        }
        buyMeta.setLore(buyLore);

        buyItem.setItemMeta(buyMeta);

        Button isBuyButton = new Button(buyItem);
        isBuyButton.addClickListener(this::btnEventSwitchCanBuy);
        editTradeGUI.setButton(37, isBuyButton);

        // IsSell
        ItemStack sellItem = new ItemStack(Material.GOLD_INGOT);
        ItemMeta sellMeta = sellItem.getItemMeta();
        sellMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_IS_SELL"));
        sellMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (trade.canSell()) {
            sellMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
        }

        List<String> sellLore = new ArrayList<>();
        sellLore.add("§r");
        if (trade.canSell()) {
            sellLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_CAN_SELL") +
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_STATUS_ENABLED")
            );
        } else {
            sellLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_CAN_SELL") +
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_STATUS_DISABLED")
            );
        }
        sellMeta.setLore(sellLore);

        sellItem.setItemMeta(sellMeta);

        Button isSellButton = new Button(sellItem);
        isSellButton.addClickListener(this::btnEventSwitchCanSell);
        editTradeGUI.setButton(39, isSellButton);

        // IsInfinityTrade
        ItemStack infinityItem = new ItemStack(Material.ENCHANTED_BOOK);
        if (!trade.canInfinity()) {
            infinityItem = new ItemStack(Material.BOOK);
        }
        ItemMeta infinityMeta = infinityItem.getItemMeta();
        //infinityMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
        infinityMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_IS_INFINITY_TRADE"));
        infinityMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (trade.canInfinity()) {
            infinityMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
        }

        List<String> infinityLore = new ArrayList<>();
        infinityLore.add("§r");
        if (trade.canInfinity()) {
            infinityLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_CAN_INFINITY") +
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_STATUS_ENABLED")
            );
        } else {
            infinityLore.add(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_CAN_INFINITY") +
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_STATUS_DISABLED")
            );
        }
        infinityMeta.setLore(infinityLore);

        infinityItem.setItemMeta(infinityMeta);

        Button isInfinityButton = new Button(infinityItem);
        isInfinityButton.addClickListener(this::btnEventSwitchCanInfinity);
        editTradeGUI.setButton(41, isInfinityButton);
        //API
        ItemStack apiItem = new ItemStack(Material.PISTON_BASE);
        ItemMeta apiMeta = apiItem.getItemMeta();
        apiMeta.setDisplayName(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.BUTTON_API"));
        apiItem.setItemMeta(apiMeta);

        Button apiButton = new Button(apiItem);
        editTradeGUI.setButton(43, apiButton);




        editTradeGUI.open(editor);
    }

    private boolean btnEventEditExp(ButtonClickEvent event) {
        TradeObjectExpLevel require = trade.getRequire(TradeObjectExpLevel.class);
        if (require == null) {
            require = new TradeObjectExpLevel(0);
            trade.addRequire(require);
        }
        require.edit(event.getPlayer());
        return true;
    }

    private boolean btnEventEditPoints(ButtonClickEvent event) {
        TradeObjectPlayerPoints require = trade.getRequire(TradeObjectPlayerPoints.class);
        if (require == null) {
            require = new TradeObjectPlayerPoints(0);
            trade.addRequire(require);
        }
        require.edit(event.getPlayer());
        return true;
    }

    private boolean btnEventEditMoney(ButtonClickEvent event) {
        TradeObjectMoney require = trade.getRequire(TradeObjectMoney.class);
        if (require == null) {
            require = new TradeObjectMoney(0);
            trade.addRequire(require);
        }
        require.edit(event.getPlayer());
        return true;
    }
    private boolean btnEventCancel(ButtonClickEvent event) {
        String id = trade.getShop().getID();
        RPGShop.getInstance().shopManager.rollBackChanges();
        Shop shop = RPGShop.getInstance().shopManager.getShop(id);
        EditorShop editorShop = new EditorShop(shop);
        editorShop.open(event.getPlayer());

        return true;
    }
    private boolean btnEventSave(ButtonClickEvent event) {
        trade.getShop().addTrade(trade);
        RPGShop.getInstance().shopManager.saveShops();
        EditorShop editorShop = new EditorShop(trade.getShop());
        editorShop.open(event.getPlayer());
        return true;
    }
    private boolean btnEventEditNeedItems(ButtonClickEvent event) {
        TradeObjectItemStacks require = trade.getRequire(TradeObjectItemStacks.class);
        if (require == null) {
            require = new TradeObjectItemStacks();
            trade.addRequire(require);
        }
        require.edit(event.getPlayer());

        return true;
    }

    private boolean btnEventEditRewardItems(ButtonClickEvent event) {
        TradeObjectItemStacks reward = trade.getReward(TradeObjectItemStacks.class);
        if (reward == null) {
            reward = new TradeObjectItemStacks();
            trade.addReward(reward);
        }
        reward.edit(event.getPlayer());

        return true;
    }
    private boolean btnEventSwitchCanBuy(ButtonClickEvent event) {
        trade.setBuy(!trade.canBuy());
        open(event.getPlayer());
        return true;
    }

    private boolean btnEventSwitchCanSell(ButtonClickEvent event) {
        trade.setSell(!trade.canSell());
        open(event.getPlayer());
        return true;
    }
    private boolean btnEventSwitchCanInfinity(ButtonClickEvent event) {
        trade.setInfinity(!trade.canInfinity());
        open(event.getPlayer());
        return true;
    }
}
