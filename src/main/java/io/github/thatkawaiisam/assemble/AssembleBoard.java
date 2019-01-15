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

@Getter
public class AssembleBoard {

	// We assign a unique identifier (random string of ChatColor values)
	// to each board entry to: bypass the 32 char limit, using
	// a team's prefix & suffix and a team entry's display name, and to
	// track the order of entries;
	private final List<AssembleBoardEntry> entries = new ArrayList<>();
	private final List<String> identifiers = new ArrayList<>();
	private Scoreboard scoreboard;
	private Objective objective;
	private UUID uuid;

	private Assemble assemble;

	public AssembleBoard(Player player, Assemble assemble) {
		this.assemble = assemble;
		this.setup(player);
		this.uuid = player.getUniqueId();
	}

	private void setup(Player player) {
		// Register new scoreboard if needed
		if (getAssemble().isHook() || !(player.getScoreboard() == Bukkit.getScoreboardManager().getMainScoreboard())) {
			this.scoreboard = player.getScoreboard();
		} else {
			this.scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
		}

		// Setup sidebar objective
		this.objective = this.scoreboard.registerNewObjective("Default", "dummy");
		this.objective.setDisplaySlot(DisplaySlot.SIDEBAR);
		this.objective.setDisplayName(getAssemble().getAdapter().getTitle(player));

		// Update scoreboard
		player.setScoreboard(this.scoreboard);

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

	public String getUniqueIdentifier(String text) {
		String identifier = getRandomChatColor() + ChatColor.WHITE;

		while (this.identifiers.contains(identifier)) {
			identifier = identifier + getRandomChatColor() + ChatColor.WHITE;
		}

		// This is rare, but just in case, make the method recursive
		if (identifier.length() > 16) {
			return this.getUniqueIdentifier(text);
		}

		// Add our identifier to the list so there are no duplicates
		this.identifiers.add(identifier);

		return identifier;
	}

	// Gets a random ChatColor and returns the String value of it
	private static String getRandomChatColor() {
		return ChatColor.values()[ThreadLocalRandom.current().nextInt(ChatColor.values().length)].toString();
	}

}
