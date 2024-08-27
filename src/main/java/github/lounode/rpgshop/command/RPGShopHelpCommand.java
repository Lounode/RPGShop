package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RPGShopHelpCommand extends RPGShopCommand{

    public RPGShopHelpCommand(RPGShop plugin) {
        super(plugin);
        this.command = "help";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        sender.sendMessage(plugin.getI18N("common.help_header"));
        sender.sendMessage(plugin.getI18N("common.help_create"));
        sender.sendMessage(plugin.getI18N("common.help_delete"));
        if (sender instanceof Player) {
            //sender.sendMessage(RPGI18N.HELP_EDIT.get());
            sender.sendMessage(plugin.getI18N("common.help_editor"));
        }
        sender.sendMessage(plugin.getI18N("common.help_rename"));
        //sender.sendMessage(RPGI18N.HELP_RESLOT.get());
        //sender.sendMessage(RPGI18N.HELP_RESIZE.get());
        sender.sendMessage(plugin.getI18N("common.help_open"));
        if (RPGShop.getInstance().isCitizens()) {
            sender.sendMessage(plugin.getI18N("common.help_bind"));
        }
        sender.sendMessage(plugin.getI18N("common.help_reload"));
        return true;
    }

}
