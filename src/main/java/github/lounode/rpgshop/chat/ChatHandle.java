package github.lounode.rpgshop.chat;

import github.lounode.rpgshop.RPGShop;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * comments:<br>
 * 获取接下来的玩家对话中指定的数据<br>
 * 食用方法:<br>
 * 建立对象后添加 callback ({@link ChatHandle} handle) 函数并启动即可
 *
 * @author Lounode
 * @date 2024/05/18
 */
public class ChatHandle implements Listener {
    public ChatHandle() {
        Bukkit.getPluginManager().registerEvents(this, RPGShop.getInstance());
    }
    private List<String> chats = new ArrayList<>();
    private int line = 1;
    private float timeout = 30.0f;
    private Date startListenTime = new Date(0);
    private UUID playerID;
    private List<ChatListenCallback> callbacks = new ArrayList<>();

    public void addCallback(ChatListenCallback callback) {
        callbacks.add(callback);
    }



    @FunctionalInterface
    public interface ChatListenCallback {
        void execute(ChatHandle handle);
    }
    public void startListener(Player player, float timeout, int line) {
        this.chats.clear();
        this.playerID = player.getUniqueId();
        this.timeout = timeout;
        this.line = line;

        this.startListenTime = new Date();
    }
    public void startListener(Player player) {
        startListener(player, 30, 1);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        Player player = event.getPlayer();
        if (
                isTimeout() ||
                playerID != player.getUniqueId() ||
                chats.size() >= line
        ) {
            return;
        }
        event.setCancelled(true);
        String message = event.getMessage();
        chats.add(message);
        if (chats.size() >= line) {
            startListenTime = new Date(0);
            for (ChatListenCallback callback : callbacks) {
                callback.execute(this);
            }
        }
    }

    private boolean isTimeout() {
        long currentTime = System.currentTimeMillis();
        long startTime = startListenTime.getTime();
        long elapsedTime = currentTime - startTime;


        long timeoutMilliseconds = (long) (timeout * 1000);

        return elapsedTime >= timeoutMilliseconds;
    }

    public double getDouble(int line) {
        if (!checkLine(line)) {
            return 0;
        }
        return Double.parseDouble(chats.get(line));
    }
    public String getString(int line) {
        if (!checkLine(line)) {
            return "";
        }
        return chats.get(line);
    }
    public int getInt(int line) {
        if (!checkLine(line)) {
            return 0;
        }
        return Integer.parseInt(chats.get(line));
    }
    private boolean checkLine(int line) {
        return line <= chats.size();
    }
    public Player getPlayer() {
        if (Bukkit.getPlayer(playerID) != null) {
            return Bukkit.getPlayer(playerID);
        }
        return null;
    }
}
