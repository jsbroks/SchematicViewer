package me.jsbroks.schematicviewer.listeners;

import me.jsbroks.schematicviewer.SchematicViewer;
import me.jsbroks.schematicviewer.files.Folder;
import me.jsbroks.schematicviewer.files.Icon;
import me.jsbroks.schematicviewer.files.Schematic;
import me.jsbroks.schematicviewer.views.ViewPage;
import me.jsbroks.schematicviewer.views.Viewer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryEvents implements Listener {

    private SchematicViewer schematicViewer;

    public InventoryEvents(SchematicViewer schematicViewer) {
        this.schematicViewer = schematicViewer;
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        if (!(event.getWhoClicked() instanceof Player)) return;

        Player player = (Player) event.getWhoClicked();

        if (!schematicViewer.views().containsKey(player)) return;

        Inventory inventory = event.getClickedInventory();

        if (inventory == null) {
            return;
        }

        Viewer viewer = schematicViewer.views().get(player);
        for (ViewPage view: viewer.getViews()) {
            if (inventory.equals(view.getInventory())) {

                event.setCancelled(true);

                int slot = event.getSlot();
                ItemStack item = event.getCurrentItem();

                if (item.hasItemMeta()) {
                    String name = item.getItemMeta().getDisplayName();

                    if (slot < 9*5) {
                        int page = Integer.valueOf(inventory.getName().replaceAll("[^\\d.]", ""));
                        Icon icon = viewer.getItems().get(slot + (page - 1) * Viewer.FILES_PER_VIEW);

                        if (icon instanceof Folder) {
                            System.out.println(icon.getFile().getPath());
                            Viewer newViewer = new Viewer((Folder) icon, player,
                                    viewer.getCompare(),
                                    viewer.isDisplayFoldersFirst());

                            schematicViewer.views().put(player, newViewer);
                            player.openInventory(newViewer.getInventory(0));
                        }

                        if (icon instanceof Schematic) {
                            //TODO: Add a confirmation window
                            ((Schematic) icon).paste(player);
                        }
                    }

                    if (name.toLowerCase().contains("next")) {
                        player.openInventory(viewer.nextPage());
                    }

                    if (name.toLowerCase().contains("previous")) {
                        player.openInventory(viewer.previousPage());
                    }
                }

                return;
            }
        }

    }

}
