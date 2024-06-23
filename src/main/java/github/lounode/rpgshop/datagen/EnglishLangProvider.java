package github.lounode.rpgshop.datagen;

import github.lounode.rpgshop.i18n.I18NMessage;
import github.lounode.rpgshop.i18n.RPGI18N;

import java.util.List;

public class EnglishLangProvider extends LanguageProvider{
    protected EnglishLangProvider(DataGenerator dataGenerator) {
        super(dataGenerator, "en_us");
    }

    @Override
    public void generateTranslations(TranslationBuilder translationBuilder) {
        List<I18NMessage> messages = RPGI18N.getAllTranslatable();
        for (I18NMessage message : messages) {
            translationBuilder.add(message.getKey(), message.getOnLanguage(languageCode));
        }
    }
}
