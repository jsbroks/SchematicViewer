package me.jsbroks.schematicviewer.utils;

import org.bukkit.ChatColor;

public class TextUtil {
    public static String colorize(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String wrapString(String string, int charWrap) {
        int lastBreak = 0;
        int nextBreak = charWrap;
        if (string.length() > charWrap) {
            String setString = "";
            do {
                while (string.charAt(nextBreak) != ' ' && nextBreak > lastBreak) {
                    nextBreak--;
                }
                if (nextBreak == lastBreak) {
                    nextBreak = lastBreak + charWrap;
                }
                setString += string.substring(lastBreak, nextBreak).trim() + "\n";
                lastBreak = nextBreak;
                nextBreak += charWrap;

            } while (nextBreak < string.length());
            setString += string.substring(lastBreak).trim();
            return setString;
        } else {
            return string;
        }
    }
}
