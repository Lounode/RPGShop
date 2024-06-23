package github.lounode.rpgshop.datagen;

import org.bukkit.plugin.Plugin;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * comments:<br>
 * 数据生成器的 {@link Logger}
 *
 * @author Lounode
 * @date 2024/06/18
 */
public class GeneratorLogger extends Logger {

    public GeneratorLogger(Plugin context) {
        super(context.getDescription().getName() + "-Generator", null);
        setParent(context.getServer().getLogger());
        setLevel(Level.ALL);
    }
}
