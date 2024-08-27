package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.gui.guis.GUIShop;
import github.lounode.rpgshop.shop.Shop;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RPGShopOpenCommand extends RPGShopCommand{
    public RPGShopOpenCommand(RPGShop plugin) {
        super(plugin);
        this.command = "open";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = Arrays.copyOfRange(args, 1, args.length);
        if (args.length < 1 || args.length > 2) {
            sender.sendMessage(plugin.getI18N("common.help_open"));
            return false;
        }
        if (!(sender instanceof Player) && args.length != 2) {
            sender.sendMessage(plugin.getI18N("common.help_open"));
            return false;
        }
        String id = args[0];
        Shop shop = plugin.shopManager.getShop(id);
        Player openPlayer = null;
        if (args.length == 2) {
            openPlayer = Bukkit.getPlayer(args[1]);
        }
        if (shop == null) {
            sender.sendMessage(plugin.getI18N("message.not_found_shop"));
            return false;
        }
        GUIShop gui = new GUIShop(shop);
        if (openPlayer != null) {
            gui.open(openPlayer);
        } else {
            gui.open((Player) sender);
        }

        return true;
    }
}
