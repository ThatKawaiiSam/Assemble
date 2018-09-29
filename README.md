# Assemble
A high performance Scoreboard API for Spigot 1.7.x to 1.13
All credit goes to Joeleoli on the original plugin which you can find here: https://github.com/joeleoli/Frame

Essentially several production projects that I am apart of required very high performance components to support hundreds of concurrent players per instance. The original version of this plugin gave us this performance with the tradeoff of small features that we needed to further optimise it and give it a unique feel. So I decided to open-source my fork to build my portfolio. 

### Features
* Non-flickering.
* Supports up to 32 characters per line.
* Extremely light weight.
* Developer friendly. Setup your board in a few lines.
* Custom Events for other plugins to easily hook into.
* Several different styles (Viper, Kohi, Modern)
* Changable timing intervals

### Code Examples
```
@Override
public void onEnable() {
        //Start Instance
	Assemble assemble = new Assemble(new ExampleAssembleAdapter());
	
	//Set Interval (Tip: 20 ticks = 1 second)
	assemble.setTicks(2);
	
	//Set Style (Tip: Viper Style starts at -1 and goes down)
	assemble.setAssembleStyle(AssembleStyle.VIPER);
}
```
```
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
```
public class AssembleSampleListeners implements Listener {

    @EventHandler
    public void onBoardCreate(AssembleBoardCreateEvent event) {
        //Do what you want with the player object
    }
}
```

###Contact
MC-Market: https://www.mc-market.org/members/53967/

Discord: ThatKawaiiSam#3881  

Email: sam@orionmc.net

