package github.lounode.rpgshop.shop.tradeobjects;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.chat.ChatHandle;
import github.lounode.rpgshop.gui.guis.EditorTrade;
import github.lounode.rpgshop.shop.Trade;
import github.lounode.rpgshop.shop.TradeType;
import github.lounode.rpgshop.utils.VaultAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * comments:<br>
 * 交易物件 - 金币(Money)<br>
 * 来源: Vault
 *
 * @author Lounode
 * @date 2024/05/14
 */
public class TradeObjectMoney extends TradeObject{
    private double money;

    /**
     * comments:<br>
     * 设置所需的金币
     *
     * @param money 新的金币
     * @author Lounode
     * @date 2024/05/14
     */
    public void setMoney(double money) {
        this.money = money;
    }

    /**
     * comments:<br>
     * 获取所需的金币
     *
     * @return double 所需金币
     * @author Lounode
     * @date 2024/05/14
     */
    public double getMoney() {
        return money;
    }
    public TradeObjectMoney (double money) {
        this.money = money;
    }

    @Override
    public boolean executeOutput(Player player) {
        VaultAPI.give(player, getMoney());
        return true;
    }

    @Override
    public boolean executeInput(Player player) {
        VaultAPI.take(player, getMoney());
        return true;
    }

    @Override
    public boolean checkCanTrade(TradeType type, Player player) {
        String currencyNamePlural = VaultAPI.getCurrencyNamePlural();
        switch (type) {
            case BUY:
                return true;
            case SELL:
                if (!VaultAPI.has(player, getMoney())) {
                    player.sendMessage(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.MESSAGE_NOT_ENOUGH_MONEY",
                            String.valueOf(getMoney()),
                            currencyNamePlural,
                            String.valueOf(VaultAPI.get(player)),
                            currencyNamePlural
                    ));
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public List<String> getFooters(TradeType type, Trade trade, Player viewer) {
        List<String> result = new ArrayList<>();
        String currencyNamePlural = VaultAPI.getCurrencyNamePlural();
        double money = getMoney();

        String messages;
        if (VaultAPI.has(viewer, money)) {
            messages = RPGShop.getInstance().configManager.getI18NMsg("SHOP.FOOTER.TRADEOBJ_PASS",
                    currencyNamePlural,
                    String.valueOf(money));
        } else {
            messages = RPGShop.getInstance().configManager.getI18NMsg("SHOP.FOOTER.TRADEOBJ_DENY",
                    currencyNamePlural,
                    String.valueOf(money));
        }

        if (checkIsDisplayMode(trade.canBuy(), trade.canSell(), type)) {
            messages = RPGShop.getInstance().configManager.getI18NMsg("SHOP.FOOTER.TRADEOBJ_NORM",
                    currencyNamePlural,
                    String.valueOf(money));
        }

        result.add(messages);
        return result;
    }
    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("money", getMoney());
        return result;
    }

    public static TradeObjectMoney deserialize(Map<String, Object> args) {
        double money = (double) args.get("money");
        TradeObjectMoney result = new TradeObjectMoney(money);
        return result;
    }
    @Override
    public TradeObjectMoney clone() {
        TradeObjectMoney tradeObj = (TradeObjectMoney) super.clone();

        tradeObj.money = this.money;

        return tradeObj;
    }

    public void edit(Player player) {
        player.sendMessage(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.MESSAGE_INPUT_CHAT",
                "1",
                "Double"
                ));
        ChatHandle handle = new ChatHandle();
        handle.addCallback(this::chatHandleCallback);
        handle.startListener(player);
        player.closeInventory();
    }
    private void chatHandleCallback(ChatHandle handle) {
        Player player = handle.getPlayer();
        double money = handle.getDouble(0);
        setMoney(money);
        EditorTrade editorTrade = new EditorTrade(trade);
        editorTrade.open(player);

    }
}
