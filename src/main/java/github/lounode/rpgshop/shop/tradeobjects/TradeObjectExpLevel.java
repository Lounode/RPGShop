package github.lounode.rpgshop.shop.tradeobjects;

import github.lounode.rpgshop.chat.ChatHandle;
import github.lounode.rpgshop.gui.guis.EditorTrade;
import github.lounode.rpgshop.i18n.RPGI18N;
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
                    player.sendMessage(RPGI18N.MESSAGE_NOT_ENOUGH_EXP.get(
                            getExp(),
                            RPGI18N.INFO_EXP_TYPE_EXP.get(),
                            ExpAPI.getTotalExperience(player),
                            RPGI18N.INFO_EXP_TYPE_EXP.get()
                    ));
                    return false;
                }
                return true;
            default:
                return false;
        }
    }

    @Override
    public List<String> getFooters(TradeType type, Trade trade, Player viewer, boolean forceNorm) {
        List<String> result = new ArrayList<>();
        String levelPlural = RPGI18N.INFO_EXP_TYPE_EXP.get();
        int exp = getExp();

        String messages;
        if (ExpAPI.getTotalExperience(viewer) <= exp) {
            messages = RPGI18N.FOOTER_TRADEOBJ_PASS.get(levelPlural, exp);
        } else {
            messages = RPGI18N.FOOTER_TRADEOBJ_DENY.get(levelPlural, exp);
        }

        if (checkIsDisplayMode(trade.canBuy(), trade.canSell(), type) || forceNorm) {
            messages = RPGI18N.FOOTER_TRADEOBJ_NORM.get(levelPlural, exp);
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
        player.sendMessage(RPGI18N.MESSAGE_INPUT_CHAT.get(
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
