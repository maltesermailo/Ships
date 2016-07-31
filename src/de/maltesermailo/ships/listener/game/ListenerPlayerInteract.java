package de.maltesermailo.ships.listener.game;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Chest;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Score;

import de.maltesermailo.ships.ShipsPlugin;
import de.maltesermailo.ships.game.GameState;

public class ListenerPlayerInteract implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1 || ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2) {
			if (event.getItem() != null && event.getItem().getType() == Material.FIREBALL) { // TODO equalize real item
				Player player = event.getPlayer();
				
				event.setCancelled(true);
				
				if (ShipsPlugin.instance().getGame().getCooldowns().get(player).contains("FIRE_CHARGE")) {
					return;
				}
				
				Score score;
				
				if (ShipsPlugin.instance().getGame().getTeamBlue().getEntries().contains(player.getName())) {
					score = ShipsPlugin.instance().getGame().getScoreShipBlue();
				} else {
					score = ShipsPlugin.instance().getGame().getScoreShipRed();
				}
				
				if (score.getScore() > 0) {
					Fireball ball = player.launchProjectile(Fireball.class);
					ball.setBounce(false);
					ball.setYield(0.0F);
					ball.setMetadata("canonball", new FixedMetadataValue(ShipsPlugin.instance(), true));
					
					ShipsPlugin.instance().getGame().getCooldowns().get(player).add("FIRE_CHARGE", 20 * 25);
				} else {
					player.sendMessage(ShipsPlugin.instance().getPrefix() + " Dein Schiff ist zu stark beschädigt!");
				}
			}
			
			if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.GOLD_BLOCK) {
				if(event.getClickedBlock().hasMetadata("team")) {
					if(event.getClickedBlock().getMetadata("team").get(0).asString().equalsIgnoreCase("blue")) {
						if(ShipsPlugin.instance().getGame().getTeamBlue().hasEntry(event.getPlayer().getName())) {
							return;
						}
						
						event.getClickedBlock().setType(Material.AIR);
						
						ItemStack item = new ItemStack(Material.GOLD_BLOCK);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName("§bBlauer Schatz");
						item.setItemMeta(meta);
						
						event.getPlayer().getInventory().setHelmet(item);
					} else if(event.getClickedBlock().getMetadata("team").get(0).asString().equalsIgnoreCase("red")) {
						if(ShipsPlugin.instance().getGame().getTeamRed().hasEntry(event.getPlayer().getName())) {
							return;
						}
						
						event.getClickedBlock().setType(Material.AIR);
						
						ItemStack item = new ItemStack(Material.GOLD_BLOCK);
						ItemMeta meta = item.getItemMeta();
						meta.setDisplayName("§cRoter Schatz");
						item.setItemMeta(meta);
						
						event.getPlayer().getInventory().setHelmet(item);
					}
				}
			} else if(event.getClickedBlock() != null && event.getClickedBlock().getType() == Material.CHEST) {
				event.setCancelled(true);
				
				if(!ShipsPlugin.instance().getGame().getChests().containsKey(event.getClickedBlock())) {
					Chest c = (Chest) event.getClickedBlock().getState();
					
					Inventory newInv = Bukkit.createInventory(null, c.getInventory().getSize());
					
					newInv.setContents(c.getInventory().getContents().clone());
					
					ShipsPlugin.instance().getGame().getChests().put(event.getClickedBlock().getLocation(), newInv);
				}
				
				event.getPlayer().openInventory(ShipsPlugin.instance().getGame().getChests().get(event.getClickedBlock().getLocation()));
			}
		} 
	}
	
}
