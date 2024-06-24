package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.i18n.RPGI18N;
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
        sender.sendMessage(RPGI18N.HELP_HEADER.get());
        sender.sendMessage(RPGI18N.HELP_CREATE.get());
        sender.sendMessage(RPGI18N.HELP_DELETE.get());
        if (sender instanceof Player) {
            //sender.sendMessage(RPGI18N.HELP_EDIT.get());
            sender.sendMessage(RPGI18N.HELP_EDITOR.get());
        }
        sender.sendMessage(RPGI18N.HELP_RENAME.get());
        //sender.sendMessage(RPGI18N.HELP_RESLOT.get());
        //sender.sendMessage(RPGI18N.HELP_RESIZE.get());
        sender.sendMessage(RPGI18N.HELP_OPEN.get());
        if (RPGShop.getInstance().isCitizens()) {
            sender.sendMessage(RPGI18N.HELP_BIND.get());
        }
        sender.sendMessage(RPGI18N.HELP_RELOAD.get());
        return true;
    }

}
