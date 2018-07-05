package me.jsbroks.schematicviewer.views;

import me.jsbroks.schematicviewer.SchematicViewer;
import me.jsbroks.schematicviewer.files.CompareType;
import me.jsbroks.schematicviewer.files.FileStructure;
import me.jsbroks.schematicviewer.files.Folder;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;

import java.util.ArrayList;
import java.util.List;

public class Viewer {

    private Folder folder;
    private boolean displayFoldersFirst;
    private int numberOfPages;
    private List<ViewPage> views;
    private List<FileStructure> items;
    private CompareType compareType;

    private int currentPage;

    public static final int FILES_PER_VIEW = 5*9;

    public Viewer(Folder folder, Player player, CompareType compare, boolean displayFoldersFirst) {
        this.folder = folder;
        this.displayFoldersFirst = displayFoldersFirst;
        this.items = new ArrayList<>();
        this.currentPage = 0;
        this.compareType = compare;

        folder.getFolders().sort(compare.getComparator());
        folder.getSchematics().sort(compare.getComparator());

        if (displayFoldersFirst) {
            items.addAll(folder.getFolders());
            items.addAll(folder.getSchematics());
        } else {
            items.addAll(folder.getSchematics());
            items.addAll(folder.getFolders());
        }

        this.views = new ArrayList<>();
        int count = 0;
        for (FileStructure fileStructure : items) {
            if (Math.ceil(count / FILES_PER_VIEW) - views.size() == 0) {
                views.add(new ViewPage(views.size() + 1, false));
            }

            if (player.hasPermission(SchematicViewer.perms.filePermission(fileStructure.getFile()))) {
                ViewPage viewPage = views.get(views.size() - 1);
                viewPage.addIcon(fileStructure.createItem());
                count++;
            }
        }

        if (views.isEmpty()) {
            views.add(new ViewPage(views.size() + 1, false));
        }

        views.get(0).hasNoPrevious();
        views.get(views.size() - 1).hasNoNext();

    }

    public Inventory getInventory(int page) {

        this.currentPage = page;

        try {
            return views.get(currentPage).getInventory();
        } catch (IndexOutOfBoundsException e) {
            return ViewPage.helpMenu("You have entered an invalid page number. Total pages: " + views.size(),
                    folder.getFile().getPath());
        }
    }

    public List<ViewPage> getViews() {
        return views;
    }

    public Inventory nextPage() {
        if (currentPage >= views.size() - 1) { return getInventory(views.size() - 1); }

        return getInventory(currentPage + 1);
    }

    public Inventory previousPage() {
        if (currentPage <= 0) return getInventory(0);
        return getInventory(currentPage - 1);
    }

    public List<FileStructure> getItems() {
        return items;
    }

    public CompareType getCompare() {
        return compareType;
    }

    public boolean isDisplayFoldersFirst() {
        return displayFoldersFirst;
    }
}