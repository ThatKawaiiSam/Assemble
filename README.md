# Assemble
> Highly performant Scoreboard library for Spigot 1.7.x to 1.17.x.
 
Credit goes to [Joeleoli](https://github.com/joeleoli) for the original base of this library.

---

## Features
* Non-flickering.
* Supports up to 32 characters per line.
* Extremely light weight.
* Developer friendly - Setup your board in a few lines.
* Custom Events for other plugins to easily hook into.
* Several different styles numbering styles (Viper, Kohi, Modern).
* Customizable styling
* Configurable timing intervals.

---

## Code Examples

#### Setup Assemble
```java
public class ExamplePlugin extends JavaPlugin {

    @Override
    public void onEnable() {
        // Start Instance.
        Assemble assemble = new Assemble(this, new ExampleAssembleAdapter());
        
        // Set Interval (Tip: 20 ticks = 1 second).
        assemble.setTicks(2);
        
        // Set Style (Tip: Viper Style starts at -1 and goes down).
        assemble.setAssembleStyle(AssembleStyle.VIPER);
    }

}
```

#### Assemble Adapter
```java
public class ExampleAssembleAdapter implements AssembleAdapter {

	@Override
	public String getTitle(Player player) {
		return ChatColor.GOLD.toString() + "Assemble Board Example";
	}

	@Override
	public List<String> getLines(Player player) {
		final List<String> toReturn = new ArrayList<>();

		toReturn.add("&a&lThis is a line!");

		return toReturn;
	}

}
```

#### Assemble Event Listener
```java
public class AssembleSampleListeners implements Listener {

    @EventHandler
    public void onBoardCreate(AssembleBoardCreateEvent event) {
        // Do what you want with the player object.
    }
}
```

```java
@Override
public void onEnable() {
        // Start Instance.
	Assemble assemble = new Assemble(this, new ExampleAssembleAdapter());
	
	// Set Interval (Tip: 20 ticks = 1 second).
	assemble.setTicks(2);
	
	// Set Custom Style (Tip: This will set the scoreboard scores to range from 8 to -7)
	assemble.setAssembleStyle(AssembleStyle.CUSTOM.descending(true).startNumber(8));
}
```

---

## Contributing
When contributing, please create a pull request with the branch named as follows ``<feature/fix>/<title>``.

To compile, run the maven command: ``mvn clean install``

To run unit tests, run the maven command ``mvn test``

---

## Contact

- Discord: ThatKawaiiSam#2882
- Telegram: ThatKawaiiSam
