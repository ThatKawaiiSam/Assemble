package io.github.thatkawaiisam.assemble;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.Collections;
import java.util.List;

public class AssembleThread extends Thread {

    private final Assemble assemble;

    /**
     * Assemble Thread.
     *
     * @param assemble instance.
     */
    AssembleThread(Assemble assemble) {
        this.assemble = assemble;
        this.start();
    }

    @Override
    public void run() {
        while(true) {
            try {
                tick();
                sleep(assemble.getTicks() * 50);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * Tick logic for thread.
     */
    private void tick() {
        for (Player player : this.assemble.getPlugin().getServer().getOnlinePlayers()) {
            try {
                AssembleBoard board = this.assemble.getBoards().get(player.getUniqueId());

                // This shouldn't happen, but just in case.
                if (board == null) {
                    continue;
                }

                Scoreboard scoreboard = board.getScoreboard();
                Objective objective = board.getObjective();

                if (scoreboard == null || objective == null) {
                    continue;
                }

                // Just make a variable so we don't have to
                // process the same thing twice.
                String title = ChatColor.translateAlternateColorCodes('&', this.assemble.getAdapter().getTitle(player));

                // Update the title if needed.
                if (!objective.getDisplayName().equals(title)) {
                    objective.setDisplayName(title);
                }

                List<String> newLines = this.assemble.getAdapter().getLines(player);

                // Allow adapter to return null/empty list to display nothing.
                if (newLines == null || newLines.isEmpty()) {
                    board.getEntries().forEach(AssembleBoardEntry::remove);
                    board.getEntries().clear();
                } else {
                    if (newLines.size() > 15) {
                        newLines = newLines.subList(0, 15);
                    }

                    // Reverse the lines because scoreboard scores are in descending order.
                    if (!this.assemble.getAssembleStyle().isDescending()) {
                        Collections.reverse(newLines);
                    }

                    // Remove excessive amount of board entries.
                    if (board.getEntries().size() > newLines.size()) {
                        for (int i = newLines.size(); i < board.getEntries().size(); i++) {
                            AssembleBoardEntry entry = board.getEntryAtPosition(i);

                            if (entry != null) {
                                entry.remove();
                            }
                        }
                    }

                    // Update existing entries / add new entries.
                    int cache = this.assemble.getAssembleStyle().getStartNumber();
                    for (int i = 0; i < newLines.size(); i++) {
                        AssembleBoardEntry entry = board.getEntryAtPosition(i);

                        // Translate any colors.
                        String line = ChatColor.translateAlternateColorCodes('&', newLines.get(i));

                        // If the entry is null, just create a new one.
                        // Creating a new AssembleBoardEntry instance will add
                        // itself to the provided board's entries list.
                        if (entry == null) {
                            entry = new AssembleBoardEntry(board, line, i);
                        }

                        // Update text, setup the team, and update the display values.
                        entry.setText(line);
                        entry.setup();
                        entry.send(
                                this.assemble.getAssembleStyle().isDescending() ? cache-- : cache++
                        );
                    }
                }

                if (player.getScoreboard() != scoreboard && !assemble.isHook()) {
                    player.setScoreboard(scoreboard);
                }
            } catch(Exception e) {
                e.printStackTrace();
                throw new AssembleException("There was an error updating " + player.getName() + "'s scoreboard.");
            }
        }
    }

}
