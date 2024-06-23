package github.lounode.rpgshop.datagen;

import org.bukkit.plugin.java.JavaPlugin;

/**
 * comments:<br>
 * RPGShop的数据生成器，用来生成语言文件
 *
 * @author Lounode
 * @date 2024/06/18
 */
public class RPGShopDataGenerator extends DataGenerator {
    public RPGShopDataGenerator(JavaPlugin plugin) {
        super(plugin);
    }

    @Override
    public void onInitializeDataGenerator() {
        this.addProvider(new ChineseLangProvider(this));
        this.addProvider(new EnglishLangProvider(this));
    }

}
