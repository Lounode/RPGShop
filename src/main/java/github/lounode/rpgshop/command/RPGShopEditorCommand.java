package github.lounode.rpgshop.command;

import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.gui.guis.EditorMain;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;


public class RPGShopEditorCommand extends RPGShopCommand {

    public RPGShopEditorCommand(RPGShop plugin) {
        super(plugin);
        this.command = "editor";
        this.playerOnly = true;
    }
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player player = (Player) sender;
        EditorMain gui = new EditorMain();
        gui.open(player);
        return true;
    }

}
