package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
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
            sender.sendMessage(plugin.getI18N("message.delete_help"));
            return false;
        }
        String shopID = args[0];
        if (plugin.getShopManager().getShop(shopID) == null) {
            sender.sendMessage(plugin.getI18N("message.delete_fail", args[0]));
            return false;
        }
        plugin.getShopManager().deleteShop(shopID);
        sender.sendMessage(plugin.getI18N("message.delete_success"));
        return true;
    }
}
