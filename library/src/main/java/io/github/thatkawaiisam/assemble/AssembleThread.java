package io.github.thatkawaiisam.assemble;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.List;

public class AssembleThread extends Thread {

    private Assemble assemble;

    AssembleThread(Assemble assemble) {
        this.assemble = assemble;
        this.start();
    }

    @Override
    public void run() {
        while(true) {
            //Tick
            try {
                tick();
            } catch (NullPointerException e) {
                e.printStackTrace();
            }
            //Thread Sleep
            try {
                sleep(assemble.getTicks() * 50);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void tick() {
        for (Player player : this.assemble.getPlugin().getServer().getOnlinePlayers()) {
            final AssembleBoard board = this.assemble.getBoards().get(player.getUniqueId());

            // This shouldn't happen, but just in case
            if (board == null) {
                continue;
            }

            final Scoreboard scoreboard = board.getScoreboard();
            final Objective objective = board.getObjective();

            // Just make a variable so we don't have to
            // process the same thing twice
            final String title = ChatColor.translateAlternateColorCodes('&', this.assemble.getAdapter().getTitle(player));

            // Update the title if needed
            if (!objective.getDisplayName().equals(title)) {
                objective.setDisplayName(title);
            }

            final List<String> newLines = this.assemble.getAdapter().getLines(player);

            // Allow adapter to return null/empty list to display nothing
            if (newLines == null || newLines.isEmpty()) {
                board.getEntries().forEach(AssembleBoardEntry::remove);
                board.getEntries().clear();
            } else {
                // Reverse the lines because scoreboard scores are in descending order
                if (!this.assemble.getAssembleStyle().isDecending()) {
                    Collections.reverse(newLines);
                }

                // Remove excessive amount of board entries
                if (board.getEntries().size() > newLines.size()) {
                    for (int i = newLines.size(); i < board.getEntries().size(); i++) {
                        final AssembleBoardEntry entry = board.getEntryAtPosition(i);

                        if (entry != null) {
                            entry.remove();
                        }
                    }
                }

                // Update existing entries / add new entries
                int cache = this.assemble.getAssembleStyle().getStartNumber();
                for (int i = 0; i < newLines.size(); i++) {
                    AssembleBoardEntry entry = board.getEntryAtPosition(i);

                    // Translate any colors
                    final String line = ChatColor.translateAlternateColorCodes('&', newLines.get(i));

                    // If the entry is null, just create a new one.
                    // Creating a new AssembleBoardEntry instance will add
                    // itself to the provided board's entries list.
                    if (entry == null) {
                        entry = new AssembleBoardEntry(board, line);
                    }

                    // Update text, setup the team, and update the display values
                    entry.setText(line);
                    entry.setup();
                    entry.send(
                            this.assemble.getAssembleStyle().isDecending() ? cache-- : cache++
                    );
                }
            }

            if (player.getScoreboard() != scoreboard) {
                player.setScoreboard(scoreboard);
            }
        }
    }
}
