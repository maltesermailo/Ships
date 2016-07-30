package de.maltesermailo.ships;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.block.BlockState;
import org.bukkit.plugin.java.JavaPlugin;

public class ShipsPlugin extends JavaPlugin {
	
	private static ShipsPlugin instance;
	
	public static ShipsPlugin instance() {
		return ShipsPlugin.instance;
	}
	
	private String prefix = "§8[§cShips§8]";
	
	private ShipsGame game;
	
	@Override
	public void onLoad() {
		ShipsPlugin.instance = this;
	}
	
	@Override
	public void onEnable() {
		Bukkit.getLogger().info("[Ships] Preparing...");
		
		this.game = new ShipsGame();
		this.game.prepare();
	}
	
	@Override
	public void onDisable() {
		for(Location loc : this.game.getPlayerBlocks().keySet()) {
			BlockState originalState = this.game.getPlayerBlocks().get(loc);
			
			//Force update block
			originalState.update(true, true);
		}
		
		for(World w : Bukkit.getWorlds()) {
			Bukkit.unloadWorld(w, false);
		}
		
		Bukkit.getLogger().info("[Ships] Disabled.");
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public ShipsGame getGame() {
		return game;
	}

}
