package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class RPGShopTestCommand extends RPGShopCommand{
    public RPGShopTestCommand(RPGShop plugin) {
        super(plugin);
        this.command = "test";
        this.playerOnly = true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        /*
        Player player = (Player) sender;
        ItemStack item = player.getInventory().getItemInMainHand();

        net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
        TranslatableComponent comp = new TranslatableComponent(nmsItem.getItem().getName());
        TextComponent main = new TextComponent("1: ");
        main.addExtra(comp);
        sender.spigot().sendMessage(main);
        Bukkit.broadcastMessage("2: "+ CraftItemStack.asNMSCopy(item).getName());
        //player.sendMessage("3: "+ Translate.getMaterial(player, item.getType()));
        /*
        TranslatableComponent giveMessage = new TranslatableComponent("commands.give.success");
        TranslatableComponent itemDiamond = new TranslatableComponent("item.swordDiamond.name");
        itemDiamond.setColor(net.md_5.bungee.api.ChatColor.GOLD);
        giveMessage.addWith(itemDiamond);
        giveMessage.addWith("32");
        TextComponent username = new TextComponent(player.getName());
        username.setColor(net.md_5.bungee.api.ChatColor.AQUA);
        giveMessage.addWith(username);
        player.spigot().sendMessage(giveMessage);

         */
        return true;
    }

}
