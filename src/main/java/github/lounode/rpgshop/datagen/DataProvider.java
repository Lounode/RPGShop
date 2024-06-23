package github.lounode.rpgshop.datagen;

import java.io.File;

public interface DataProvider {
    void run();

    File getLanguageFileOutput();
}
