package github.lounode.rpgshop.shop.tradeobjects;

import github.lounode.rpgshop.shop.Trade;
import github.lounode.rpgshop.shop.TradeType;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * comments:<br>
 * 交易物件<br>
 * 抽象化的交易输入与输出对象，用来封装交易物的中间件
 *
 * @author Lounode
 * @date 2024/05/16
 */
public abstract class TradeObject implements Cloneable, ConfigurationSerializable {
    protected Trade trade;

    public void setTrade(Trade trade) {
        this.trade = trade;
    }

    public Trade getTrade() {
        return trade;
    }

    /**
     * comments:<br>
     * 交易中执行给予玩家交易物的方法
     *
     * @param player 玩家
     * @return boolean 执行成功
     * @author Lounode
     * @date 2024/05/16
     */
    public abstract boolean executeOutput (Player player);

    /**
     * comments:<br>
     * 交易中执行从玩家处取得交易物的方法
     *
     * @param player 玩家
     * @return boolean 执行成功
     * @author Lounode
     * @date 2024/05/16
     */
    public abstract boolean executeInput (Player player);

    /**
     * comments:<br>
     * 用来检测交易是否满足，在交易时，会对两边所有的物件都进行检测<br>
     * 任意条件不满足即交易失败<br>
     * 对玩家抛出交易失败提示信息也应在此进行
     *
     * @param type 交易类型
     * @param player 玩家
     * @return boolean 交易检查是否通过
     * @author Lounode
     * @date 2024/05/16
     */
    public abstract boolean checkCanTrade(TradeType type, Player player);

    /**
     * comments:<br>
     * 获取该交易单元的需求满足情况，一般以如下情况输出<br><br>
     * SHOP.FOOTER.TRADEOBJ_PASS<br>'&a&l✔ &f{0} &f× {1}'<br>
     * 表示交易条件满足<br><br>
     * SHOP.FOOTER.TRADEOBJ_DENY<br>'&c&l✘ &f{0} &f× {1}'<br>
     * 表示交易条件不满足<br><br>
     * SHOP.FOOTER.TRADEOBJ_NORM<br>'&f&l➤ &f{0} &f× {1}'<br>
     * 表示交易条件不被检查或者仅做展示
     *
     * @param type   传入的交易类型
     * @param trade  所属的 {@link Trade}
     * @param viewer 当前玩家
     * @return {@link List }<{@link String }>
     * @author Lounode
     * @date 2024/05/16
     */
    public List<String> getFooters(TradeType type, Trade trade, Player viewer) {
        return getFooters(type, trade, viewer, false);
    }
    public abstract List<String> getFooters(TradeType type, Trade trade, Player viewer, boolean forceNorm);

    /**
     * comments:
     * 用来检测此处应该使用正常标记还是展示标记
     *
     * @param buy       能否购买
     * @param sell      能否售卖
     * @param tradeType 交易类型
     * @return boolean 是否启用展示标记
     * @author Lounode
     * @date 2024/05/14
     */
    protected boolean checkIsDisplayMode (boolean buy, boolean sell, TradeType tradeType) {
        if (buy && !sell && tradeType == TradeType.BUY) {
            return true;
        } else if (!buy && sell && tradeType == TradeType.SELL) {
            return true;
        } else if (!buy && !sell) {
            return true;
        }
        return false;
    }
    @Override
    public TradeObject clone() {
        try {
            TradeObject tradeObj = (TradeObject) super.clone();

            return tradeObj;
        } catch (CloneNotSupportedException e) {
            throw new Error(e);
        }
    }
}
