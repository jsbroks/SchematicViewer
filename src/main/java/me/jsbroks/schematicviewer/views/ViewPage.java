package me.jsbroks.schematicviewer.views;

import me.jsbroks.schematicviewer.utils.ItemIcons;
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
    private final int UNDO_POSITION = 45;
    private final int BACK_POSITION = 45;

    public ViewPage(int pageNum, boolean hasBack) {
        this.inventory = Bukkit.createInventory(null, 6*9, "Schematic Viewer (Page: " + pageNum + ")");

        this.inventory.setItem(NEXT_POSITION, ItemIcons.nextItem());
        this.inventory.setItem(PREVIOUS_POSITION, ItemIcons.previousItem());

        //TODO: Add undo functionality
        //this.inventory.setItem(UNDO_POSITION, ItemIcons.undoItem());

        //TODO: ADd back functionality
        if (hasBack) this.inventory.setItem(BACK_POSITION, ItemIcons.backItem());
    }

    public void hasNoNext() {
        this.inventory.setItem(NEXT_POSITION, ItemIcons.noNextItem());
    }

    public void hasNoPrevious() {
        this.inventory.setItem(PREVIOUS_POSITION, ItemIcons.noPreviousItem());
    }

    public void addIcon(ItemStack item) {
        inventory.addItem(item);
    }

    public Inventory getInventory() {
        return inventory;
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
