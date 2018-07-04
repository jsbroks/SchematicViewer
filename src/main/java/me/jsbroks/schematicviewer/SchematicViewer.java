package me.jsbroks.schematicviewer;

import me.jsbroks.schematicviewer.listeners.InventoryEvents;
import me.jsbroks.schematicviewer.views.Viewer;
import org.bukkit.Bukkit;
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

    /**
     * -2: Feature disabled
     * -1: Error while checking
     * 0: Update found
     * 1: Using current version
     */
    private int upToDate;

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

        checkForUpdates();
    }

    private void checkForUpdates() {
        upToDate = 1;
        if (Config.config.getBoolean("UpdateChecker")) {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
                SpigotUpdater updater = new SpigotUpdater(this, 58318);
                try {

                    if (upToDate == 0) return;

                    if (updater.checkForUpdates()) {
                        getLogger().info("An update is available. Download the most recent version (v"
                                + updater.getLatestVersion() + ") at " + updater.getResourceURL() + ".");

                        upToDate = 0;
                    } else {
                        getLogger().info("You are using the most current version of " + this.getName() + ".");
                        upToDate = 1;
                    }
                } catch (Exception e) {
                    getLogger().warning("Could not check for updates!");
                }

            }, 0, 20 * 60 * 60);
        } else {
            upToDate = -2;
            getLogger().warning("Update checker is disabled in the config.");
        }
    }

    public Map<Player, Viewer> views() {
        return views;
    }

    public File getSchematicDirectory() {
        return schematicDirectory;
    }
}
