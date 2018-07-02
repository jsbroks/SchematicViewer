package me.jsbroks.schematicviewer;

import java.io.File;

public class PermissionsManager {

    private final String BASE_PERM = "schematic.viewer.";
    public final String USE = BASE_PERM + "use";

    private SchematicViewer schematicViewer;

    public PermissionsManager(SchematicViewer schematicViewer) {
        this.schematicViewer = schematicViewer;
    }

    public String filePermission(File file) {
        String path = file.getPath().replaceAll(schematicViewer.getSchematicDirectory().getPath(), "");
        System.out.println(path);
        return BASE_PERM + "file." + path.replaceAll("/", ".");
    }


}
