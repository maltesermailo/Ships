package de.maltesermailo.ships.game;

import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Score;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.scoreboard.Team.Option;
import org.bukkit.scoreboard.Team.OptionStatus;

import com.google.common.collect.Lists;
import com.sk89q.worldguard.bukkit.WorldGuardPlugin;
import com.sk89q.worldguard.protection.managers.RegionManager;
import com.sk89q.worldguard.protection.regions.ProtectedRegion;

import de.maltesermailo.ships.ShipsPlugin;
import de.maltesermailo.ships.commands.MainCmd;
import de.maltesermailo.ships.commands.StartCmd;
import de.maltesermailo.ships.listener.ListenerArmorStandManipulate;
import de.maltesermailo.ships.listener.ListenerBrew;
import de.maltesermailo.ships.listener.ListenerCraftItem;
import de.maltesermailo.ships.listener.ListenerEntityExplode;
import de.maltesermailo.ships.listener.ListenerFoodLevelChange;
import de.maltesermailo.ships.listener.ListenerFurnaceBurn;
import de.maltesermailo.ships.listener.ListenerPlayerAchievement;
import de.maltesermailo.ships.listener.ListenerPlayerBedEnter;
import de.maltesermailo.ships.listener.ListenerPlayerFish;
import de.maltesermailo.ships.listener.ListenerWeatherChange;
import de.maltesermailo.ships.listener.game.ListenerEntityShootArrow;
import de.maltesermailo.ships.listener.game.ListenerInventoryClick;
import de.maltesermailo.ships.listener.game.ListenerPlayerDeath;
import de.maltesermailo.ships.listener.game.ListenerPlayerInteract;
import de.maltesermailo.ships.listener.game.ListenerPlayerMove;
import de.maltesermailo.ships.listener.game.ListenerPlayerRespawn;
import de.maltesermailo.ships.listener.game.ListenerProjectileHit;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyBlockBreak;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyBlockBurn;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyBlockExplode;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyBlockPlace;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyDropItem;
import de.maltesermailo.ships.listener.lobby.ListenerLobbyEntityDamage;
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
	
	private HashMap<Location, BlockState> playerBlocks = new HashMap<>();
	private HashMap<Location, Inventory> chests = new HashMap<>();
	
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
		this.teamBlue.setDisplayName("§bBlau");
		this.teamBlue.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.FOR_OWN_TEAM);
		this.teamBlue.setAllowFriendlyFire(false);
		this.teamBlue.setCanSeeFriendlyInvisibles(false);
		
		this.teamRed = this.bukkitSB.registerNewTeam("red");
		this.teamRed.setPrefix("§cRot | ");
		this.teamRed.setDisplayName("§cRot");
		this.teamRed.setOption(Option.NAME_TAG_VISIBILITY, OptionStatus.FOR_OWN_TEAM);
		this.teamRed.setAllowFriendlyFire(false);
		this.teamRed.setCanSeeFriendlyInvisibles(false);
		
		if(this.bukkitSB.getObjective("shipLifes") != null) {
			this.bukkitSB.getObjective("shipLifes").unregister();
		}
		
		this.shipLifes = this.bukkitSB.registerNewObjective("shipLifes", "dummy");
		this.shipLifes.setDisplayName("§cSchiffsstärke");
		this.shipLifes.setDisplaySlot(DisplaySlot.SIDEBAR);
		
		this.scoreShipBlue = this.shipLifes.getScore("§bBlau");
		this.scoreShipRed = this.shipLifes.getScore("§cRot");
		
		this.scoreShipBlue.setScore(500);
		this.scoreShipRed.setScore(500);
		
		this.gameConfiguration = new GameConfiguration();
		this.gameConfiguration.loadConfiguration();
		
		WorldGuardPlugin plugin = WorldGuardPlugin.inst();
		RegionManager regionManager = plugin.getRegionManager(this.getGameConfiguration().getWorld());
		
		if(!regionManager.hasRegion(this.getGameConfiguration().getShipRegionBlue()) ||
				!regionManager.hasRegion(this.getGameConfiguration().getShipRegionRed())) {
			throw new RuntimeException("Regions are not configured properly");
		}
		
		this.shipBlue = regionManager.getRegion(this.getGameConfiguration().getShipRegionBlue());
		this.shipRed = regionManager.getRegion(this.getGameConfiguration().getShipRegionRed());
		
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
		this.bowExplosion.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		
		this.bowFire = new ItemStack(Material.BOW);
		
		ItemMeta bowFireMeta = this.bowFire.getItemMeta();
		bowFireMeta.setDisplayName("§cFeuerbogen");
		bowFireMeta.setLore(Lists.newArrayList("Dieser Bogen ist heiß", "und seine Pfeile auch!", "§7+§210 Schaden"));
		
		this.bowFire.setItemMeta(bowFireMeta);
		this.bowFire.addEnchantment(Enchantment.ARROW_INFINITE, 1);
		
		Bukkit.getScheduler().runTaskTimer(ShipsPlugin.instance(), () -> {
			for (CooldownManager manager : this.cooldowns.values()) {
				manager.tick();
			}
		}, 0L, 1L);
	}

	/**
	 * Registers listeners, commands, schedulers and other things which are critical for the plugin to be functioal.
	 */
	public void prepare() {
		this.getGameConfiguration().getWorld().setGameRuleValue("doFireTick", "false");
		
		Bukkit.getPluginCommand("ships").setExecutor(new MainCmd());
		Bukkit.getPluginCommand("start").setExecutor(new StartCmd());
		
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyBlockBreak(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyBlockPlace(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyBlockExplode(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyBlockBurn(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyDropItem(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerLobbyEntityDamage(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerJoin(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerQuit(), ShipsPlugin.instance());
		
		Bukkit.getPluginManager().registerEvents(new ListenerArmorStandManipulate(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerBrew(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerCraftItem(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerEntityExplode(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerFoodLevelChange(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerFurnaceBurn(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerAchievement(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerBedEnter(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerFish(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerWeatherChange(), ShipsPlugin.instance());
		
		Bukkit.getPluginManager().registerEvents(new ListenerEntityShootArrow(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerInventoryClick(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerDeath(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerInteract(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerMove(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerPlayerRespawn(), ShipsPlugin.instance());
		Bukkit.getPluginManager().registerEvents(new ListenerProjectileHit(), ShipsPlugin.instance());
		
		this.getGameConfiguration().getTeamTreasureBlue().getBlock().setType(Material.GOLD_BLOCK);
		this.getGameConfiguration().getTeamTreasureBlue().getBlock().removeMetadata("team", ShipsPlugin.instance());
		this.getGameConfiguration().getTeamTreasureBlue().getBlock().setMetadata("team", new FixedMetadataValue(ShipsPlugin.instance(), "blue"));
		
		this.getGameConfiguration().getTeamTreasureRed().getBlock().setType(Material.GOLD_BLOCK);
		this.getGameConfiguration().getTeamTreasureRed().getBlock().removeMetadata("team", ShipsPlugin.instance());
		this.getGameConfiguration().getTeamTreasureRed().getBlock().setMetadata("team", new FixedMetadataValue(ShipsPlugin.instance(), "red"));
		
		this.setCurrentState(GameState.LOBBY);
	}
	
	/**
	 * Starts the game. Setting game state to PHASE_1 and teleporting all players to their team spawn
	 */
	public void startGame() {
		int i = 1;
		
		for(Player p : Bukkit.getOnlinePlayers()) {
			if(i == 1) {
				i++;
				
				p.sendMessage(ShipsPlugin.instance().getPrefix() + "§7Du bist in Team §bBlau");
				
				this.teamBlue.addEntry(p.getName());
			} else if(i == 2) {
				i--;
				
				p.sendMessage(ShipsPlugin.instance().getPrefix() + "§7Du bist in Team §cRot");
				
				this.teamRed.addEntry(p.getName());
			}
		}
		
		Bukkit.getOnlinePlayers().forEach(p -> {
			if(this.teamBlue.hasEntry(p.getName())) {
				p.teleport(this.getGameConfiguration().getTeamSpawnBlue());
			} else if(this.teamRed.hasEntry(p.getName())) {
				p.teleport(this.getGameConfiguration().getTeamSpawnRed());
			}
		});
		
		this.setCurrentState(GameState.PHASE_1);
	}
	
    public void startPhase2() {
    	Bukkit.broadcastMessage(ShipsPlugin.instance().getPrefix() + "§7Ein Schiff wurde zu stark beschädigt. Phase 2 beginnt.");

		this.setCurrentState(GameState.PHASE_2);
	}
    
    public void end(Team team) {
    	Bukkit.broadcastMessage(ShipsPlugin.instance().getPrefix() + "§7Das Team " + team.getDisplayName() + "§7 hat gewonnen.");
    	
    	this.setCurrentState(GameState.END);
    	
    	Bukkit.getOnlinePlayers().forEach(p -> p.getInventory().clear());
    	
    	Bukkit.getScheduler().runTaskLater(ShipsPlugin.instance(), () -> {
    		Bukkit.getOnlinePlayers().forEach(p -> p.kickPlayer("Der Server startet neu!"));
    		
    		Bukkit.shutdown();
    	}, 10 * 20L);
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
	
	public HashMap<Location, BlockState> getPlayerBlocks() {
		return this.playerBlocks;
	}
	
	public HashMap<Location, Inventory> getChests() {
		return this.chests;
	}
	
	public ProtectedRegion getShipBlue() {
		return this.shipBlue;
	}
	
	public ProtectedRegion getShipRed() {
		return this.shipRed;
	}
	
	public ItemStack getCannonBall() {
		return this.cannonBall;
	}
	
	public ItemStack getBowExplosion() {
		return this.bowExplosion;
	}
	
	public ItemStack getBowFire() {
		return bowFire;
	}
	
	/*///////////////////////////////////////////////////*/

}
