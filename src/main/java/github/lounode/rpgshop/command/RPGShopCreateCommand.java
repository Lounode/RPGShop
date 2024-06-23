package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.i18n.RPGI18N;
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
            sender.sendMessage(RPGI18N.CREATE_INVALID_FORMAT.get());
            return false;
        }
        if (hasInvalidCharacters(args[0])) {
            sender.sendMessage(RPGI18N.CREATE_INVALID_ID.get());
            return false;
        }
        if (!isNumeric(args[1])) {
            sender.sendMessage(RPGI18N.CREATE_INVALID_NUMBER.get());
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

        Shop shop = plugin.shopManager.createShop(sender, id ,row, title);
        if (shop == null){
            sender.sendMessage(RPGI18N.MESSAGE_SAVE_FAIL.get(id + ".yml", RPGI18N.MESSAGE_REASON_ALREADY.get()));
            return false;
        }
        shop.setOwner(owner);
        shop.setCreateTime(createTime);
        plugin.shopManager.saveShops();

        sender.sendMessage(RPGI18N.CREATE_SUCCESS.get(row, title, id));
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
