package github.lounode.rpgshop.utils.citizens;

import net.citizensnpcs.api.exception.NPCLoadException;
import net.citizensnpcs.api.trait.Trait;
import net.citizensnpcs.api.trait.TraitName;
import net.citizensnpcs.api.util.DataKey;

@TraitName("rpgshop")
public class RPGShopTrait extends Trait {
    private String shopID;

    public RPGShopTrait(){
        super("rpgshop");
    }
    public RPGShopTrait(String shopID) {
        super("rpgshop");
        this.shopID = shopID;
    }
    public String getShopID() {
        return shopID;
    }

    @Override
    public void save(DataKey key) {
        key.setString("shopID", shopID);
    }

    @Override
    public void load(DataKey key) throws NPCLoadException {
        String shopID = key.getString("shopID");
        this.shopID = shopID;
    }
}
