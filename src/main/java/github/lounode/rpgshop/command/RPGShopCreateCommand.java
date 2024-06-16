package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.Arrays;
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
            sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.CREATE_INVALID_FORMAT"));
            return false;
        }
        if (hasInvalidCharacters(args[0])) {
            sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.CREATE_INVALID_ID"));
            return false;
        }
        if (!isNumeric(args[1])) {
            sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.CREATE_INVALID_NUMBER"));
            return false;
        }

        String id = args[0];
        int row = Integer.parseInt(args[1]);
        String title = args[2];

        if (!plugin.shopManager.createShop(sender, id ,row, title)){
            sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.SAVE_FAIL",
                    id+".yml",
                    plugin.configManager.getI18NMsg("ADMIN.REASON_ALREADY")
            ));
            return false;
        }

        sender.sendMessage(plugin.configManager.getI18NMsg("ADMIN.CREATE_SUCCESS", String.valueOf(row), title, id));
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
