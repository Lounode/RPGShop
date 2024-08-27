package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.shop.Shop;
import github.lounode.rpgshop.utils.citizens.RPGShopTrait;
import github.lounode.spearmintlib.bukkit.plugins.citizens.CitizensUtil;
import net.citizensnpcs.api.npc.NPC;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.Arrays;

public class RPGShopBindNPCCommand extends RPGShopCommand{
    public RPGShopBindNPCCommand(RPGShop plugin) {
        super(plugin);
        this.command = "bind";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = Arrays.copyOfRange(args, 1, args.length);
        if (args.length != 1) {
            sender.sendMessage(plugin.getI18N("common.help_bind"));
            return false;
        }
        Player player = (Player) sender;
        Shop shop = plugin.shopManager.getShop(args[0]);
        NPC npc = CitizensUtil.GetNPCForward(player);
        if (shop == null || npc == null) {
            sender.sendMessage(plugin.getI18N("common.help_bind"));
            return false;
        }
        RPGShopTrait rsTrait = new RPGShopTrait(shop.getID());
        npc.addTrait(rsTrait);
        player.sendMessage(plugin.getI18N("message.bind_success"));

        return true;
    }
}
