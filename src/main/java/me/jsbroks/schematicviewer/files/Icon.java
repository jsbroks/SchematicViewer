package me.jsbroks.schematicviewer.files;

import org.bukkit.inventory.ItemStack;

import java.io.File;

public interface Icon {

    ItemStack createItem();
    File getFile();

}
