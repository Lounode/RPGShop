package github.lounode.rpgshop.i18n;

import github.lounode.rpgshop.RPGShop;
import org.bukkit.configuration.file.YamlConfiguration;

import java.util.ArrayList;
import java.util.List;

public class I18N {
    public static I18NMessage register(String key, String defaultTranslate) {
        I18NMessage message = new I18NMessage(key, defaultTranslate);
        messages.add(message);
        return message;
    }
    public static I18NMessage registerMessage(String path, String defaultTranslate) {
        String key = "message"+ '.' + namespace + '.' + path;
        return register(key, defaultTranslate);
    }
    public static String languageCode = "en_us";
    public static String namespace = "rpgshop";
    protected static List<I18NMessage> messages = new ArrayList<>();
    public static List<I18NMessage> getAllTranslatable() {
        return messages;
    }
    public static void reload() {
        YamlConfiguration config = RPGShop.getInstance().ymlManager.getYml("config");
        languageCode = config.getString("language","en_us");
        YamlConfiguration languageFile = RPGShop.getInstance().ymlManager.getYml("messages_zh_cn");
        for (I18NMessage message : messages) {
            String key = message.getKey();
            String translate = languageFile.getString(key);
            if (translate == null) {
                continue;
            }
            message.addLanguage(languageCode, translate);
        }
    }
}
