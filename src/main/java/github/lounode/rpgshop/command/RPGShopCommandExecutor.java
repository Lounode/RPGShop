package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.i18n.RPGI18N;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class RPGShopCommandExecutor implements CommandExecutor {
    private final RPGShop plugin;
    private List<RPGShopCommand> subCommands = new ArrayList<>();
    public RPGShopCommandExecutor(RPGShop plugin) {
        this.plugin = plugin;
        this.subCommands.add(new RPGShopHelpCommand(plugin));
        this.subCommands.add(new RPGShopReloadCommand(plugin));
        this.subCommands.add(new RPGShopCreateCommand(plugin));
        this.subCommands.add(new RPGShopDeleteCommand(plugin));
        this.subCommands.add(new RPGShopRenameCommand(plugin));
        this.subCommands.add(new RPGShopEditorCommand(plugin));
        this.subCommands.add(new RPGShopTestCommand(plugin));

        for(RPGShopCommand command : subCommands) {
            Bukkit.getPluginManager().registerEvents(command, plugin);
        }
    }


    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            args = new String[]{"help"};
        }
        for(RPGShopCommand subcommand: subCommands) {
            if (!Objects.equals(subcommand.command, args[0])) {
                continue;
            }
            if (subcommand.playerOnly && !(sender instanceof Player)) {
                sender.sendMessage(RPGI18N.NO_CONSOLE.get());
                return false;
            }
            if (subcommand.onCommand(sender, command, label, args)) {
                return true;
            } else {
                return false;
            }
        }
        sender.sendMessage(RPGI18N.NO_CMD.get(args[0]));
        return false;
    }
}
