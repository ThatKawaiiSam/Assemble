package io.github.thatkawaiisam.assemble;

import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import lombok.Setter;

public class AssembleBoardEntry {

	private final AssembleBoard board;

	private Team team;
	@Setter
	private String text, identifier;

	/**
	 * Assemble Board Entry
	 *
	 * @param board    that entry belongs to.
	 * @param text     of entry.
	 * @param position of entry.
	 */
	public AssembleBoardEntry(AssembleBoard board, String text, int position) {
		this.board = board;
		this.text = text;
		this.identifier = this.board.getUniqueIdentifier(position);

		this.setup();
	}

	/**
	 * Setup Board Entry.
	 */
	public void setup() {
		final Scoreboard scoreboard = this.board.getScoreboard();

		if (scoreboard == null) {
			return;
		}

		String teamName = this.identifier;

		// This shouldn't happen, but just in case.
		if (teamName.length() > 16) {
			teamName = teamName.substring(0, 16);
		}

		Team team = scoreboard.getTeam(teamName);

		// Register the team if it does not exist.
		if (team == null) {
			team = scoreboard.registerNewTeam(teamName);
		}

		// Add the entry to the team.
		if (!team.getEntries().contains(this.identifier)) {
			team.addEntry(this.identifier);
		}

		// Add the entry if it does not exist.
		if (!this.board.getEntries().contains(this)) {
			this.board.getEntries().add(this);
		}

		this.team = team;
	}

	/**
	 * Send Board Entry Update.
	 *
	 * @param position of entry.
	 */
	public void send(int position) {
		final int textLength = text.length();
		if (textLength > 16) {
			// Make the prefix the first 16 characters of our text
			String prefix = this.text.substring(0, 16);

			// Get the last index of the color char in the prefix
			final int lastColorIndex = prefix.lastIndexOf(ChatColor.COLOR_CHAR);

			String suffix;

			if (lastColorIndex >= 14) {
				prefix = prefix.substring(0, lastColorIndex);
				suffix = ChatColor.getLastColors(this.text.substring(0, 17)) + this.text.substring(lastColorIndex + 2);
			} else {
				suffix = ChatColor.getLastColors(prefix) + this.text.substring(16);
			}

			if (suffix.length() > 16) {
				suffix = suffix.substring(0, 16);
			}

			this.team.setPrefix(prefix);
			this.team.setSuffix(suffix);
		} else {
			this.team.setPrefix(this.text);
			this.team.setSuffix("");
		}

		// Set the score
		this.board.getObjective().getScore(this.identifier).setScore(position);
	}

	/**
	 * Remove Board Entry from Board.
	 */
	public void remove() {
		this.board.getIdentifiers().remove(this.identifier);
		this.board.getScoreboard().resetScores(this.identifier);
	}

}
