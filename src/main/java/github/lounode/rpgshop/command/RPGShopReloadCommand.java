package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.event.RSReloadEvent;
import github.lounode.rpgshop.i18n.RPGI18N;
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
            float time = System.currentTimeMillis() - startTime;
            sender.sendMessage(RPGI18N.PLUGIN_RELOAD.get(time));
            return true;
        }
        sender.sendMessage(RPGI18N.PLUGIN_RELOAD_FAIL.get());
        return false;
    }
}
