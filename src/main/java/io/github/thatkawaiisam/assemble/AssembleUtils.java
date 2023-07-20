package io.github.thatkawaiisam.assemble;

import lombok.experimental.UtilityClass;
import org.bukkit.ChatColor;

@UtilityClass
public class AssembleUtils {

    public String[] splitTeamText(String text) {
        String prefix = "";
        String line = text.substring(0, Math.min(16, text.length()));
        String suffix = "";

        if (text.length() > 16) {
            prefix = text.substring(0, 16);

            final int lastColorPrefixIndex = prefix.lastIndexOf(ChatColor.COLOR_CHAR);

            if (lastColorPrefixIndex >= 14) {
                prefix = prefix.substring(0, lastColorPrefixIndex);
                line = ChatColor.getLastColors(text.substring(0, 17)) + text.substring(lastColorPrefixIndex + 2, 31);
            } else {
                line = ChatColor.getLastColors(prefix) + text.substring(16, Math.min(32 - ChatColor.getLastColors(prefix).length(), text.length()));
            }

            if (line.length() > 16) {
                line = line.substring(0, 16);
            }
        }

        if (text.length() > 32) {
            final int lastColorLineIndex = line.lastIndexOf(ChatColor.COLOR_CHAR);


            if (lastColorLineIndex >= 14) {
                line = line.substring(0, lastColorLineIndex);
                suffix = ChatColor.getLastColors(text.substring(17, 34)) + text.substring(lastColorLineIndex + 2);
            } else {
                suffix = ChatColor.getLastColors(line) + text.substring(30);
            }

            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }
        }

        return new String[]{prefix, line, suffix};
    }

}
