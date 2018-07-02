package me.jsbroks.schematicviewer;

import me.jsbroks.schematicviewer.files.CompareType;
import me.jsbroks.schematicviewer.files.Folder;
import me.jsbroks.schematicviewer.utils.TextUtil;
import me.jsbroks.schematicviewer.views.Viewer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.io.File;

public class SchematicsCommand implements CommandExecutor {

    private SchematicViewer schematicViewer;

    public SchematicsCommand(SchematicViewer schematicViewer) {
        this.schematicViewer = schematicViewer;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {

        if (command.getName().equalsIgnoreCase("schematics")) {

            if (!(sender instanceof Player)) {
                sender.sendMessage("You must be a player to use this command.");
                return true;
            }

            Player player = (Player) sender;
            if (!player.hasPermission(SchematicViewer.perms.USE)) {
                permissionsMessage(player);
            }

            if (args.length == 0) {

                Folder folder = new Folder(schematicViewer.getSchematicDirectory());

                if (player.hasPermission(SchematicViewer.perms
                        .filePermission(folder.getFile()))) {

                    createViewer(folder, player, CompareType.NAME, true);
                    return true;
                }
            } else {

                String path = "";
                CompareType compare = CompareType.NAME;
                boolean displayFoldersFirst = true;

                // TODO: add support for short forms
                for (String arg: args) {
                    arg = arg.toLowerCase();

                    if (arg.startsWith("compare=")) {
                        compare = CompareType.valueOf(arg.split("=")[1]);
                    }

                    // TODO: add support to files with spaces in name.
                    if (arg.startsWith("path=")) {
                        path = arg.split("=")[1];
                    }

                    if (arg.startsWith("foldersfirst=")) {
                        displayFoldersFirst = Boolean.valueOf(arg.split("=")[1]);
                    }

                    // TODO: add support for ordering
                    if (arg.startsWith("order=")) {

                    }
                }

                // Remove being b
                path = path.startsWith("/") ? path.substring(1) : path;
                File file = new File(schematicViewer.getSchematicDirectory().getPath() + "/" + path);

                Folder folder = new Folder(file);

                if (!folder.isValid()) {
                    player.sendMessage(TextUtil.colorize(Config.config.getString("Messages.InvalidFolder")));
                    return true;
                }

                createViewer(folder, player, compare, displayFoldersFirst);
                return true;
            }
        }
        return false;
    }

    private void createViewer(Folder folder, Player player, CompareType compare,
                              boolean displayFoldersFirst) {

        // TODO: Print options using custom message with variables from config file

        Viewer viewer = new Viewer(folder, player, compare, displayFoldersFirst);
        schematicViewer.views().put(player, viewer);

        player.openInventory(viewer.getInventory(0));
    }

    private void permissionsMessage(Player player) {
        player.sendMessage(TextUtil.colorize(Config.config.getString("Messages.PermissionDenied")));
    }
}
