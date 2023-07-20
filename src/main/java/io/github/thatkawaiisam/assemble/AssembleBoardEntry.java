package io.github.thatkawaiisam.assemble;

import lombok.Setter;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

public class AssembleBoardEntry {

	private final AssembleBoard board;

	private Team team;
	@Setter
	private String text;

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

		String[] split = AssembleUtils.splitTeamText(text);

		Team team = scoreboard.getTeam(split[1]);

		// Register the team if it does not exist.
		if (team == null) {
			team = scoreboard.registerNewTeam(split[1]);
		}

		// Add the entry to the team.
		if (!team.getEntries().contains(team.getName())) {
			team.addEntry(team.getName());
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
		String[] split = AssembleUtils.splitTeamText(text);

		// Set Prefix & Suffix.
		String prefix = split[0];
		String suffix = split[2];

		if (prefix != null) {
			team.setPrefix(prefix);
		}

		if (suffix != null) {
			team.setSuffix(suffix);
		}

		// Set the score
		this.board.getObjective().getScore(team.getName()).setScore(position);
	}

	/**
	 * Remove Board Entry from Board.
	 */
	public void remove() {
		this.board.getScoreboard().resetScores(team.getName());
	}

}