package github.lounode.rpgshop.gui.guis;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.event.RSShopTradeEvent;
import github.lounode.rpgshop.gui.Button;
import github.lounode.rpgshop.gui.ButtonClickEvent;
import github.lounode.rpgshop.gui.MultiPageInventory;
import github.lounode.rpgshop.i18n.RPGI18N;
import github.lounode.rpgshop.shop.Shop;
import github.lounode.rpgshop.shop.Trade;
import github.lounode.rpgshop.shop.TradeType;
import github.lounode.rpgshop.shop.tradeobjects.TradeObject;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GUIShop {
    private Shop shop;
    public GUIShop (Shop shop) {
        this.shop = shop;
    }
    public void open(Player player) {
        MultiPageInventory shopInv = new MultiPageInventory(RPGShop.getInstance().guiManager, shop.getSize(), shop.getTitle(), false);

        List<Trade> trades = shop.getTrades();
        for (Trade trade : trades) {
            int slot = trade.getSaleSlot();
            if (slot > shop.getSize() || slot < 0) {
                RPGShop.getInstance().getLogger().severe(String.format("Invalid shop slot: %d", slot));
                continue;
            }
            ItemStack display = trade.getShowDisplayItem(player);

            Button tradeBtn = new Button(display);
            tradeBtn.addClickListener(this::btnEventExecuteTrade);
            shopInv.setButton(slot,tradeBtn);
        }

        shopInv.open(player);
    }
    private boolean btnEventExecuteTrade(ButtonClickEvent event) {
        Player player = event.getPlayer();
        int index = event.getRealSlotIndex();
        Trade trade = shop.getTrade(index);
        List<TradeObject> requires = trade.getRequires();
        List<TradeObject> rewards = trade.getRewards();

        TradeType tradeType = null;
        if (event.getClick() == ClickType.LEFT) {
            if (trade.canBuy() && !trade.canSell()) {
                tradeType = TradeType.BUY;
            } else if (!trade.canBuy() && trade.canSell()) {
                tradeType = TradeType.SELL;
            } else if (trade.canBuy() && trade.canSell()) {
                tradeType = TradeType.BUY;
            } else {
                tradeType = null;
            }
        } else if (event.getClick() == ClickType.RIGHT) {
            if (trade.canBuy() && trade.canSell()) {
                tradeType = TradeType.SELL;
            }
        }
        if (tradeType == null) {
            return false;
        }

        //Event
        RSShopTradeEvent e = new RSShopTradeEvent(shop, trade);
        Bukkit.getPluginManager().callEvent(e);
        if (e.isCancelled()) {
            String message = e.getMessage();
            player.sendMessage(message);
            return false;
        }
        //Execute Trade
        if (tradeType == TradeType.BUY) {

            for (TradeObject require : requires) {
                if (!require.checkCanTrade(TradeType.SELL, player)) {
                    return false;
                }
            }
            for (TradeObject reward : rewards) {
                if (!reward.checkCanTrade(TradeType.BUY, player)) {
                    return false;
                }
            }
            player.sendMessage(RPGI18N.MESSAGE_TRADE_SUCCESS.get());

            for(TradeObject require : requires) {
                require.executeInput(player);
            }

            for (TradeObject reward : rewards) {
                reward.executeOutput(player);
            }
        } else {
            for (TradeObject reward : rewards) {
                if (!reward.checkCanTrade(TradeType.SELL, player)) {
                    return false;
                }
            }
            player.sendMessage(RPGI18N.MESSAGE_TRADE_SUCCESS.get());

            for (TradeObject reward : rewards) {
                reward.executeInput(player);
            }
            for(TradeObject require : requires) {
                require.executeOutput(player);
            }
        }

        open(player);
        return true;
    }
}
