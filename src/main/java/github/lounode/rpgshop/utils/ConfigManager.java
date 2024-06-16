package github.lounode.rpgshop.utils;


import github.lounode.rpgshop.RPGShop;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.text.MessageFormat;

public class ConfigManager {
    private RPGShop plugin;
    private FileConfiguration config;
    private FileConfiguration messages;
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
        String language = getConfig().getString("language");
        String languageFileName = String.format("messages_%s.yml", language);

        if (!new File(plugin.getDataFolder(), languageFileName).exists()) {
            plugin.saveResource(languageFileName, false);
        }
        messages = YamlConfiguration.loadConfiguration(new File(plugin.getDataFolder(), languageFileName));
    }
    public FileConfiguration getConfig() {
        return config;
    }
    public FileConfiguration getI18N() {
        return messages;
    }
    public String getI18NMsg(String key) {
        String message = getI18N().getString(key);
        if (message == null) {
            return key;
        }
        return FormatMessage(message);
    }
    public String getI18NMsg(String key, String... args) {
        String message = getI18N().getString(key);
        if (message == null) {
            return key;
        }
        return FormatMessage(message, args);
    }
    private String FormatMessage(String string) {
        return string
                .replace("{PREFIX}", getI18N().getString("COMMON.PREFIX"))
                .replace("&", "§");
    }
    private String FormatMessage(String string, String... args) {
        return MessageFormat.format(string.replace("{PREFIX}", getI18N().getString("COMMON.PREFIX")), args)
                .replace("&", "§");
    }
}
