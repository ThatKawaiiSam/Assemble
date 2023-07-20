package io.github.thatkawaiisam.assemble;

import io.github.thatkawaiisam.assemble.events.AssembleBoardCreatedEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Getter
public class AssembleBoard {

	private final Assemble assemble;

	private final List<AssembleBoardEntry> entries = new ArrayList<>();

	private final UUID uuid;

	/**
	 * Assemble Board.
	 *
	 * @param player that the board belongs to.
	 * @param assemble instance.
	 */
	public AssembleBoard(Player player, Assemble assemble) {
		this.uuid = player.getUniqueId();
		this.assemble = assemble;
		this.setup(player);
	}

	/**
	 * Get's a player's bukkit scoreboard.
	 *
	 * @return either existing scoreboard or new scoreboard.
	 */
	public Scoreboard getScoreboard() {
		Player player = Bukkit.getPlayer(getUuid());
		if (getAssemble().isHook() || player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
			return player.getScoreboard();
		} else {
			return Bukkit.getScoreboardManager().getNewScoreboard();
		}
	}

	/**
	 * Get's the player's scoreboard objective.
	 *
	 * @return either existing objecting or new objective.
	 */
	public Objective getObjective() {
		Scoreboard scoreboard = getScoreboard();
		if (scoreboard.getObjective("Assemble") == null) {
			Objective objective = scoreboard.registerNewObjective("Assemble", "dummy");
			objective.setDisplaySlot(DisplaySlot.SIDEBAR);
			objective.setDisplayName(getAssemble().getAdapter().getTitle(Bukkit.getPlayer(getUuid())));
			return objective;
		} else {
			return scoreboard.getObjective("Assemble");
		}
	}

	/**
	 * Setup the board.
	 *
	 * @param player who's board to setup.
	 */
	private void setup(Player player) {
		Scoreboard scoreboard = getScoreboard();
		player.setScoreboard(scoreboard);
		getObjective();

		// Call Events if enabled.
		if (assemble.isCallEvents()) {
			AssembleBoardCreatedEvent createdEvent = new AssembleBoardCreatedEvent(this);
			Bukkit.getPluginManager().callEvent(createdEvent);
		}
	}

	/**
	 * Get the board entry at a specific position.
	 *
	 * @param pos to find entry.
	 * @return entry if it isn't out of range.
	 */
	public AssembleBoardEntry getEntryAtPosition(int pos) {
		return pos >= this.entries.size() ? null : this.entries.get(pos);
	}

}