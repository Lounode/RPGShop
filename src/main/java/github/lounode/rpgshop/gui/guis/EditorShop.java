package github.lounode.rpgshop.gui.guis;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.gui.Button;
import github.lounode.rpgshop.gui.ButtonClickEvent;
import github.lounode.rpgshop.gui.MultiPageInventory;
import github.lounode.rpgshop.shop.Shop;
import github.lounode.rpgshop.shop.Trade;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

/**
 * comments:<br>
 * 编辑Shop的GUI
 *
 * @author Lounode
 * @date 2024/05/18
 */
public class EditorShop {
    private Shop shop;
    public EditorShop(Shop shop) {
        this.shop = shop;
    }
    public void open(Player player) {
        String title = RPGShop.getInstance().getI18N("gui.title.edit");
        MultiPageInventory editorOverview = new MultiPageInventory(RPGShop.getInstance().guiManager, shop.getSize(), title, true);

        //Init

        ItemStack createSkin = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta createMeta = createSkin.getItemMeta();
        createMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.create"));
        createSkin.setItemMeta(createMeta);

        Button create = new Button(createSkin);
        create.addClickListener(this::btnEventCreate);
        for (int i = 0; i < shop.getSize(); i++) {
            editorOverview.setButton(i, create);
        }

        for (Trade trade : shop.getTrades()) {
            int slot = trade.getSaleSlot();
            if (slot > shop.getSize() || slot < 0) {
                throw new ArrayIndexOutOfBoundsException(String.format("Invalid shop slot: %d", slot));
            }

            ItemStack editSkin = trade.getEditDisplayItem(player);
            Button edit = new Button(editSkin);
            edit.addClickListener(this::btnEventEdit);
            editorOverview.setButton(slot, edit);
        }


        //Buttons
        //Exit
        ItemStack exitButtonSkin = new ItemStack(Material.BARRIER);
        ItemMeta exitButtonMeta = exitButtonSkin.getItemMeta();
        exitButtonMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.exit"));
        exitButtonSkin.setItemMeta(exitButtonMeta);
        Button exitButton = new Button(exitButtonSkin);
        exitButton.addClickListener(this::btnEventReturn);
        editorOverview.setFunctionButton(4, exitButton);


        //Open
        ItemStack openButton = new ItemStack(Material.EYE_OF_ENDER);
        ItemMeta openButtonMeta = openButton.getItemMeta();
        openButtonMeta.setDisplayName(RPGShop.getInstance().getI18N("gui.button.open"));
        openButton.setItemMeta(openButtonMeta);

        Button open = new Button(openButton);
        open.addClickListener(this::btnEventOpen);
        editorOverview.setFunctionButton(6, open);


        editorOverview.open(player);
    }
    private boolean btnEventEdit(ButtonClickEvent event) {
        Player player = event.getPlayer();
        int index = event.getRealSlotIndex();
        Trade trade = shop.getTrade(index);
        EditorTrade editorTrade = new EditorTrade(trade);
        editorTrade.open(player);
        return true;
    }
    private boolean btnEventReturn (ButtonClickEvent event) {
        Player player = event.getPlayer();

        EditorMain gui = new EditorMain();
        gui.open(player);
        return true;
    }
    private boolean btnEventCreate (ButtonClickEvent event) {
        Player player = event.getPlayer();
        int index = event.getRealSlotIndex();
        Trade trade = new Trade(index);
        trade.setShop(shop);
        EditorTrade editorTrade = new EditorTrade(trade);
        editorTrade.open(player);
        return true;
    }
    private boolean btnEventOpen (ButtonClickEvent event) {
        Player player = event.getPlayer();
        GUIShop guiShop = new GUIShop(shop);
        guiShop.open(player);
        return true;
    }
}
