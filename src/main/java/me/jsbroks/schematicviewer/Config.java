package me.jsbroks.schematicviewer;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class Config {

    public static FileConfiguration config;

    private File schematic;
    private static File configFile;
    private Plugin plugin;

    public Config(Plugin plugin) {
        this.plugin = plugin;
    }

    private boolean fileExist(File file) {
        if (file.exists()) {
            return true;
        } else {
            configFile.getParentFile().mkdirs();
            plugin.saveResource(file.getName(), false);
            return false;
        }
    }

    public void setup() {

        configFile = new File(plugin.getDataFolder(), "config.yml");
        schematic = new File(Bukkit.getPluginManager().getPlugin("WorldEdit").getDataFolder(), "/schematics");

        if (!schematic.exists()) {
            schematic.mkdir();
        }

        fileExist(configFile);
        config = new YamlConfiguration();

        try {
            config.load(configFile);
            config = YamlConfiguration.loadConfiguration(configFile);
            config.options().copyDefaults(true);

            plugin.saveDefaultConfig();
        } catch (Exception e) {

            plugin.getLogger().info("\nError setting up file!\n");
            e.printStackTrace();
        }
    }

    private void saveConfig() {
        try {
            config.save(configFile);
            reloadConfig();
        } catch (IOException ignored) {
            plugin.getLogger().info("Could not save file!");
        }
    }

    public File getSchematicDirectory() {
        return schematic;
    }

    private void reloadConfig() {

        if (config == null) {
            configFile = new File(plugin.getDataFolder(), "config.yml");
        }

        plugin.reloadConfig();
    }

}