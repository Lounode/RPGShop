package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.i18n.RPGI18N;
import github.lounode.rpgshop.shop.Shop;
import net.citizensnpcs.api.CitizensAPI;
import net.citizensnpcs.api.command.CommandMessages;
import net.citizensnpcs.api.npc.NPC;
import net.citizensnpcs.api.util.Messaging;
import net.citizensnpcs.trait.CommandTrait;
import net.citizensnpcs.util.Messages;
import org.bukkit.command.Command;
import org.bukkit.command.CommandException;
import org.bukkit.command.CommandSender;

import java.util.Arrays;

public class RPGShopBindNPCCommand extends RPGShopCommand{
    public RPGShopBindNPCCommand(RPGShop plugin) {
        super(plugin);
        this.command = "bind";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = Arrays.copyOfRange(args, 1, args.length);
        if (args.length != 2) {
            sender.sendMessage(RPGI18N.HELP_BIND.get());
            return false;
        }
        Shop shop = plugin.shopManager.getShop(args[0]);
        int npcID = Integer.parseInt(args[1]);
        NPC npc = CitizensAPI.getNPCRegistry().getById(npcID);
        if (shop == null || npc == null) {
            sender.sendMessage(RPGI18N.HELP_BIND.get());
            return false;
        }
        CommandTrait commands = npc.getOrAddTrait(CommandTrait.class);
        String cmd = "rs open" + " " + shop.getID() + " " + "<p>";
        CommandTrait.Hand hand = CommandTrait.Hand.RIGHT;

        try {
            int id = commands.addCommand(new CommandTrait.NPCCommandBuilder(cmd, hand)
                    .cooldown(0)
                    .globalCooldown(0)
            );
            Messaging.sendTr(sender, Messages.COMMAND_ADDED, cmd, id);
        } catch (NumberFormatException ex) {
            throw new CommandException(CommandMessages.INVALID_NUMBER);
        }
        return false;
    }
}
