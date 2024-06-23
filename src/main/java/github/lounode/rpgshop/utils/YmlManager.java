package github.lounode.rpgshop.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class YmlManager {
    private JavaPlugin plugin;
    private File rootFolder;
    private Map<File, YamlConfiguration> ymls = new LinkedHashMap<>();
    public YmlManager (JavaPlugin plugin, File rootFolder) {
        this.plugin = plugin;
        this.rootFolder = rootFolder;
    }
    public YamlConfiguration getYml(String name) {
        for (Map.Entry<File, YamlConfiguration> yml : ymls.entrySet()) {
            if (yml.getKey().getName().replace(".yml", "").equals(name)) {
                return yml.getValue();
            }
        }
        return null;
    }
    public void reloadYmls() {
        if (!this.rootFolder.exists()) {
            plugin.getLogger().severe("YML root path: " + rootFolder.getPath() + "not found!");
            return;
        }
        List<File> ymlFiles = new ArrayList<>();
        findYmlFiles(rootFolder, ymlFiles);

        for (File ymlFile : ymlFiles) {
            try{
                YamlConfiguration yamlConfig = YamlConfiguration.loadConfiguration(ymlFile);
                ymls.put(ymlFile, yamlConfig);
            } catch (IllegalArgumentException e) {
                plugin.getLogger().warning("Could not load yml file:"+ ymlFile.getName() +" , in the: " + ymlFile.getPath());
            }
        }
    }
    private void findYmlFiles(File directory, List<File> ymlFiles) {
        File[] files = directory.listFiles();
        if (files == null) {
            return;
        }

        for (File file : files) {
            if (file.isDirectory()) {
                findYmlFiles(file, ymlFiles);
            } else if (file.isFile() && file.getName().endsWith(".yml")) {
                ymlFiles.add(file);
            }
        }
    }
}
