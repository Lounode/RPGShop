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
        if (false) {
            sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.DELETE_NOTFOUND", args[0]));
            return false;
        }
        sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.CREATE_DELETE_SUCCESS", args[0]));
        return true;
    }
}
