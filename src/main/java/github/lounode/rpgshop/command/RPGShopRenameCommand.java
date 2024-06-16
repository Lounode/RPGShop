package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
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
        String oldName = "null";
        String newName = args[1];
        if (false) {
            sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.RENAME_NOTFOUND", args[0]));
            return false;
        }
        sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.RENAME_SUCCESS", oldName, newName));
        return true;
    }
}
