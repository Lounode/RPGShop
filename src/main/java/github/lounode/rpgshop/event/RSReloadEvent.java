package github.lounode.rpgshop.event;

import org.bukkit.command.CommandSender;

public class RSReloadEvent extends RSEvent{
    private CommandSender sender;
    public RSReloadEvent(CommandSender sender) {
        this.sender = sender;
    }

    public CommandSender getSender() {
        return sender;
    }
}
