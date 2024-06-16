package github.lounode.rpgshop.shop.tradeobjects;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.chat.ChatHandle;
import github.lounode.rpgshop.gui.guis.EditorTrade;
import github.lounode.rpgshop.shop.Trade;
import github.lounode.rpgshop.shop.TradeType;
import github.lounode.rpgshop.utils.ExpAPI;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * comments:<br>
 * 交易物件 - 经验值(Exp)<br>
 * 来源: Minecraft
 *
 * @author Lounode
 * @date 2024/05/14
 */
public class TradeObjectExpLevel extends TradeObject{
    private int exp;

    /**
     * comments:<br>
     * 获取所需的经验值
     *
     * @return int 所需经验值
     * @author Lounode
     * @date 2024/05/14
     */
    public int getExp() {
        return exp;
    }

    /**
     * comments:<br>
     * 设置所需的经验值
     *
     * @param exp 新的经验值
     * @author Lounode
     * @date 2024/05/14
     */
    public void setExp(int exp) {
        this.exp = exp;
    }


    public TradeObjectExpLevel (int exp) {
        setExp(exp);
    }
    @Override
    public boolean executeOutput(Player player) {
        ExpAPI.setTotalExperience(player, ExpAPI.getTotalExperience(player) + getExp());
        return true;
    }

    @Override
    public boolean executeInput(Player player) {
        int origin = ExpAPI.getTotalExperience(player);
        ExpAPI.setTotalExperience(player, origin - getExp());
        return true;
    }

    @Override
    public boolean checkCanTrade(TradeType type, Player player) {
        switch (type) {
            case BUY:
                return true;
            case SELL:
                if (ExpAPI.getTotalExperience(player) <= getExp()) {
                    player.sendMessage(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.MESSAGE_NOT_ENOUGH_EXP",
                            String.valueOf(getExp()),
                            RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_EXP_TYPE_EXP"),
                            String.valueOf(ExpAPI.getTotalExperience(player)),
                            RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_EXP_TYPE_EXP")
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
        String levelPlural = RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.INFO_EXP_TYPE_EXP");
        int exp = getExp();

        String messages;
        if (ExpAPI.getTotalExperience(viewer) <= exp) {
            messages = RPGShop.getInstance().configManager.getI18NMsg("SHOP.FOOTER.TRADEOBJ_PASS",
                    levelPlural,
                    String.valueOf(exp));
        } else {
            messages = RPGShop.getInstance().configManager.getI18NMsg("SHOP.FOOTER.TRADEOBJ_DENY",
                    levelPlural,
                    String.valueOf(exp));
        }

        if (checkIsDisplayMode(trade.canBuy(), trade.canSell(), type)) {
            messages = RPGShop.getInstance().configManager.getI18NMsg("SHOP.FOOTER.TRADEOBJ_NORM",
                    levelPlural,
                    String.valueOf(exp));
        }
        result.add(messages);
        return result;
    }

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>();
        result.put("exp", getExp());
        return result;
    }
    public static TradeObjectExpLevel deserialize(Map<String, Object> args) {
        int points = (int) args.get("exp");
        TradeObjectExpLevel result = new TradeObjectExpLevel(points);
        return result;
    }
    @Override
    public TradeObjectExpLevel clone() {
        TradeObjectExpLevel tradeObj = (TradeObjectExpLevel) super.clone();

        tradeObj.exp = this.exp;

        return tradeObj;
    }
    public void edit(Player player) {
        player.sendMessage(RPGShop.getInstance().configManager.getI18NMsg("SHOP.TRADE.MESSAGE_INPUT_CHAT",
                "1",
                "Int"
        ));
        ChatHandle handle = new ChatHandle();
        handle.addCallback(this::chatHandleCallback);
        handle.startListener(player);
        player.closeInventory();
    }
    private void chatHandleCallback(ChatHandle handle) {
        Player player = handle.getPlayer();
        int exp = handle.getInt(0);
        setExp(exp);

        EditorTrade editorTrade = new EditorTrade(trade);
        editorTrade.open(player);
    }
}
