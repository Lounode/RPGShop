package github.lounode.rpgshop.utils;


import github.lounode.rpgshop.RPGShop;
import lombok.Getter;

import java.io.File;

public class ConfigManager {
    @Getter
    private RPGShop plugin;

    public ConfigManager(RPGShop plugin) {
        this.plugin = plugin;
    }

    public boolean reloadConfigs() {
        if (plugin.isDebugVersion()) {
            //Delete language
            File[] files = plugin.getDataFolder().listFiles();
            for (File file: files) {
                if (file.getName().endsWith(".json")) {
                    file.delete();
                }
            }
            plugin.saveResource("config.yml", true);
        }
        plugin.reloadConfig();
        plugin.getI18N().reloadMessages(plugin.getConfig().getString("language", "zh_cn"));
        plugin.getShopManager().reloadShops();
        return true;
    }
}
