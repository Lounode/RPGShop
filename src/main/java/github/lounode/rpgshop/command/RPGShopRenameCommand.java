package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.shop.Shop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class RPGShopRenameCommand extends RPGShopCommand{

    public RPGShopRenameCommand(RPGShop plugin) {
        super(plugin);
        this.command = "rename";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = Arrays.copyOfRange(args, 1, args.length);
        if (args.length != 2) {
            sender.sendMessage(plugin.getI18N("common.help_rename"));
            return false;
        }
        String newName = args[1];
        String shopID = args[0];

        Shop shopToRename = plugin.shopManager.getShop(shopID);
        if (shopToRename == null) {
            sender.sendMessage(plugin.getI18N("message.rename_fail", shopID));
            return false;
        }
        sender.sendMessage(plugin.getI18N("message.rename_success", shopToRename.getTitle(), newName));
        shopToRename.setTitle(newName);
        plugin.shopManager.saveShops();
        return true;
    }
}
