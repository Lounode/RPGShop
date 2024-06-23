package github.lounode.rpgshop.gui.guis;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.gui.Button;
import github.lounode.rpgshop.gui.ButtonClickEvent;
import github.lounode.rpgshop.gui.MultiPageInventory;
import github.lounode.rpgshop.gui.events.GUICloseEvent;
import github.lounode.rpgshop.i18n.RPGI18N;
import github.lounode.rpgshop.shop.TradeType;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectItemStacks;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class EditorTradeObjectItemStacks {
    private TradeObjectItemStacks tradeObj;
    public EditorTradeObjectItemStacks(TradeObjectItemStacks tradeObj) {
        this.tradeObj = tradeObj;
    }
    public void open (Player player) {
        MultiPageInventory inv = new MultiPageInventory(
                RPGShop.getInstance().guiManager,
                45,
                RPGI18N.GUI_TITLE_EDIT_ITEMS.get(),
                true
        );
        inv.setDenyAnyClick(false);
        // 创建返回按钮
        ItemStack backButton = new ItemStack(Material.BARRIER);
        ItemMeta backMeta = backButton.getItemMeta();
        backMeta.setDisplayName(RPGI18N.TRADE_BUTTON_BACK.get());
        backButton.setItemMeta(backMeta);

        Button back = new Button(backButton);
        back.addClickListener(this::btnEventBackToTradeEditor);
        inv.setFunctionButton(4, back);

        inv.addListener(this::listenEventCloseInventory);


        for(int i = 0; i < tradeObj.getItemStacks().size(); i++) {
            ItemStack item = tradeObj.getItemStacks().get(i);
            inv.getSlots().get(i).setItem(item);
        }

        inv.open(player);
    }
    private boolean btnEventBackToTradeEditor(ButtonClickEvent event) {
        saveItems(event.getInventory());
        TradeObjectItemStacks require = tradeObj.getTrade().getRequire(TradeObjectItemStacks.class);
        EditorTrade editor;
        if (require == tradeObj) {
            editor = new EditorTrade(tradeObj.getTrade(), TradeType.BUY);
        } else {
            editor = new EditorTrade(tradeObj.getTrade(), TradeType.SELL);
        }



        editor.open(event.getPlayer());
        return true;
    }
    private boolean listenEventCloseInventory(GUICloseEvent event) {
        saveItems(event.getGUI().getInventory());
        return true;
    }
    private void saveItems(Inventory inventory) {
        List<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < 45; i++) {
            ItemStack item = inventory.getItem(i);
            if (item != null) {
                items.add(item);
            }
        }
        tradeObj.setItemStacks(items);
    }

}
