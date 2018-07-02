package me.jsbroks.schematicviewer.files;

import java.util.Comparator;

public enum CompareType {

    NAME() {
        @Override
        public Comparator<Icon> getComparator() {
            return (icon1, icon2) -> {
                int res = String.CASE_INSENSITIVE_ORDER
                        .compare(icon1.getFile().getName(), icon1.getFile().getName());

                if (res == 0) {
                    res = icon1.getFile().getName().compareTo(icon2.getFile().getName());
                }
                return res;
            };
        }
    };

    public abstract Comparator<Icon> getComparator();
}
