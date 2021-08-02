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

			// Get the last index of the color char
			final int lastColorIndex = prefix.lastIndexOf(ChatColor.COLOR_CHAR);

			final String colors;

			// Cut off trailing color code *after* getting the color for the suffix
			// This is done after we set the suffix, so that we still retain suffix color
			if (lastColorIndex >= 14) {
				// Get any potential colors that could be cut off
				colors = ChatColor.getLastColors(this.text.substring(0, 17));
				prefix = prefix.substring(0, lastColorIndex);
			} else {
				// Get colors from string
				colors = ChatColor.getLastColors(prefix);
			}

			// Make the suffix the next 16 characters, capping it
			final String suffix = colors + this.text.substring(prefix.length(), Math.min(textLength, 31 - colors.length()));

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
