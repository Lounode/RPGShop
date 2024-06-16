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
        Player player = (Player) sender;

        player.sendMessage(plugin.configManager.getI18NMsg("COMMON.HELP_HEADER"));
        player.sendMessage(plugin.configManager.getI18NMsg("COMMON.HELP_CREATE"));
        player.sendMessage(plugin.configManager.getI18NMsg("COMMON.HELP_DELETE"));
        player.sendMessage(plugin.configManager.getI18NMsg("COMMON.HELP_EDIT"));
        player.sendMessage(plugin.configManager.getI18NMsg("COMMON.HELP_EDITOR"));
        player.sendMessage(plugin.configManager.getI18NMsg("COMMON.HELP_RENAME"));
        player.sendMessage(plugin.configManager.getI18NMsg("COMMON.HELP_RELOAD"));
        return true;
    }

}
