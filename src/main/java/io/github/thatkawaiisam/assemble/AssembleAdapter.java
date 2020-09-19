package io.github.thatkawaiisam.assemble;

import java.util.List;
import org.bukkit.entity.Player;

public interface AssembleAdapter {

	/**
	 * Get's the scoreboard title.
	 *
	 * @param player who's title is being displayed.
	 * @return title.
	 */
	String getTitle(Player player);

	/**
	 * Get's the scoreboard lines.
	 *
	 * @param player who's lines are being displayed.
	 * @return lines.
	 */
	List<String> getLines(Player player);

}
