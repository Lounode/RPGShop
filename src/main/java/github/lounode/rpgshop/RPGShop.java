package github.lounode.rpgshop;

import com.demonwav.mcdev.annotations.Translatable;
import github.lounode.rpgshop.command.RPGShopCommandExecutor;
import github.lounode.rpgshop.gui.GUIManager;
import github.lounode.rpgshop.shop.Shop;
import github.lounode.rpgshop.shop.ShopManager;
import github.lounode.rpgshop.shop.Trade;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectExpLevel;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectItemStacks;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectMoney;
import github.lounode.rpgshop.shop.tradeobjects.TradeObjectPlayerPoints;
import github.lounode.rpgshop.utils.*;
import github.lounode.rpgshop.utils.citizens.CitizensListener;
import github.lounode.rpgshop.utils.citizens.RPGShopTrait;
import github.lounode.spearmintlib.bukkit.BukkitPlugin;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.serialization.ConfigurationSerialization;


/**
 * comments:
 * RPGShop插件的主类
 *
 * @author Lounode
 * @date 2024/04/11
 */
public final class RPGShop extends BukkitPlugin<RPGShop> {
    private static final int PLUGIN_ID = 21967;
    private final Metrics METRICS = new Metrics(this, PLUGIN_ID);
    private static final String VAULT = "Vault";
    private static final String PLAYER_POINTS = "PlayerPoints";
    private static final String CITIZENS = "Citizens";
    @Getter
    private boolean vault;
    @Getter
    private boolean playerPoints;
    @Getter
    private boolean citizens;
    public ConfigManager configManager = new ConfigManager();
    @Getter
    public ShopManager shopManager = new ShopManager();
    public GUIManager guiManager = new GUIManager();



    @Override
    public void onLoad() {


        String languageCode = getConfig().getString("language","en_us");
        getI18N().reloadMessages(languageCode);

        ConfigurationSerialization.registerClass(ItemStackCounter.class, "ItemStackCounter");
        ConfigurationSerialization.registerClass(ItemPair.class, "ItemPair");
        ConfigurationSerialization.registerClass(Shop.class, "Shop");
        ConfigurationSerialization.registerClass(Trade.class, "Trade");
        //TradeObjects
        ConfigurationSerialization.registerClass(TradeObjectItemStacks.class, "TradeObjectItemStacks");
        ConfigurationSerialization.registerClass(TradeObjectMoney.class, "TradeObjectMoney");
        ConfigurationSerialization.registerClass(TradeObjectPlayerPoints.class, "TradeObjectPlayerPoints");
        ConfigurationSerialization.registerClass(TradeObjectExpLevel.class, "TradeObjectItemExpLevel");
    }

    @Override
    public void onEnable() {
        getLogger().info(getWelcome());
        /*
        if (isDebugVersion()) {
            DATA_GENERATOR.onInitializeDataGenerator();
            DATA_GENERATOR.generatorAll();
        }
        */


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
        if (Bukkit.getPluginManager().isPluginEnabled(CITIZENS)) {
            citizens = true;
            net.citizensnpcs.api.CitizensAPI.getTraitFactory()
                    .registerTrait(net.citizensnpcs.api.trait.TraitInfo.create(RPGShopTrait.class));
            Bukkit.getPluginManager().registerEvents(new CitizensListener(), this);
        } else {
            getLogger().warning("No Find Citizens!");
        }

        shopManager.onEnable(this);

        configManager.onEnable(this);
        guiManager.onEnable(this);


        this.getCommand("rpgshop").setExecutor(new RPGShopCommandExecutor(this));

    }

    @Override
    public void onDisable() {

        configManager.onDisable();
        shopManager.onDisable();
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
    public static RPGShop getInstance() {
        return (RPGShop) instance;
    }

    @Override
    public String getI18N(@Translatable String key, Object... args) {
        String result = super.getI18N(key, args);
        result = result.replace("{PREFIX}", super.getI18N("common.prefix"));
        result = ChatColor.translateAlternateColorCodes('&', result);
        return result;
    }
}
