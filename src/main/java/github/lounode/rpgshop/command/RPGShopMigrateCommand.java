package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

/**
 * 迁移数据从Yml到数据库
 *
 * @author Lounode
 * @date 2024/08/27
 */
public class RPGShopMigrateCommand extends RPGShopCommand{
    public RPGShopMigrateCommand(RPGShop plugin) {
        super(plugin);
        this.command = "migrate";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        return false;
    }
}
