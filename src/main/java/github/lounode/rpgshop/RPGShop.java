package github.lounode.rpgshop;

import github.lounode.rpgshop.command.RPGShopCommandExecutor;
import github.lounode.rpgshop.gui.GUIManager;
import github.lounode.rpgshop.shop.Shop;
import github.lounode.rpgshop.shop.ShopManager;
import github.lounode.rpgshop.shop.Trade;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectItemStacks;
import github.lounode.rpgshop.utils.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;
import org.bukkit.plugin.java.JavaPlugin;


/**
 * comments:
 * RPGShop插件的主类
 *
 * @author Lounode
 * @date 2024/04/11
 */
public final class RPGShop extends JavaPlugin {
    private static RPGShop instance;
    private static final int PLUGIN_ID = 21967;
    private final Metrics metrics = new Metrics(this, PLUGIN_ID);
    private static final String VAULT = "Vault";
    private static final String PLAYER_POINTS = "PlayerPoints";
    private boolean vault;
    private boolean playerPoints;
    public ConfigManager configManager = new ConfigManager();
    public ShopManager shopManager = new ShopManager();
    public GUIManager guiManager = new GUIManager();
    public static RPGShop getInstance() {
        return instance;
    }
    public boolean isVault() {
        return vault;
    }
    public boolean isPlayerPoints() {
        return playerPoints;
    }



    @Override
    public void onEnable() {
        instance = this;
        ConfigurationSerialization.registerClass(ItemStackCounter.class, "ItemStackCounter");
        ConfigurationSerialization.registerClass(ItemPair.class, "ItemPair");
        ConfigurationSerialization.registerClass(Shop.class, "Shop");
        ConfigurationSerialization.registerClass(Trade.class, "Trade");
        ConfigurationSerialization.registerClass(TradeObjectItemStacks.class, "TradeObject");

        getLogger().info(getWelcome());

        if (Bukkit.getPluginManager().isPluginEnabled(VAULT)) {
            try {
                VaultAPI.setup();
                vault = true;
            } catch (NullPointerException e) {
                getLogger().warning("No Find Vault-Economy!");
            }
        } else {
            getLogger().warning("No Find Vault!");
        }

        if (Bukkit.getPluginManager().isPluginEnabled(PLAYER_POINTS)) {
            playerPoints = true;
        } else {
            getLogger().warning("No Find PlayerPoints!");
        }

        shopManager.onEnable(this);

        configManager.onEnable(this);
        guiManager.onEnable(this);


        this.getCommand("rpgshop").setExecutor(new RPGShopCommandExecutor(this));

    }

    @Override
    public void onDisable() {
        ConfigurationSerialization.unregisterClass("ItemStackCounter");
        ConfigurationSerialization.unregisterClass("ItemPair");
        ConfigurationSerialization.unregisterClass("Trade");
        ConfigurationSerialization.unregisterClass("Shop");
        ConfigurationSerialization.unregisterClass("TradeObject");
        configManager.onDisable();
        shopManager.onDisable();
    }

    public boolean isDebugVersion() {
        String debugProperty = System.getProperty("debug");
        return "true".equals(debugProperty);
    }
    private String getWelcome() {
        String normal =
                "\n  _____  _____   _____  _____ _    _  ____  _____  \n" +
                " |  __ \\|  __ \\ / ____|/ ____| |  | |/ __ \\|  __ \\ \n" +
                " | |__) | |__) | |  __| (___ | |__| | |  | | |__) |\n" +
                " |  _  /|  ___/| | |_ |\\___ \\|  __  | |  | |  ___/ \n" +
                " | | \\ \\| |    | |__| |____) | |  | | |__| | |     \n" +
                " |_|  \\_\\_|     \\_____|_____/|_|  |_|\\____/|_|     \n" +
                "                                                   \n" +
                "                                                   ";
        String debug =
                ChatColor.RED +
                "\n  _____  ______ ____  _    _  _____ \n" +
                " |  __ \\|  ____|  _ \\| |  | |/ ____|\n" +
                " | |  | | |__  | |_) | |  | | |  __ \n" +
                " | |  | |  __| |  _ <| |  | | | |_ |\n" +
                " | |__| | |____| |_) | |__| | |__| |\n" +
                " |_____/|______|____/ \\____/ \\_____|\n" +
                "                                    \n" +
                "                                    ";
        if (isDebugVersion()) {
            return normal + debug;
        }
        return normal;
    }
}
