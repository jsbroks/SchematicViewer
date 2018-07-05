package me.jsbroks.schematicviewer.utils;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public class ItemIcons {

    public static ItemStack undoItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.WORKBENCH).hideAttributes();
        item.withName("&r&eUndo");
        return item.build();
    }

    public static ItemStack backItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.REDSTONE).hideAttributes();
        item.withName("&r&cBack");
        return item.build();
    }

    public static ItemStack nextItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.INK_SACK).hideAttributes();
        item.withName("&aNext");
        item.withData(10);
        return item.build();
    }

    public static ItemStack previousItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.INK_SACK).hideAttributes();
        item.withName("&aPrevious");
        item.withData(10);
        return item.build();
    }

    public static ItemStack noNextItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.INK_SACK).hideAttributes();
        item.withName("&7Next");
        item.withData(8);
        return item.build();
    }

    public static ItemStack noPreviousItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.INK_SACK).hideAttributes();
        item.withName("&7Previous");
        item.withData(8);
        return item.build();
    }
}
