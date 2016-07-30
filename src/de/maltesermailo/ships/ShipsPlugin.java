package de.maltesermailo.ships;

import org.bukkit.Bukkit;
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
		Bukkit.getLogger().info("[ShipsGame] Preparing...");
		
		this.game = new ShipsGame();
		this.game.prepare();
	}
	
	@Override
	public void onDisable() {
		Bukkit.getLogger().info("[ShipsGame] Disabled.");
	}
	
	public String getPrefix() {
		return prefix;
	}
	
	public ShipsGame getGame() {
		return game;
	}

}
