package github.lounode.rpgshop.gui.guis;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.gui.Button;
import github.lounode.rpgshop.gui.ButtonClickEvent;
import github.lounode.rpgshop.gui.MultiPageInventory;
import github.lounode.rpgshop.shop.Shop;
import github.lounode.rpgshop.shop.Trade;
import github.lounode.rpgshop.shop.TradeType;
import github.lounode.rpgshop.shop.tradeobjects.*;
import github.lounode.rpgshop.utils.VaultAPI;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EditorTrade {
    private Trade trade;
    private TradeType editType;
    public EditorTrade (Trade trade) {
        this.trade = trade;
        this.editType = TradeType.BUY;
    }
    public EditorTrade (Trade trade, TradeType editType) {
        this.trade = trade;
        this.editType = editType;
    }
    public void open(Player editor) {
        MultiPageInventory editTradeGUI = new MultiPageInventory(
                RPGShop.getInstance().guiManager,
                54,
                RPGShop.getInstance().getI18N("gui.title.trade",
                        trade.getShop().getTitle(), trade.getSaleSlot()
                ),
                false
        );
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
        saveMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.trade_save"));
        skinSave.setItemMeta(saveMeta);

        Button save = new Button(skinSave);
        save.addClickListener(this::btnEventSave);
        editTradeGUI.setButton(53, save);
        //Cancel
        ItemStack skinCancel = new ItemStack(Material.BARRIER);
        ItemMeta cancelMeta = skinCancel.getItemMeta();
        cancelMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.trade_cancel"));
        skinCancel.setItemMeta(cancelMeta);

        Button cancel = new Button(skinCancel);
        cancel.addClickListener(this::btnEventCancel);
        editTradeGUI.setButton(52, cancel);
        //Info
        ItemStack skinInfo = new ItemStack(Material.SIGN);
        ItemMeta infoMeta = skinInfo.getItemMeta();
        infoMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.trade_info"));
        infoMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> infoLore = new ArrayList<>();
        infoLore.add("§r");
        if (this.editType == TradeType.BUY) {
            for(TradeObject tradeObject : trade.getRequires()) {
                infoLore.addAll(tradeObject.getFooters(TradeType.BUY, trade, editor, true));
            }
        } else {
            for(TradeObject tradeObject : trade.getRewards()) {
                infoLore.addAll(tradeObject.getFooters(TradeType.SELL, trade, editor, true));
            }
        }

        infoMeta.setLore(infoLore);
        skinInfo.setItemMeta(infoMeta);

        Button info = new Button(skinInfo);
        editTradeGUI.setButton(13, info);
        //Item
        ItemStack itemStacks = new ItemStack(Material.CHEST);
        ItemMeta itemStacksMeta = itemStacks.getItemMeta();
        itemStacksMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.trade_need"));
        if (this.editType == TradeType.SELL) {
            itemStacksMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.trade_reward"));
        }

        itemStacksMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> itemStackLore = new ArrayList<>();
        itemStackLore.add("§r");
        if (this.editType == TradeType.BUY) {
            TradeObjectItemStacks itemRequire = trade.getRequire(TradeObjectItemStacks.class);
            if (itemRequire == null) {
                itemStackLore.add(RPGShop.getInstance().getI18N("gui.info.item_require_unset"));
            } else {
                itemStacksMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
                itemStackLore.add(RPGShop.getInstance().getI18N("gui.info.item_require"));

                itemStackLore.addAll(itemRequire.getFooters(TradeType.BUY, trade, editor, true));
            }
        } else {
            TradeObjectItemStacks itemReward = trade.getReward(TradeObjectItemStacks.class);
            if (itemReward == null) {
                itemStackLore.add(RPGShop.getInstance().getI18N("gui.info.item_reward_unset"));
            } else {
                itemStacksMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
                itemStackLore.add(RPGShop.getInstance().getI18N("gui.info.item_reward"));

                itemStackLore.addAll(itemReward.getFooters(TradeType.SELL, trade, editor, true));
            }
        }


        itemStacksMeta.setLore(itemStackLore);

        itemStacks.setItemMeta(itemStacksMeta);

        Button need = new Button(itemStacks);
        need.addClickListener(this::btnEventEditNeedItems);
        editTradeGUI.setButton(22, need);
        // Coin
        if (RPGShop.getInstance().isVault()) {
            ItemStack coinItem = new ItemStack(Material.GOLD_NUGGET);
            ItemMeta coinMeta = coinItem.getItemMeta();
            coinMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
            coinMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.trade_need_coin"));

            List<String> moneyLore = new ArrayList<>();
            moneyLore.add("§r");

            if (this.editType == TradeType.BUY) {
                TradeObjectMoney moneyRequire = trade.getRequire(TradeObjectMoney.class);
                if (moneyRequire == null) {
                    moneyLore.add(RPGShop.getInstance().getI18N("gui.info.money_unset"));
                } else {
                    coinMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
                    moneyLore.add(RPGShop.getInstance().getI18N("gui.info.money",
                            moneyRequire.getMoney(),
                            VaultAPI.getCurrencyNamePlural()
                    ));
                }
            } else {
                TradeObjectMoney moneyReward = trade.getReward(TradeObjectMoney.class);
                if (moneyReward == null) {
                    moneyLore.add(RPGShop.getInstance().getI18N("gui.info.money_unset"));
                } else {
                    coinMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
                    moneyLore.add(RPGShop.getInstance().getI18N("gui.info.money",
                            moneyReward.getMoney(),
                            VaultAPI.getCurrencyNamePlural()
                    ));
                }
            }


            coinMeta.setLore(moneyLore);
            coinItem.setItemMeta(coinMeta);

            Button needCoinButton = new Button(coinItem);
            needCoinButton.addClickListener(this::btnEventEditMoney);
            editTradeGUI.setButton(20, needCoinButton);
        }

        // Points
        if (RPGShop.getInstance().isPlayerPoints()) {
            ItemStack pointsItem = new ItemStack(Material.DIAMOND);
            ItemMeta pointsMeta = pointsItem.getItemMeta();
            pointsMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.trade_need_points"));
            pointsMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);

            List<String> pointsLore = new ArrayList<>();
            pointsLore.add("§r");

            if (this.editType == TradeType.BUY) {
                TradeObjectPlayerPoints pointsRequire = trade.getRequire(TradeObjectPlayerPoints.class);
                if (pointsRequire == null) {
                    pointsLore.add(RPGShop.getInstance().getI18N("gui.info.points_unset"));
                } else {
                    pointsMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
                    pointsLore.add(RPGShop.getInstance().getI18N("gui.info.points",
                            pointsRequire.getPoints(),
                            VaultAPI.getCurrencyNamePlural()
                    ));
                }
            } else {
                TradeObjectPlayerPoints pointsReward = trade.getReward(TradeObjectPlayerPoints.class);
                if (pointsReward == null) {
                    pointsLore.add(RPGShop.getInstance().getI18N("gui.info.points_unset"));
                } else {
                    pointsMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
                    pointsLore.add(RPGShop.getInstance().getI18N("gui.info.points",
                            pointsReward.getPoints(),
                            VaultAPI.getCurrencyNamePlural()
                    ));
                }
            }

            pointsMeta.setLore(pointsLore);

            pointsItem.setItemMeta(pointsMeta);

            Button needPointsButton = new Button(pointsItem);
            needPointsButton.addClickListener(this::btnEventEditPoints);
            editTradeGUI.setButton(24, needPointsButton);
        }
        // Exp
        ItemStack expItem = new ItemStack(Material.EXP_BOTTLE);
        ItemMeta expMeta = expItem.getItemMeta();
        expMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.trade_need_exp"));

        String expNamePlural = RPGShop.getInstance().getI18N("gui.info.exp_type_exp");
        List<String> expLore = new ArrayList<>();
        expLore.add("§r");

        if (this.editType == TradeType.BUY) {
            TradeObjectExpLevel expRequire = trade.getRequire(TradeObjectExpLevel.class);
            if (expRequire == null) {
                expLore.add(RPGShop.getInstance().getI18N("gui.info.exp_unset"));
            } else {
                expMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
                expLore.add(RPGShop.getInstance().getI18N("gui.info.exp",
                        expRequire.getExp(),
                        expNamePlural
                ));
            }
        } else {
            TradeObjectExpLevel expReward = trade.getReward(TradeObjectExpLevel.class);
            if (expReward == null) {
                expLore.add(RPGShop.getInstance().getI18N("gui.info.exp_unset"));
            } else {
                expMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
                expLore.add(RPGShop.getInstance().getI18N("gui.info.exp",
                        expReward.getExp(),
                        expNamePlural
                ));
            }
        }

        expMeta.setLore(expLore);

        expItem.setItemMeta(expMeta);

        Button needExpButton = new Button(expItem);
        needExpButton.addClickListener(this::btnEventEditExp);
        editTradeGUI.setButton(30, needExpButton);

        //SwitchTradeType
        ItemStack switcher = new ItemStack(Material.BOOK);
        ItemMeta switcherMeta = switcher.getItemMeta();
        switcherMeta.setDisplayName(RPGShop.getInstance().getI18N("button.trade.switch"));
        switcherMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        List<String> switcherLore = new ArrayList<>();
        switcherLore.add("§r");
        String buyStatus = trade.canBuy() ? RPGShop.getInstance().getI18N("gui.info.status_enabled") : RPGShop.getInstance().getI18N("gui.info.status_disabled");
        String sellStatus = trade.canSell() ? RPGShop.getInstance().getI18N("gui.info.status_enabled") : RPGShop.getInstance().getI18N("gui.info.status_disabled");

        switcherLore.add(RPGShop.getInstance().getI18N("gui.info.switcher.buy", buyStatus));
        switcherLore.add(RPGShop.getInstance().getI18N("gui.info.switcher.sell", sellStatus));
        switcherMeta.setLore(switcherLore);
        switcher.setItemMeta(switcherMeta);

        Button switcherButton = new Button(switcher);
        switcherButton.addClickListener(this::btnEventSwitcher);
        editTradeGUI.setButton(40, switcherButton);

        // IsInfinityTrade
        /*
        ItemStack infinityItem = new ItemStack(Material.ENCHANTED_BOOK);
        if (!trade.canInfinity()) {
            infinityItem = new ItemStack(Material.BOOK);
        }
        ItemMeta infinityMeta = infinityItem.getItemMeta();
        //infinityMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
        infinityMeta.setDisplayName(RPGI18N.TRADE_BUTTON_IS_INFINITY_TRADE.get());
        infinityMeta.addItemFlags(ItemFlag.HIDE_ENCHANTS);
        if (trade.canInfinity()) {
            infinityMeta.addEnchant(Enchantment.ARROW_INFINITE , 1 , false);
        }

        List<String> infinityLore = new ArrayList<>();
        infinityLore.add("§r");
        if (trade.canInfinity()) {
            infinityLore.add(RPGI18N.INFO_CAN_INFINITY.get(RPGI18N.INFO_STATUS_ENABLED.get()));
        } else {
            infinityLore.add(RPGI18N.INFO_CAN_INFINITY.get(RPGI18N.INFO_STATUS_DISABLED.get()));
        }
        infinityMeta.setLore(infinityLore);

        infinityItem.setItemMeta(infinityMeta);

        Button isInfinityButton = new Button(infinityItem);
        isInfinityButton.addClickListener(this::btnEventSwitchCanInfinity);
        editTradeGUI.setButton(40, isInfinityButton);
        */
        //API
        ItemStack apiItem = new ItemStack(Material.PISTON_BASE);
        ItemMeta apiMeta = apiItem.getItemMeta();
        apiMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.trade_api"));
        apiItem.setItemMeta(apiMeta);

        Button apiButton = new Button(apiItem);
        editTradeGUI.setButton(32, apiButton);
        //Switch
        ItemStack switchItem = new ItemStack(Material.EMERALD_BLOCK);
        if (this.editType == TradeType.SELL) {
            switchItem = new ItemStack(Material.REDSTONE_BLOCK);
        }
        ItemMeta switchMeta = switchItem.getItemMeta();
        switchMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.switch_buy"));
        if (this.editType == TradeType.SELL) {
            switchMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.switch_sell"));
        }
        switchItem.setItemMeta(switchMeta);

        Button switchButton = new Button(switchItem);
        switchButton.addClickListener(this::btnEventSwitch);
        editTradeGUI.setButton(49, switchButton);






        editTradeGUI.open(editor);
    }

    private boolean btnEventSwitcher(ButtonClickEvent event) {
        if (trade.canBuy() && trade.canSell()) {
            trade.setSell(false);
        } else if (trade.canBuy() && !trade.canSell()) {
            trade.setBuy(false);
            trade.setSell(true);
        } else if (!trade.canBuy() && trade.canSell()) {
            trade.setBuy(false);
            trade.setSell(false);
        } else if (!trade.canBuy() && !trade.canSell()) {
            trade.setBuy(true);
            trade.setSell(true);
        }
        open(event.getPlayer());
        return true;
    }

    private boolean btnEventSwitch(ButtonClickEvent event) {
        TradeType toType = this.editType == TradeType.BUY ? TradeType.SELL : TradeType.BUY;
        this.editType = toType;
        open(event.getPlayer());
        return true;
    }

    private boolean btnEventEditExp(ButtonClickEvent event) {
        if (this.editType == TradeType.BUY) {
            TradeObjectExpLevel require = trade.getRequire(TradeObjectExpLevel.class);
            if (require == null) {
                require = new TradeObjectExpLevel(0);
                trade.addRequire(require);
            }
            require.edit(event.getPlayer());
        } else {
            TradeObjectExpLevel reward = trade.getReward(TradeObjectExpLevel.class);
            if (reward == null) {
                reward = new TradeObjectExpLevel(0);
                trade.addReward(reward);
            }
            reward.edit(event.getPlayer());
        }

        return true;
    }

    private boolean btnEventEditPoints(ButtonClickEvent event) {
        if (this.editType == TradeType.BUY) {
            TradeObjectPlayerPoints require = trade.getRequire(TradeObjectPlayerPoints.class);
            if (require == null) {
                require = new TradeObjectPlayerPoints(0);
                trade.addRequire(require);
            }
            require.edit(event.getPlayer());
        } else {
            TradeObjectPlayerPoints reward = trade.getReward(TradeObjectPlayerPoints.class);
            if (reward == null) {
                reward = new TradeObjectPlayerPoints(0);
                trade.addReward(reward);
            }
            reward.edit(event.getPlayer());
        }


        return true;
    }

    private boolean btnEventEditMoney(ButtonClickEvent event) {
        if (this.editType == TradeType.BUY) {
            TradeObjectMoney require = trade.getRequire(TradeObjectMoney.class);
            if (require == null) {
                require = new TradeObjectMoney(0);
                trade.addRequire(require);
            }
            require.edit(event.getPlayer());
        } else {
            TradeObjectMoney reward = trade.getReward(TradeObjectMoney.class);
            if (reward == null) {
                reward = new TradeObjectMoney(0);
                trade.addReward(reward);
            }
            reward.edit(event.getPlayer());
        }

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
        trade.getShop().setLastEditor(event.getPlayer().getName());

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String editTime = sdf.format(date);

        trade.getShop().setLastEditTime(editTime);
        RPGShop.getInstance().shopManager.saveShops();
        EditorShop editorShop = new EditorShop(trade.getShop());
        editorShop.open(event.getPlayer());
        return true;
    }
    private boolean btnEventEditNeedItems(ButtonClickEvent event) {
        if (this.editType == TradeType.BUY) {
            TradeObjectItemStacks require = trade.getRequire(TradeObjectItemStacks.class);
            if (require == null) {
                require = new TradeObjectItemStacks();
                trade.addRequire(require);
            }
            require.edit(event.getPlayer());
        } else {
            TradeObjectItemStacks reward = trade.getReward(TradeObjectItemStacks.class);
            if (reward == null) {
                reward = new TradeObjectItemStacks();
                trade.addReward(reward);
            }
            reward.edit(event.getPlayer());
        }


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
