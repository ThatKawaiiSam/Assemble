package io.github.thatkawaiisam.assemble;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;

import io.github.thatkawaiisam.assemble.events.AssembleBoardCreatedEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

public class AssembleBoard {

	// We assign a unique identifier (random string of ChatColor values)
	// to each board entry to: bypass the 32 char limit, using
	// a team's prefix & suffix and a team entry's display name, and to
	// track the order of entries;
	@Getter private final List<AssembleBoardEntry> entries = new ArrayList<>();
	@Getter private final List<String> identifiers = new ArrayList<>();
	@Getter private final UUID uuid;

	@Getter private Assemble assemble;

	public AssembleBoard(Player player, Assemble assemble) {
		this.uuid = player.getUniqueId();
		this.assemble = assemble;
		this.setup(player);
	}

	public Scoreboard getScoreboard() {
		Player player = Bukkit.getPlayer(getUuid());
		if (getAssemble().isHook() || player.getScoreboard() != Bukkit.getScoreboardManager().getMainScoreboard()) {
			return player.getScoreboard();
		} else {
			return Bukkit.getScoreboardManager().getNewScoreboard();
		}
	}

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


	private void setup(Player player) {
		Scoreboard scoreboard = getScoreboard();
		player.setScoreboard(scoreboard);
		getObjective();

		//Send Update
		AssembleBoardCreatedEvent createdEvent = new AssembleBoardCreatedEvent(this);
		Bukkit.getPluginManager().callEvent(createdEvent);
	}

	public AssembleBoardEntry getEntryAtPosition(int pos) {
		if (pos >= this.entries.size()) {
			return null;
		} else {
			return this.entries.get(pos);
		}
	}

	public String getUniqueIdentifier(int position) {
		String identifier = getRandomChatColor(position) + ChatColor.WHITE;

		while (this.identifiers.contains(identifier)) {
			identifier = identifier + getRandomChatColor(position) + ChatColor.WHITE;
		}

		// This is rare, but just in case, make the method recursive
		if (identifier.length() > 16) {
			return this.getUniqueIdentifier(position);
		}

		// Add our identifier to the list so there are no duplicates
		this.identifiers.add(identifier);

		return identifier;
	}

	// Gets a random ChatColor and returns the String value of it
	private static String getRandomChatColor(int position) {
		return ChatColor.values()[position].toString();
	}

}
