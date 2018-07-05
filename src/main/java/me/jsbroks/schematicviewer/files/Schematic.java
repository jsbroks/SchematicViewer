package me.jsbroks.schematicviewer.files;

import com.sk89q.jnbt.*;
import com.sk89q.worldedit.EditSession;
import com.sk89q.worldedit.WorldEdit;
import com.sk89q.worldedit.bukkit.BukkitWorld;
import com.sk89q.worldedit.schematic.SchematicFormat;
import me.jsbroks.schematicviewer.Config;
import me.jsbroks.schematicviewer.utils.ItemStackBuilder;
import me.jsbroks.schematicviewer.utils.TextUtil;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class Schematic implements FileStructure {

    private byte[] blocks;
    private byte[] data;
    private short width;
    private short length;
    private short height;
    private File file;
    private boolean isValid;

    public Schematic(File file, byte[] blocks, byte[] data, short width, short length, short height) {
        this.file = file;
        this.blocks = blocks;
        this.data = data;
        this.width = width;
        this.length = length;
        this.height = height;
        this.isValid = width > 0 && height > 0 && length > 0;
    }

    private static void pasteSchematic(Schematic schematic, World world, Location loc) {

        com.sk89q.worldedit.Vector to = new com.sk89q.worldedit.Vector(loc.getX(), loc.getY(), loc.getZ());
        com.sk89q.worldedit.world.World worldEditWorld = new BukkitWorld(world);
        EditSession editSession = WorldEdit.getInstance().getEditSessionFactory().getEditSession(worldEditWorld, -1);
        try {
            SchematicFormat.getFormat(schematic.file).load(schematic.file).paste(editSession, to, false, true);
            editSession.flushQueue();
        } catch (Exception ignore) {}
    }

    @Override
    public File getFile() {
        return file;
    }

    @Override
    public ItemStack createItem() {
        ItemStackBuilder itemStackBuilder = (new ItemStackBuilder(Material.PAPER)).hideAttributes();

        List<String> lore = new ArrayList<>();

        if (!isValid) {
            itemStackBuilder.withName("&r&c" + file.getName().replace(".schematic", ""));
            lore.add("&7Invalid schematic");
        } else {
            itemStackBuilder.withName("&r&a" + file.getName().replace(".schematic", ""));
            lore.add("&7Size: " + length + "x" + width + "x" + height);
            lore.add("");
            lore.add("&7" + getSize() + " blocks");
        }

        itemStackBuilder.withLore(lore);
        return itemStackBuilder.build();
    }

    @Override
    public long getSize() {
        return length * width * height;
    }

    public void paste(final Player player) {

        if (!isValid) return;

        //TODO: Add timing variable
        player.closeInventory();
        player.sendMessage(TextUtil.colorize(Config.config.getString("Messages.Pasting")));
        pasteSchematic(this, player.getWorld(), player.getLocation());
        player.sendMessage(TextUtil.colorize(Config.config.getString("Messages.DonePasting")));

    }

    public static Schematic loadSchematic(File file) {
        try {
            FileInputStream stream = new FileInputStream(file);
            NBTInputStream nbtStream = new NBTInputStream(new GZIPInputStream(stream));

            NamedTag rootTag = nbtStream.readNamedTag();
            nbtStream.close();
            if (rootTag.getName().equals("Schematic")) {
                CompoundTag schematicTag = (CompoundTag) rootTag.getTag();

                // Check
                Map<String, Tag> schematic = schematicTag.getValue();
                if (schematic.containsKey("Blocks")) {
                    short width = getChildTag(schematic, "Width", ShortTag.class).getValue();
                    short length = getChildTag(schematic, "Length", ShortTag.class).getValue();
                    short height = getChildTag(schematic, "Height", ShortTag.class).getValue();

                    String materials = getChildTag(schematic, "Materials", StringTag.class).getValue();
                    if (!materials.equals("Alpha")) {
                        throw new IllegalArgumentException("Schematic file is not an Alpha schematic");
                    }

                    byte[] blocks = getChildTag(schematic, "Blocks", ByteArrayTag.class).getValue();
                    byte[] blockData = getChildTag(schematic, "Data", ByteArrayTag.class).getValue();
                    return new Schematic(file, blocks, blockData, width, length, height);
                }
            }
        } catch (IOException ignore) {}

        return new Schematic(file, "0".getBytes(),"0".getBytes(),(short) 0,(short) 0, (short) 0);
    }

    private static <T extends Tag> T getChildTag(Map<String, Tag> items, String key, Class<T> expected) throws IllegalArgumentException {
        if (!items.containsKey(key)) {
            throw new IllegalArgumentException("Schematic file is missing a \"" + key + "\" tag");
        }
        Tag tag = items.get(key);
        if (!expected.isInstance(tag)) {
            throw new IllegalArgumentException(key + " tag is not of tag type " + expected.getName());
        }
        return expected.cast(tag);
    }
}
