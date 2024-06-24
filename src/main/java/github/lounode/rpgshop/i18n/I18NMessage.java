package github.lounode.rpgshop.i18n;

import org.bukkit.ChatColor;

import java.text.MessageFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
public class I18NMessage {
    protected String defaultLanguageCode;
    protected String key;
    protected Map<String, String> translates = new LinkedHashMap<>(16);

    public I18NMessage(String key, String translate) {
        this(key, translate, "en_us");
    }
    public I18NMessage(String key, String translate, String languageCode) {
        this.key = key;
        this.defaultLanguageCode = languageCode;
        this.translates.put(languageCode, translate);
    }
    public String getKey() {
        return key;
    }
    public String getOnLanguage(String languageCode) {
        if (translates.containsKey(languageCode)) {
            return translates.get(languageCode);
        }
        return key;
    }
    public String getOrigin() {
        return getOnLanguage(defaultLanguageCode);
    }

    public String get() {
        String result = getOrigin();
        result = FormatMessage(result);
        return result;
    }
    public String get(Object... args) {
        String[] stringArgs = Arrays.stream(args)
                .map(String::valueOf)
                .toArray(String[]::new);
        String result = getOrigin();
        result = FormatMessage(result, stringArgs);
        return result;
    }

    public I18NMessage addLanguage(String languageCode, String translate) {
        translates.put(languageCode, translate);
        return this;
    }
    protected String FormatMessage(String string) {
        return ChatColor.translateAlternateColorCodes('&', string);
    }
    protected String FormatMessage(String string, String... args) {
        String result = ChatColor.translateAlternateColorCodes('&', string);
        result = MessageFormat.format(result, args);
        result = ChatColor.translateAlternateColorCodes('&', result);
        return result;
    }
}
