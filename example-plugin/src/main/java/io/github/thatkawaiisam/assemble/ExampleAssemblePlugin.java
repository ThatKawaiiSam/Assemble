package io.github.thatkawaiisam.assemble;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Testing possible issues with library
 */
public class ExampleAssemblePlugin extends JavaPlugin {

	@Override
	public void onEnable() {
		//This stuff is the same
		new Assemble(this, new AssembleAdapter() {
			@Override
			public String getTitle(Player player) {
				return ChatColor.GOLD.toString() + "Assemble Board Example";
			}

			@Override
			public List<String> getLines(Player player) {
				final int rand = ThreadLocalRandom.current().nextInt(500);
				final List<String> toReturn = new ArrayList<>();

				toReturn.add("&eThis is an example board.");
				toReturn.add("&dAs you can see, it supports");
				toReturn.add("&dup to 32 characters per line.");
				toReturn.add("&aDon't forget... Colors count");
				toReturn.add("&btowards the character count.");
				toReturn.add("Random number: " + rand);
				toReturn.add("");
				toReturn.add("Credits to Joelioli!");

				return toReturn;
			}
		});

		//This stuff is new
		Assemble.getInstance().setAssembleStyle(AssembleStyle.VIPER);
		Assemble.getInstance().setTicks(2);
	}

}
