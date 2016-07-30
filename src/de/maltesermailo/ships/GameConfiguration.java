package de.maltesermailo.ships;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.World.Environment;
import org.bukkit.WorldCreator;
import org.bukkit.configuration.file.FileConfiguration;

public class GameConfiguration {
	
	private FileConfiguration cfg;
	
	private Location teamSpawn1;
	private Location teamSpawn2;
	
	private Location teamTreasure1;
	private Location teamTreasure2;
	
	private String ship1;
	private String ship2;
	
	private World world;
	
	private Location lobbySpawn;
	
	public GameConfiguration() {
		this.cfg = ShipsPlugin.instance().getConfig();
		
		this.cfg.addDefault("locations.teamSpawn1.x", 0D);
		this.cfg.addDefault("locations.teamSpawn1.y", 0D);
		this.cfg.addDefault("locations.teamSpawn1.z", 0D);
		this.cfg.addDefault("locations.teamSpawn1.yaw", 0F);
		this.cfg.addDefault("locations.teamSpawn1.pitch", 0F);
		this.cfg.addDefault("locations.teamSpawn1.world", "world");
		
		this.cfg.addDefault("locations.teamSpawn2.x", 0D);
		this.cfg.addDefault("locations.teamSpawn2.y", 0D);
		this.cfg.addDefault("locations.teamSpawn2.z", 0D);
		this.cfg.addDefault("locations.teamSpawn2.yaw", 0F);
		this.cfg.addDefault("locations.teamSpawn2.pitch", 0F);
		this.cfg.addDefault("locations.teamSpawn2.world", "world");
		
		this.cfg.addDefault("locations.teamTreasure1.x", 0D);
		this.cfg.addDefault("locations.teamTreasure1.y", 0D);
		this.cfg.addDefault("locations.teamTreasure1.z", 0D);
		this.cfg.addDefault("locations.teamTreasure1.yaw", 0F);
		this.cfg.addDefault("locations.teamTreasure1.pitch", 0F);
		this.cfg.addDefault("locations.teamTreasure1.world", "world");
		
		this.cfg.addDefault("locations.teamTreasure2.x", 0D);
		this.cfg.addDefault("locations.teamTreasure2.y", 0D);
		this.cfg.addDefault("locations.teamTreasure2.z", 0D);
		this.cfg.addDefault("locations.teamTreasure2.yaw", 0F);
		this.cfg.addDefault("locations.teamTreasure2.pitch", 0F);
		this.cfg.addDefault("locations.teamTreasure2.world", "world");
		
		this.cfg.addDefault("locations.ship1.region", "ship1");
		this.cfg.addDefault("locations.ship2.region", "ship2");
		
		this.cfg.addDefault("locations.world", "world");
		
		this.cfg.addDefault("locations.lobby.x", 0D);
		this.cfg.addDefault("locations.lobby.y", 0D);
		this.cfg.addDefault("locations.lobby.z", 0D);
		this.cfg.addDefault("locations.lobby.yaw", 0F);
		this.cfg.addDefault("locations.lobby.pitch", 0F);
		this.cfg.addDefault("locations.lobby.world", "world");
		
		this.cfg.options().copyDefaults(true);
		
		this.saveConfiguration();
	}
	
	public void loadConfiguration() {
		this.teamSpawn1 = this.loadLocation("locations.teamSpawn1");
		this.teamSpawn2 = this.loadLocation("locations.teamSpawn2");
		
		this.teamTreasure1 = this.loadLocation("locations.teamTreasure1");
		this.teamTreasure2 = this.loadLocation("locations.teamTreasure2");
		
		this.lobbySpawn = this.loadLocation("locations.lobby");
		
		this.ship1 = this.cfg.getString("locations.ship1.region");
		this.ship2 = this.cfg.getString("locations.ship2.region");
		
		String worldName = this.cfg.getString("locations.world");
		
		if(Bukkit.getWorld(worldName) == null) {
			new WorldCreator(worldName).environment(Environment.NORMAL).createWorld();
		}
		
		this.world = Bukkit.getWorld(worldName);
	}
	
	public void saveConfiguration() {
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
	
	public Location getTeamSpawn1() {
		return teamSpawn1;
	}
	
	public void setTeamSpawn1(Location teamSpawn1) {
		this.teamSpawn1 = teamSpawn1;
	}
	
	public Location getTeamSpawn2() {
		return teamSpawn2;
	}
	
	public void setTeamSpawn2(Location teamSpawn2) {
		this.teamSpawn2 = teamSpawn2;
	}
	
	public Location getTeamTreasure1() {
		return teamTreasure1;
	}
	
	public void setTeamTreasure1(Location teamTreasure1) {
		this.teamTreasure1 = teamTreasure1;
	}
	
	public Location getTeamTreasure2() {
		return teamTreasure2;
	}
	
	public void setTeamTreasure2(Location teamTreasure2) {
		this.teamTreasure2 = teamTreasure2;
	}
	
	public Location getLobbySpawn() {
		return lobbySpawn;
	}
	
	public void setLobbySpawn(Location lobbySpawn) {
		this.lobbySpawn = lobbySpawn;
	}
	
	public String getShipRegion1() {
		return ship1;
	}
	
	public String getShipRegion2() {
		return ship2;
	}
	
	public World getWorld() {
		return world;
	}

}
