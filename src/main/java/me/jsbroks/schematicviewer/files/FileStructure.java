package me.jsbroks.schematicviewer.files;

import org.bukkit.inventory.ItemStack;

import java.io.File;

public interface FileStructure {

    ItemStack createItem();
    File getFile();
    long getSize();

}
