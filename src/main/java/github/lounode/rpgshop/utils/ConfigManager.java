package github.lounode.rpgshop.utils;


import github.lounode.rpgshop.RPGShop;
import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

public class ConfigManager {
    private RPGShop plugin;
    @Getter
    private FileConfiguration config;
    public void onEnable(RPGShop plugin) {
        this.plugin = plugin;
        reloadConfigs();
    }

    public void onDisable() {

    }
    public boolean reloadConfigs() {

        if (plugin.isDebugVersion()) {
            File[] files = plugin.getDataFolder().listFiles();
            for (File file: files) {
                if (file.getName() == "config" || file.getName().startsWith("messages")) {
                    file.delete();
                }
            }
        }


        reloadConfig();
        plugin.getI18N().reloadMessages(getConfig().getString("language", "zh_cn"));
        plugin.shopManager.reloadFormDisk();
        return true;
    }
    public void reloadConfig() {
        if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
    }
}
