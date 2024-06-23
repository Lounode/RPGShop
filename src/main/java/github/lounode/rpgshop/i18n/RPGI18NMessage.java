package github.lounode.rpgshop.i18n;

public class RPGI18NMessage extends I18NMessage{
    public RPGI18NMessage(String key, String translate) {
        super(key, translate, "zh_cn");
    }

    public RPGI18NMessage(String key, String translate, String languageCode) {
        super(key, translate, languageCode);
    }
    @Override
    public RPGI18NMessage addLanguage(String languageCode, String translate) {
        translates.put(languageCode, translate);
        return this;
    }
    @Override
    protected String FormatMessage(String string) {
        String result = string
                .replace("{PREFIX}", RPGI18N.PREFIX.getOrigin());
        return super.FormatMessage(result);
    }
    @Override
    protected String FormatMessage(String string, String... args) {
        String result = string
                .replace("{PREFIX}", RPGI18N.PREFIX.getOrigin());
        return super.FormatMessage(result, args);
    }
}
