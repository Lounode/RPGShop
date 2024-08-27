package github.lounode.rpgshop.shop.tradeobjects;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.chat.ChatHandle;
import github.lounode.rpgshop.gui.guis.EditorTrade;
import github.lounode.rpgshop.shop.Trade;
import github.lounode.rpgshop.shop.TradeType;
import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.black_ixx.playerpoints.manager.LocaleManager;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * comments:<br>
 * 交易物件 - 玩家点数(Player Points)<br>
 * 来源: Player Points
 *
 * @author Lounode
 * @date  2024/05/14
 */
public class TradeObjectPlayerPoints extends TradeObject{
    private final PlayerPoints pointsPlugin = PlayerPoints.getInstance();
    private final PlayerPointsAPI pointsAPI = pointsPlugin.getAPI();
    private final LocaleManager localeManager = pointsPlugin.getManager(LocaleManager.class);
    public TradeObjectPlayerPoints (int points) {
        setPoints(points);
    }
    private int points;

    /**
     * comments:<br>
     * 获取所需的点数
     *
     * @return int 所需点数
     * @author Lounode
     * @date 2024/05/14
     */
    public int getPoints() {
        return points;
    }


    /**
     * comments:<br>
     * 设置所需的点数
     *
     * @param points 新的点数
     * @author Lounode
     * @date 2024/05/14
     */
    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public boolean executeOutput(Player player) {
        return pointsAPI.give(player.getUniqueId(), getPoints());
    }

    @Override
    public boolean executeInput(Player player) {
        return pointsAPI.take(player.getUniqueId(), getPoints());
    }

    @Override
    public boolean checkCanTrade(TradeType type, Player player) {
        String pointsNamePlural = localeManager.getCurrencyName(getPoints());
        switch (type) {
            case BUY:
                return true;
            case SELL:
                if (getPoints() > pointsAPI.look(player.getUniqueId())) {
                    player.sendMessage(RPGShop.getInstance().getI18N("message.not_enough_points",
                            getPoints(),
                            pointsNamePlural,
                            pointsAPI.look(player.getUniqueId()),
                            pointsNamePlural
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
        int point = getPoints();
        String currencyNamePlural = localeManager.getCurrencyName(point);

        String messages;
        if (point <= pointsAPI.look(viewer.getUniqueId())) {
            messages = RPGShop.getInstance().getI18N("gui.info.tradeobj_pass",currencyNamePlural, point);
        } else {
            messages = RPGShop.getInstance().getI18N("gui.info.tradeobj_deny",currencyNamePlural, point);
        }

        if (checkIsDisplayMode(trade.canBuy(), trade.canSell(), type) || forceNorm) {
            messages = RPGShop.getInstance().getI18N("gui.info.tradeobj_norm",currencyNamePlural, point);
        }

        result.add(messages);
        return result;
    }


    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> result = new HashMap<>(16);
        result.put("points", getPoints());
        return result;
    }
    public static TradeObjectPlayerPoints deserialize(Map<String, Object> args) {
        int points = (int) args.get("points");
        TradeObjectPlayerPoints result = new TradeObjectPlayerPoints(points);
        return result;
    }
    @Override
    public TradeObjectPlayerPoints clone() {
        TradeObjectPlayerPoints tradeObj = (TradeObjectPlayerPoints) super.clone();

        tradeObj.points = this.points;

        return tradeObj;
    }
    public void edit(Player player) {
        player.sendMessage(RPGShop.getInstance().getI18N("message.input_chat",
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
        int points = handle.getInt(0);

        setPoints(points);

        EditorTrade editorTrade = new EditorTrade(trade);
        editorTrade.open(player);
    }
}
