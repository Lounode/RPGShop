package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.i18n.RPGI18N;
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
            sender.sendMessage(RPGI18N.HELP_RENAME.get());
            return false;
        }
        String newName = args[1];
        String shopID = args[0];

        Shop shopToRename = plugin.shopManager.getShop(shopID);
        if (shopToRename == null) {
            sender.sendMessage(RPGI18N.RENAME_FAIL.get(shopID));
            return false;
        }
        sender.sendMessage(RPGI18N.RENAME_SUCCESS.get(shopToRename.getTitle(), newName));
        shopToRename.setTitle(newName);
        plugin.shopManager.saveShops();
        return true;
    }
}
