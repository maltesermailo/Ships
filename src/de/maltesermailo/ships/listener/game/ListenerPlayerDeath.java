package de.maltesermailo.ships.listener.game;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.metadata.FixedMetadataValue;

import de.maltesermailo.ships.GameState;
import de.maltesermailo.ships.ShipsPlugin;

public class ListenerPlayerDeath implements Listener {
	
	@EventHandler
	public void onDeath(PlayerDeathEvent event) {
		if (ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1 || ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2) {
			event.setKeepInventory(true);
			
			if (ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2) {
				Player player = event.getEntity();
				
				if (player.getInventory().getHelmet() != null &&
						player.getInventory().getHelmet().getType() == Material.GOLD_BLOCK) {
					ItemStack item = player.getInventory().getHelmet();
					
					if(item.getItemMeta().hasDisplayName()) {
						player.getInventory().setHelmet(new ItemStack(Material.AIR));
						
						if(item.getItemMeta().getDisplayName().equals("§bBlauer Schatz")) {
							ShipsPlugin.instance().getGame().getGameConfiguration().getTeamTreasureBlue().getBlock().setType(Material.GOLD_BLOCK);
							
							ShipsPlugin.instance().getGame().getGameConfiguration().getTeamTreasureBlue().getBlock().removeMetadata("team", ShipsPlugin.instance());
							ShipsPlugin.instance().getGame().getGameConfiguration().getTeamTreasureBlue().getBlock().setMetadata("team", new FixedMetadataValue(ShipsPlugin.instance(), "blue"));
							
							Bukkit.broadcastMessage(ShipsPlugin.instance().getPrefix() + "§7Der Schatz von Team §bBlau §7ist wieder an seinem Platz.");
						} else if(item.getItemMeta().getDisplayName().equals("§cRoter Schatz")) {
							ShipsPlugin.instance().getGame().getGameConfiguration().getTeamTreasureRed().getBlock().setType(Material.GOLD_BLOCK);
							
							ShipsPlugin.instance().getGame().getGameConfiguration().getTeamTreasureBlue().getBlock().removeMetadata("team", ShipsPlugin.instance());
							ShipsPlugin.instance().getGame().getGameConfiguration().getTeamTreasureBlue().getBlock().setMetadata("team", new FixedMetadataValue(ShipsPlugin.instance(), "blue"));
							
							Bukkit.broadcastMessage(ShipsPlugin.instance().getPrefix() + "§7Der Schatz von Team §cRot §7ist wieder an seinem Platz.");
						}
					}
				}
			}
		}
	}
	
}
