package github.lounode.rpgshop.datagen;


public class ChineseLangProvider extends LanguageProvider{
    protected ChineseLangProvider(DataGenerator dataGenerator) {
        super(dataGenerator, "zh_cn");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        /*
        List<I18NMessage> messages = RPGI18N.getAllTranslatable();
        for (I18NMessage message : messages) {
            translationBuilder.add(message.getKey(), message.getOnLanguage(languageCode));
        }

         */
    }


}
