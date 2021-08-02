package io.github.thatkawaiisam.assemble;

import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

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
		if (this.text.length() > 16) {
			// Make the prefix the first 16 characters of our text
			String prefix = this.text.substring(0, 16);
			
			// Make the suffix the next 16 characters, capping it (30 cap because indexes start from 0)
			String suffix = ChatColor.getLastColors(prefix) + this.text.substring(16, Math.min(this.text.length(), 30));

			// Get the last index of the color char
			final int lastColorIndex = prefix.lastIndexOf(ChatColor.COLOR_CHAR);
			
			// Cut off trailing color code *after* getting the color for the suffix
			// This is done after we set the suffix, so that we still retain these colors in the suffix
			if (lastColorIndex >= 14)
				prefix = prefix.substring(0, lastColorIndex);

			this.team.setPrefix(prefix);
			this.team.setSuffix(suffix);
		} else {
			this.team.setPrefix(this.text);
			this.team.setSuffix("");
		}

		Score score = this.board.getObjective().getScore(this.identifier);
		score.setScore(position);
	}

	/**
	 * Remove Board Entry from Board.
	 */
	public void remove() {
		this.board.getIdentifiers().remove(this.identifier);
		this.board.getScoreboard().resetScores(this.identifier);
	}

}
