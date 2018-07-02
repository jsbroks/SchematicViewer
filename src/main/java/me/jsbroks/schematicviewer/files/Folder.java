package me.jsbroks.schematicviewer.files;

import me.jsbroks.schematicviewer.utils.ItemStackBuilder;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

public class Folder implements Icon {

    private List<Folder> subFolders;
    private List<Schematic> schematics;

    private File directory;
    private boolean isValid;
    private boolean loadChildrenFolders;

    public Folder(File directory) {
        this(directory,true);
    }

    private Folder(File directory, boolean loadChildrenFolders) {

        this.schematics = new ArrayList<>();
        this.subFolders = new ArrayList<>();
        this.directory = directory;
        this.loadChildrenFolders = loadChildrenFolders;

        if (directory == null) {
            this.isValid = false;
            return;
        }

        if (!(this.isValid = directory.exists() && directory.isDirectory())) return;

        setSubFolders(directory.listFiles(File::isDirectory));
        setSchematics(directory.listFiles(new FileFilter() {
            @Override
            public boolean accept(File file) {
                return file.getName().endsWith(".schematic");
            }
        }));
    }

    private void setSubFolders(File[] files) {
        if (files == null) return;
        if (!loadChildrenFolders) return;

        for (File file: files) {
            if (!file.isDirectory()) continue;

            Folder folder = new Folder(file, false);
            if (folder.isValid) subFolders.add(folder);
        }
    }

    private void setSchematics(File[] files) {
        if (files == null) return;
        if (!loadChildrenFolders) return;

        for (File file: files) {
            if (!file.isFile()) continue;
            if (!file.getName().endsWith(".schematic")) return;

            schematics.add(Schematic.loadSchematic(file));
        }
    }

    public boolean isValid() {
        return isValid;
    }

    public List<Folder> getFolders() {
        return this.subFolders;
    }

    public List<Schematic> getSchematics() {
        return this.schematics;
    }

    public int getFilesLength() {
        return this.schematics.size() + this.subFolders.size();
    }

    @Override
    public File getFile() {
        return directory;
    }

    @Override
    public ItemStack createItem() {
        ItemStackBuilder item = new ItemStackBuilder(Material.CHEST).hideAttributes();
        item.withName("&r" + directory.getName());

        List<String> lore = new ArrayList<>();
        item.withLore(lore);
        return item.build();
    }
}
