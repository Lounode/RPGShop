package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.event.Listener;

public abstract class RPGShopCommand implements Listener {
    protected final RPGShop plugin;
    public boolean playerOnly;
    public String command;
    public RPGShopCommand(RPGShop plugin) {
        this.plugin = plugin;
    }
    public abstract boolean onCommand(CommandSender sender, Command command, String label, String[] args);
}
