package io.github.thatkawaiisam.assemble;

import io.github.thatkawaiisam.assemble.events.AssembleBoardCreateEvent;
import io.github.thatkawaiisam.assemble.events.AssembleBoardDestroyEvent;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.server.PluginDisableEvent;

@Getter
public class AssembleListener implements Listener {

	private Assemble assemble;

	public AssembleListener(Assemble assemble) {
		this.assemble = assemble;
	}

	@EventHandler
	public void onPlayerJoin(PlayerJoinEvent event) {
		AssembleBoardCreateEvent createEvent = new AssembleBoardCreateEvent(event.getPlayer());

		Bukkit.getPluginManager().callEvent(createEvent);
		if (createEvent.isCancelled()) {
			return;
		}

		getAssemble().getBoards().put(event.getPlayer().getUniqueId(), new AssembleBoard(event.getPlayer(), getAssemble()));
	}

	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		AssembleBoardDestroyEvent destroyEvent = new AssembleBoardDestroyEvent(event.getPlayer());

		Bukkit.getPluginManager().callEvent(destroyEvent);
		if (destroyEvent.isCancelled()) {
			return;
		}

		getAssemble().getBoards().remove(event.getPlayer().getUniqueId());
		event.getPlayer().setScoreboard(Bukkit.getScoreboardManager().getMainScoreboard());
	}

	@EventHandler
	public void onPluginDisable(PluginDisableEvent event) {
		if (event.getPlugin().getName().equals(assemble.getPlugin().getName())) {
			assemble.setdown();
		}
	}

}
