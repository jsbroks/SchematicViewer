package me.jsbroks.schematicviewer.files;

import com.sk89q.jnbt.*;
import me.jsbroks.schematicviewer.utils.ItemStackBuilder;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.inventory.ItemStack;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.zip.GZIPInputStream;

public class Schematic implements Icon {

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

    public byte[] getBlocks() {
        return blocks;
    }

    public byte[] getData() {
        return data;
    }

    public short getWidth() {
        return width;
    }

    public short getLength() {
        return length;
    }

    public short getHeight() {
        return height;
    }

    public boolean isValid() {
        return isValid;
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
            lore.add("&7Blocks: " + blocks.toString());
            lore.add("&7Size: " + length + "x" + width + "x" + height);
        }
        itemStackBuilder.withLore(lore);
        return itemStackBuilder.build();
    }

    @Override
    public File getFile() {
        return file;
    }

    public void paste(World world, Location loc) {

        if (!isValid) return;

        for (int x = 0; x < width; ++x) {
            for (int y = 0; y < height; ++y) {
                for (int z = 0; z < length; ++z) {
                    int index = y * width * length + z * width + x;
                    Block block = new Location(world, x + loc.getX(), y + loc.getY(), z + loc.getZ()).getBlock();
                    block.setTypeIdAndData(blocks[index], data[index], true);
                }
            }
        }
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
