package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.event.RSReloadEvent;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RPGShopReloadCommand extends RPGShopCommand{
    public RPGShopReloadCommand(RPGShop plugin) {
        super(plugin);
        this.command = "reload";
    }
    @Override
    public boolean onCommand (CommandSender sender, Command command, String label, String[] args) {
        long startTime = System.currentTimeMillis();
        RSReloadEvent event = new RSReloadEvent(sender);
        Bukkit.getPluginManager().callEvent(event);
        if (event.isCancelled()) {

        }
        if (plugin.configManager.reloadConfigs()){
            sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.PLUGIN_RELOAD",
                    String.valueOf((System.currentTimeMillis() - startTime))));
            return true;
        }
        sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.PLUGIN_RELOAD_FAIL"));
        return false;
    }
}
