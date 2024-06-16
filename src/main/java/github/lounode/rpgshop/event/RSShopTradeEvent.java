package github.lounode.rpgshop.event;

import github.lounode.rpgshop.shop.Shop;
import github.lounode.rpgshop.shop.Trade;

public class RSShopTradeEvent extends RSShopEvent {
    private Trade trade;
    public RSShopTradeEvent(Shop shop, Trade trade){
        super(shop);
    }

    public Trade getTrade() {
        return trade;
    }
}
