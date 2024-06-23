package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.i18n.RPGI18N;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class RPGShopDeleteCommand extends RPGShopCommand{

    public RPGShopDeleteCommand(RPGShop plugin) {
        super(plugin);
        this.command = "delete";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = Arrays.copyOfRange(args, 1, args.length);
        if (args.length == 0) {
            sender.sendMessage(RPGI18N.MESSAGE_DELETE_HELP.get());
            return false;
        }
        String shopID = args[0];
        if (plugin.shopManager.getShop(shopID) == null) {
            sender.sendMessage(RPGI18N.DELETE_FAIL.get(args[0]));
            return false;
        }
        plugin.shopManager.deleteShop(shopID);
        sender.sendMessage(RPGI18N.DELETE_SUCCESS.get(args[0]));
        return true;
    }
}
