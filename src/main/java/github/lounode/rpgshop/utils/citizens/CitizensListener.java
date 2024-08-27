package github.lounode.rpgshop.utils.citizens;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.gui.guis.EditorShop;
import github.lounode.rpgshop.gui.guis.GUIShop;
import github.lounode.rpgshop.shop.Shop;
import net.citizensnpcs.api.event.NPCRightClickEvent;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class CitizensListener implements Listener {
    public CitizensListener() {}
    @EventHandler
    private void citizensNpcRightClickEvent(NPCRightClickEvent event) {
        Player player = event.getClicker();
        NPC npc = event.getNPC();
        String shopID = CitizensUtil.GetShopID(npc);
        Shop shop = RPGShop.getInstance().getShopManager().getShop(shopID);

        if (player.isSneaking()) {
            if (player.hasPermission("rpgshop.admin")) {
                if (shop == null) {
                    openCreateGUI(player);
                } else {
                    openEditorGUI(shop, player);
                }
            }
        } else if (shop != null) {
            openShopGUI(shop, player);
        }
    }
    private void openShopGUI(Shop shop, Player player) {
        GUIShop guiShop = new GUIShop(shop);
        guiShop.open(player);
    }
    private void openCreateGUI(Player player) {
        player.sendMessage("Create");
    }
    private void openEditorGUI(Shop shop, Player player) {
        EditorShop editor = new EditorShop(shop);
        editor.open(player);
    }
}
