package me.jsbroks.schematicviewer.views;

import me.jsbroks.schematicviewer.utils.ItemStackBuilder;
import me.jsbroks.schematicviewer.utils.TextUtil;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.List;

public class ViewPage {

    private Inventory inventory;

    private final int NEXT_POSITION = 53;
    private final int PREVIOUS_POSITION = 45;

    public ViewPage(int pageNum) {
        this.inventory = Bukkit.createInventory(null, 6*9, "Schematic Viewer (Page: " + pageNum + ")");
        this.inventory.setItem(NEXT_POSITION, nextItem());
        this.inventory.setItem(PREVIOUS_POSITION, previousItem());
    }

    public void hasNoNext() {
        this.inventory.setItem(NEXT_POSITION, noNextItem());
    }

    public void hasNoPrevious() {
        this.inventory.setItem(PREVIOUS_POSITION, noPreviousItem());
    }

    public void addIcon(ItemStack item) {
        inventory.addItem(item);
    }

    public Inventory getInventory() {
        return inventory;
    }

    private static ItemStack nextItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.INK_SACK).hideAttributes();
        item.withName("&aNext");
        item.withData(10);
        return item.build();
    }

    private static ItemStack noNextItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.INK_SACK).hideAttributes();
        item.withName("&7Next");
        item.withData(8);
        return item.build();
    }

    private static ItemStack previousItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.INK_SACK).hideAttributes();
        item.withName("&aPrevious");
        item.withData(10);
        return item.build();
    }

    private static ItemStack noPreviousItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.INK_SACK).hideAttributes();
        item.withName("&7Previous");
        item.withData(8);
        return item.build();
    }

    public static Inventory helpMenu(String message, String path) {

        Inventory menu = Bukkit.createInventory(null, 9, "Error");

        List<String> errorLore = new ArrayList<>();
        for (String line: TextUtil.wrapString(message, 20).split("\n")) {
            errorLore.add("&7" + line);
        }

        if (path != null) {
            errorLore.add("&ePath: &7" + path.replace("plugins/WorldEdit", ""));
        }

        menu.addItem(new ItemStackBuilder(Material.PAPER).hideAttributes().withName("&cError Message")
                .withLore(errorLore).build());

        return menu;
    }
}
