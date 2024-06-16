package github.lounode.rpgshop.utils;


import java.text.MessageFormat;

public class Formater {
    public static String FormatMessage(String string) {
        return string.replace("&", "§");
    }
    public static String FormatMessage(String string, String... args) {
        return MessageFormat.format(string, args).replace("&", "§");
    }

}
