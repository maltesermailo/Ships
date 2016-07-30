package de.maltesermailo.ships;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.google.common.collect.Lists;
import com.sk89q.worldedit.regions.Region;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.maltesermailo.ships.listener.ListenerBrew;
import de.maltesermailo.ships.listener.ListenerCraftItem;
import de.maltesermailo.ships.listener.ListenerFoodLevelChange;
import de.maltesermailo.ships.listener.ListenerFurnaceBurn;
import de.maltesermailo.ships.listener.ListenerPlayerAchievement;
import de.maltesermailo.ships.listener.ListenerArmorStandManipulate;
import de.maltesermailo.ships.listener.ListenerPlayerBedEnter;
import de.maltesermailo.ships.listener.ListenerPlayerFish;
import de.maltesermailo.ships.listener.ListenerWeatherChange;
import de.maltesermailo.ships.listener.game.ListenerEntityShootArrow;
import de.maltesermailo.ships.listener.game.ListenerPlayerInteract;
import de.maltesermailo.ships.listener.game.ListenerProjectileHit;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyBlockBreak;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyBlockBurn;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyBlockExplode;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyBlockPlace;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyDropItem;
import de.maltesermailo.ships.listener.lobby.ListenerPlayerJoin;
import de.maltesermailo.ships.listener.lobby.ListenerPlayerQuit;

public class ShipsGame {
	
	/*                Scoreboard Block                */
    private Scoreboard bukkitSB;
	
	private Team teamBlue;
	private Team teamRed;
	
	private Objective shipLifes;
	
	private Score scoreShipBlue;
	private Score scoreShipRed;
	/*////////////////////////////////////////////////*/
	
	private GameConfiguration gameConfiguration;
	
	private HashMap<Player, CooldownManager> cooldowns;
	
	private ProtectedRegion shipBlue;
	private ProtectedRegion shipRed;
	
	private ItemStack cannonBall;
	private ItemStack bowExplosion;
	private ItemStack bowFire;
	
	private GameState currentState;
	
	/**
	 * The constructor prepares configuration and other information
	 */
	public ShipsGame() {
		this.bukkitSB = Bukkit.getScoreboardManager().getMainScoreboard();
		
		if(this.bukkitSB.getTeam("blue") != null) {
			this.bukkitSB.getTeam("blue").unregister();
		}
		
		if(this.bukkitSB.getTeam("red") != null) {
			this.bukkitSB.getTeam("red").unregister();
		}
		
		this.teamBlue = this.bukkitSB.registerNewTeam("blue");
		this.teamBlue.setPrefix("§bBlau | ");
		
		this.teamRed = this.bukkitSB.registerNewTeam("red");
		this.teamRed.setPrefix("§bRot | ");
		
		if(this.bukkitSB.getObjective("shipLifes") != null) {
			this.bukkitSB.getObjective("shipLifes").unregister();
		}
		
		this.shipLifes = this.bukkitSB.registerNewObjective("shipLifes", "dummy");
		this.shipLifes.setDisplayName("§cSchiffsstärke");
		this.shipLifes.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		this.scoreShipBlue = this.shipLifes.getScore("§bBlau");
		this.scoreShipRed = this.shipLifes.getScore("§cRot");
		
		this.scoreShipBlue.setScore(300);
		this.scoreShipRed.setScore(300);
		
		this.gameConfiguration = new GameConfiguration();
		this.gameConfiguration.loadConfiguration();
		
		WorldGuardPlugin plugin = WorldGuardPlugin.inst();
		RegionManager regionManager = plugin.getRegionManager(this.getGameConfiguration().getWorld());
		
		if(!regionManager.hasRegion(this.getGameConfiguration().getShipRegion1()) ||
				!regionManager.hasRegion(this.getGameConfiguration().getShipRegion2())) {
			throw new RuntimeException("Regions are not configured properly");
		}
		
		this.shipBlue = regionManager.getRegion(this.getGameConfiguration().getShipRegion1());
		this.shipRed = regionManager.getRegion(this.getGameConfiguration().getShipRegion2());
		
		this.cooldowns = new HashMap<Player, CooldownManager>();
		
		this.cannonBall = new ItemStack(Material.FIREBALL);
		
		ItemMeta cannonBallMeta = this.cannonBall.getItemMeta();
		cannonBallMeta.setDisplayName("§4Schwere Kanonenkugel");
		cannonBallMeta.setLore(Lists.newArrayList("Diese Kanonenkugel ist verzaubert",
				"und schießt riesige brennende Kugeln!",
				"§7+§225 Schaden",
				"§7-§c25 Sekunden Abklingzeit"));
		
		this.cannonBall.setItemMeta(cannonBallMeta);
		
		this.bowExplosion = new ItemStack(Material.BOW);
		
		ItemMeta bowExplosionMeta = this.bowExplosion.getItemMeta();
		bowExplosionMeta.setDisplayName("§cExplosionsbogen");
		bowExplosionMeta.setLore(Lists.newArrayList("Die Pfeile dieses Bogens sind", " mit Schwarzpulver bestückt", "§7+§215 Schaden"));
		
		this.bowExplosion.setItemMeta(bowExplosionMeta);
	}

	/**
	 * Registers listeners, commands, schedulers and other things which are critical for the plugin to be functioal.
	 */
	public void prepare() {
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyBlockBreak(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyBlockPlace(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyBlockExplode(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyBlockBurn(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyDropItem(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerJoin(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerQuit(), ShipsPlugin.instance());
		
		Bukkit.getPluginManager().registerEvents(new ListenerBrew(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerCraftItem(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerFoodLevelChange(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerFurnaceBurn(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerAchievement(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerArmorStandManipulate(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerBedEnter(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerFish(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerWeatherChange(), ShipsPlugin.instance());
		
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerInteract(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerProjectileHit(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerEntityShootArrow(), ShipsPlugin.instance());
		
		this.setCurrentState(GameState.LOBBY);
	}
	
	/**
	 * Starts the game. Setting game state to PHASE_1 and teleporting all players to their team spawn
	 */
	public void startGame() {
		
	}
	
	/*                    Getters                         */
	
	public Team getTeamBlue() {
		return this.teamBlue;
	}
	
	public Team getTeamRed() {
		return this.teamRed;
	}
	
	public Score getScoreShipBlue() {
		return this.scoreShipBlue;
	}
	
	public Score getScoreShipRed() {
		return this.scoreShipRed;
	}
	
	public GameConfiguration getGameConfiguration() {
		return this.gameConfiguration;
	}
	
	public GameState getCurrentState() {
		return this.currentState;
	}
	
	public void setCurrentState(GameState currentState) {
		this.currentState = currentState;
	}
	
	public HashMap<Player, CooldownManager> getCooldowns() {
		return this.cooldowns;
	}
	
	public ItemStack getCannonBall() {
		return this.cannonBall;
	}
	
	public ItemStack getBowExplosion() {
		return this.bowExplosion;
	}
	
	/*///////////////////////////////////////////////////*/

}
