package github.lounode.rpgshop.datagen;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public abstract class LanguageProvider implements DataProvider {
    private DataGenerator generator;
    protected final String languageCode;
    private final File outputFile;
    protected LanguageProvider(DataGenerator dataGenerator) {
        this(dataGenerator, "en_us");
    }

    protected LanguageProvider(DataGenerator dataGenerator, String languageCode) {
        this.generator = dataGenerator;
        this.languageCode = languageCode;
        this.outputFile = new File(generator.getOutputFolder(), "language");
    }

    @Override
    public void run() {
        generator.LOGGER.info("Generate language file: " + languageCode);
        YamlConfiguration languageYml = new YamlConfiguration();

        generateTranslations((String key, String value) -> {
            Objects.requireNonNull(key);
            Objects.requireNonNull(value);

            languageYml.set(key, value);
        });
        try {
            languageYml.save(new File(this.getLanguageFileOutput(), "messages_"+ languageCode +".yml"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }
    @Override
    public File getLanguageFileOutput() {
        return outputFile;
    }

    public abstract void generateTranslations(TranslationBuilder translationBuilder);

    @FunctionalInterface
    public interface TranslationBuilder {
        void add(String translationKey, String value);
    }
}
