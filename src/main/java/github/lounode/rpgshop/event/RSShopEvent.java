package github.lounode.rpgshop.event;

import github.lounode.rpgshop.shop.Shop;

public class RSShopEvent extends RSEvent {

    private Shop shop;

    public RSShopEvent(Shop shop) {
        this.shop = shop;
    }

    public Shop getShop() {
        return shop;
    }


}
