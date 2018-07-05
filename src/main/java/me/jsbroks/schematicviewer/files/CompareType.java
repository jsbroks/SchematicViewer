package me.jsbroks.schematicviewer.files;

import java.util.Comparator;

public enum CompareType {

    NAME() {
        @Override
        public Comparator<FileStructure> getComparator() {
            return (fileStruct1, fileStruct2) -> {
                int res = String.CASE_INSENSITIVE_ORDER
                        .compare(fileStruct1.getFile().getName(), fileStruct2.getFile().getName());

                if (res == 0) {
                    res = fileStruct1.getFile().getName().compareTo(fileStruct2.getFile().getName());
                }
                return res;
            };
        }
    };

    public abstract Comparator<FileStructure> getComparator();
}
