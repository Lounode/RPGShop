package github.lounode.rpgshop.utils;


import github.lounode.rpgshop.RPGShop;
import github.lounode.rpgshop.i18n.RPGI18N;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;

public class ConfigManager {
    private RPGShop plugin;
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
        reloadLanguage();
        plugin.shopManager.reloadFormDisk();
        return true;
    }
    public void reloadConfig() {
        if (!new File(plugin.getDataFolder(), "config.yml").exists()) {
            plugin.saveResource("config.yml", false);
        }
        config = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), "config.yml"));
    }
    public void reloadLanguage() {
        String language = getConfig().getString("language", "zh_cn");
        String languageFileName = String.format("messages_%s.yml", language);
        File languageFile = new File(plugin.getDataFolder(), languageFileName);
        if (!languageFile.exists()) {
            if (!plugin.isDebugVersion()) {
                plugin.saveResource("lang/" + languageFileName, false);
            } else {
                File genFile = new File(plugin.getDataFolder(), ".datagen/language/" + languageFileName);
                try {
                    Files.copy(genFile.toPath(), languageFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        plugin.ymlManager.reloadYmls();


        RPGI18N.languageCode = language;
        RPGI18N.reload();
    }
    public FileConfiguration getConfig() {
        return config;
    }
}
