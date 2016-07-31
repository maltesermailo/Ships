package de.maltesermailo.ships.game;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;

import de.maltesermailo.ships.ShipsPlugin;

public class GameConfiguration {
	
	private FileConfiguration cfg;
	
	private Location teamSpawnBlue;
	private Location teamSpawnRed;
	
	private Location teamTreasureBlue;
	private Location teamTreasureRed;
	
	private String ship1;
	private String ship2;
	
	private World world;
	
	private Location lobbySpawn;
	
	public GameConfiguration() {
		this.cfg = ShipsPlugin.instance().getConfig();
		
		this.cfg.addDefault("locations.teamSpawnBlue.x", 0D);
		this.cfg.addDefault("locations.teamSpawnBlue.y", 0D);
		this.cfg.addDefault("locations.teamSpawnBlue.z", 0D);
		this.cfg.addDefault("locations.teamSpawnBlue.yaw", 0F);
		this.cfg.addDefault("locations.teamSpawnBlue.pitch", 0F);
		this.cfg.addDefault("locations.teamSpawnBlue.world", "world");
		
		this.cfg.addDefault("locations.teamSpawnRed.x", 0D);
		this.cfg.addDefault("locations.teamSpawnRed.y", 0D);
		this.cfg.addDefault("locations.teamSpawnRed.z", 0D);
		this.cfg.addDefault("locations.teamSpawnRed.yaw", 0F);
		this.cfg.addDefault("locations.teamSpawnRed.pitch", 0F);
		this.cfg.addDefault("locations.teamSpawnRed.world", "world");
		
		this.cfg.addDefault("locations.teamTreasureBlue.x", 0D);
		this.cfg.addDefault("locations.teamTreasureBlue.y", 0D);
		this.cfg.addDefault("locations.teamTreasureBlue.z", 0D);
		this.cfg.addDefault("locations.teamTreasureBlue.yaw", 0F);
		this.cfg.addDefault("locations.teamTreasureBlue.pitch", 0F);
		this.cfg.addDefault("locations.teamTreasureBlue.world", "world");
		
		this.cfg.addDefault("locations.teamTreasureRed.x", 0D);
		this.cfg.addDefault("locations.teamTreasureRed.y", 0D);
		this.cfg.addDefault("locations.teamTreasureRed.z", 0D);
		this.cfg.addDefault("locations.teamTreasureRed.yaw", 0F);
		this.cfg.addDefault("locations.teamTreasureRed.pitch", 0F);
		this.cfg.addDefault("locations.teamTreasureRed.world", "world");
		
		this.cfg.addDefault("locations.shipBlue.region", "shipBlue");
		this.cfg.addDefault("locations.shipRed.region", "shipRed");
		
		this.cfg.addDefault("locations.world", "world");
		
		this.cfg.addDefault("locations.lobby.x", 0D);
		this.cfg.addDefault("locations.lobby.y", 0D);
		this.cfg.addDefault("locations.lobby.z", 0D);
		this.cfg.addDefault("locations.lobby.yaw", 0F);
		this.cfg.addDefault("locations.lobby.pitch", 0F);
		this.cfg.addDefault("locations.lobby.world", "world");
		
		this.cfg.options().copyDefaults(true);
		
		ShipsPlugin.instance().saveConfig();
	}
	
	public void loadConfiguration() {
		this.teamSpawnBlue = this.loadLocation("locations.teamSpawnBlue");
		this.teamSpawnRed = this.loadLocation("locations.teamSpawnRed");
		
		this.teamTreasureBlue = this.loadLocation("locations.teamTreasureBlue");
		this.teamTreasureRed = this.loadLocation("locations.teamTreasureRed");
		
		this.lobbySpawn = this.loadLocation("locations.lobby");
		
		this.ship1 = this.cfg.getString("locations.shipBlue.region");
		this.ship2 = this.cfg.getString("locations.shipRed.region");
		
		String worldName = this.cfg.getString("locations.world");
		
		if(Bukkit.getWorld(worldName) == null) {
			new WorldCreator(worldName).environment(Environment.NORMAL).createWorld();
		}
		
		this.world = Bukkit.getWorld(worldName);
	}
	
	public void saveConfiguration() {
		this.saveLocation("locations.teamSpawnBlue", this.teamSpawnBlue);
		this.saveLocation("locations.teamSpawnRed", this.teamSpawnRed);
		this.saveLocation("locations.teamTreasureBlue", this.teamTreasureBlue);
		this.saveLocation("locations.teamTreasureRed", this.teamTreasureRed);
		this.saveLocation("locations.lobby", this.lobbySpawn);
		
		ShipsPlugin.instance().saveConfig();
	}
	
	public Location loadLocation(String path) {
		double x = this.cfg.getDouble(path + ".x", 0);
		double y = this.cfg.getDouble(path + ".y", 0);
		double z = this.cfg.getDouble(path + ".z", 0);
		
		float yaw = (float) this.cfg.getDouble(path + ".yaw", 0);
		float pitch = (float) this.cfg.getDouble(path + ".pitch", 0);
		
		String worldName = this.cfg.getString(path + ".world", "world");
		
		if(Bukkit.getWorld(worldName) == null) {
			Bukkit.createWorld(new WorldCreator(worldName).environment(Environment.NORMAL));
		}
		
		World world = Bukkit.getWorld(worldName);
		
		return new Location(world, x, y, z, yaw, pitch);
	}
	
	public void saveLocation(String path, Location loc) {
		this.cfg.set(path + ".x", loc.getX());
		this.cfg.set(path + ".y", loc.getY());
		this.cfg.set(path + ".z", loc.getZ());
		
		this.cfg.set(path + ".yaw", loc.getYaw());
		this.cfg.set(path + ".pitch", loc.getPitch());
		
		this.cfg.set(path + ".world", loc.getWorld().getName());
	}
	
	public Location getTeamSpawnBlue() {
		return teamSpawnBlue;
	}
	
	public void setTeamSpawnBlue(Location loc) {
		this.teamSpawnBlue = loc;
	}
	
	public Location getTeamSpawnRed() {
		return teamSpawnRed;
	}
	
	public void setTeamSpawnRed(Location loc) {
		this.teamSpawnRed = loc;
	}
	
	public Location getTeamTreasureBlue() {
		return teamTreasureBlue;
	}
	
	public void setTeamTreasureBlue(Location loc) {
		this.teamTreasureBlue = loc;
	}
	
	public Location getTeamTreasureRed() {
		return teamTreasureRed;
	}
	
	public void setTeamTreasureRed(Location loc) {
		this.teamTreasureRed = loc;
	}
	
	public Location getLobbySpawn() {
		return lobbySpawn;
	}
	
	public void setLobbySpawn(Location loc) {
		this.lobbySpawn = loc;
	}
	
	public String getShipRegionBlue() {
		return ship1;
	}
	
	public String getShipRegionRed() {
		return ship2;
	}
	
	public World getWorld() {
		return world;
	}

}
