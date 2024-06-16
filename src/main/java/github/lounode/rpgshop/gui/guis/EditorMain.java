package github.lounode.rpgshop.gui.guis;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.gui.Button;
import github.lounode.rpgshop.gui.ButtonClickEvent;
import github.lounode.rpgshop.gui.MultiPageInventory;
import github.lounode.rpgshop.shop.Shop;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class EditorMain {

    public void open(Player player) {
        List<Shop> shops = RPGShop.getInstance().shopManager.getShops();
        //GUIHolder holder = new GUIHolder(RPGShop.getInstance().guiManager);
        MultiPageInventory inv = new MultiPageInventory(RPGShop.getInstance().guiManager, shops.size(),
                RPGShop.getInstance().configManager.getI18NMsg("SHOP.EDITOR_TITLE"), true);

        for(int i = 0; i < shops.size(); i++) {
            Shop shop = shops.get(i);

            ItemStack shopDefaultDisplay = new ItemStack(Material.SIGN);
            ItemMeta shopDefaultDisplayMeta = shopDefaultDisplay.getItemMeta();
            shopDefaultDisplayMeta.setDisplayName("§r"+shop.getTitle());
            List<String> lore = new ArrayList<>(Arrays.asList(
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.EDITOR_DISPLAY_ID", shop.getID()),
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.EDITOR_DISPLAY_TIME", "null"),
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.EDITOR_DISPLAY_ROWS", String.valueOf(shop.getRows())),
                    RPGShop.getInstance().configManager.getI18NMsg("SHOP.EDITOR_DISPLAY_AUTHOR", "null")
            ));
            shopDefaultDisplayMeta.setLore(lore);
            shopDefaultDisplay.setItemMeta(shopDefaultDisplayMeta);

            Button button = new Button(shopDefaultDisplay);
            button.addClickListener(this::btnEventEditShop);

            inv.setButton(i, button);
        }
        inv.open(player);
    }
    private boolean btnEventEditShop(ButtonClickEvent event) {
        Player player = event.getPlayer();
        int slot = event.getRealSlotIndex();
        List<Shop> shops = RPGShop.getInstance().shopManager.getShops();

        Shop shop = shops.get(slot);
        if (shop == null) {
            return false;
        }
        EditorShop editorShop = new EditorShop(shop);
        editorShop.open(player);
        return true;
    }
}
