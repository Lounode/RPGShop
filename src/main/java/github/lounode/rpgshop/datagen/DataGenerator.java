package github.lounode.rpgshop.datagen;

import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * comments:<br>
 * 插件数据生成器
 *
 * @author Lounode
 * @date 2024/06/18
 */
public abstract class DataGenerator {
    public final Logger LOGGER;
    public JavaPlugin plugin;
    private List<DataProvider> providers = new ArrayList<>();

    public DataGenerator(JavaPlugin plugin) {
        this.plugin = plugin;
        this.LOGGER = new GeneratorLogger(plugin);
    }

    abstract void onInitializeDataGenerator();
    public void generatorAll() {
        for (DataProvider provider : providers) {
            provider.run();
        }
        LOGGER.info("End of the generate.");
    }

    protected void addProvider(DataProvider provider) {
        this.providers.add(provider);
    }

    public File getOutputFolder() {
        return new File(plugin.getDataFolder(), ".datagen");
    }
}
