package github.lounode.rpgshop.event;

import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class RSEvent extends Event implements Cancellable {
    private Event.Result result;
    private String message;
    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public void setResult(Event.Result newResult) {
        this.result = newResult;
    }

    public Event.Result getResult() {
        return this.result;
    }

    public boolean isCancelled() {
        return this.getResult() == Result.DENY;
    }

    public void setCancelled(boolean toCancel, String message) {
        setCancelled(toCancel);
        this.message = message;
    }
    public void setCancelled(boolean toCancel) {
        this.setResult(toCancel ? Result.DENY : Result.ALLOW);
    }
    public String getMessage() {
        return message;
    }
}
