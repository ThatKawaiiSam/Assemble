package io.github.thatkawaiisam.assemble;

import org.bukkit.ChatColor;

public class AssembleUtils {

    public static String[] splitTeamText(String input) {
        final int inputLength = input.length();
        if (inputLength > 16) {
            // Make the prefix the first 16 characters of our text
            String prefix = input.substring(0, 16);

            // Get the last index of the color char in the prefix
            final int lastColorIndex = prefix.lastIndexOf(ChatColor.COLOR_CHAR);

            String suffix;

            if (lastColorIndex >= 14) {
                prefix = prefix.substring(0, lastColorIndex);
                suffix = ChatColor.getLastColors(input.substring(0, 17)) + input.substring(lastColorIndex + 2);
            } else {
                suffix = ChatColor.getLastColors(prefix) + input.substring(16);
            }

            if (suffix.length() > 16) {
                suffix = suffix.substring(0, 16);
            }

            return new String[] {prefix, suffix};
        } else {
            return new String[] {input, ""};
        }
    }

}
