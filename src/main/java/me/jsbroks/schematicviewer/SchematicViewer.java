package me.jsbroks.schematicviewer;

import me.jsbroks.schematicviewer.listeners.InventoryEvents;
import me.jsbroks.schematicviewer.views.Viewer;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class SchematicViewer extends JavaPlugin {

    public static PermissionsManager perms;
    private Config config;
    private Map<Player, Viewer> views;
    private File schematicDirectory;

    @Override
    public void onDisable() {
        views.clear();
    }

    @Override
    public void onEnable() {

        this.config = new Config(this);
        config.setup();
        schematicDirectory = config.getSchematicDirectory();

        perms = new PermissionsManager(this);
        views = new HashMap<>();

        getCommand("schematics").setExecutor(new SchematicsCommand(this));

        getServer().getPluginManager().registerEvents(new InventoryEvents(this), this);
    }

    public Map<Player, Viewer> views() {
        return views;
    }

    public File getSchematicDirectory() {
        return schematicDirectory;
    }
}
