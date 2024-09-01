package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.shop.Shop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Date;
import java.util.regex.Pattern;

public class RPGShopCreateCommand extends RPGShopCommand{

    public RPGShopCreateCommand(RPGShop plugin) {
        super(plugin);
        this.command = "create";
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        args = Arrays.copyOfRange(args, 1, args.length);
        if (args.length != 3) {
            sender.sendMessage(plugin.getI18N("message.create_invalid_format"));
            return false;
        }
        if (hasInvalidCharacters(args[0])) {
            sender.sendMessage(plugin.getI18N("message.create_invalid_id"));
            return false;
        }
        if (plugin.getShopManager().getShop(args[0]) != null) {
            sender.sendMessage(plugin.getI18N("message.create_already_exists", args[0]));
        }
        if (!isNumeric(args[1])) {
            sender.sendMessage(plugin.getI18N("message.create_invalid_number"));
            return false;
        }

        String id = args[0];
        int row = Integer.parseInt(args[1]);
        String title = args[2];
        String owner = sender.getName();

        SimpleDateFormat sdf = new SimpleDateFormat();
        sdf.applyPattern("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String createTime = sdf.format(date);

        Shop shop = new Shop(id, row, title);
        shop.setOwner(owner);
        shop.setCreateTime(createTime);
        plugin.getShopManager().addShop(shop);

        sender.sendMessage(plugin.getI18N("message.create_success", row, title, id));
        return true;
    }
    private boolean hasInvalidCharacters(String string) {
        return string.contains("&") || string.contains("§");
    }
    private static boolean isNumeric(String string){
        Pattern pattern = Pattern.compile("^[1-9][0-9]*$");
        return pattern.matcher(string).matches();
    }
}
