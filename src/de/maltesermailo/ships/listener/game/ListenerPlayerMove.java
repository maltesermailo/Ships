package de.maltesermailo.ships.listener.game;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import de.maltesermailo.ships.ShipsGame;
import de.maltesermailo.ships.ShipsPlugin;

public class ListenerPlayerMove implements Listener {

	@EventHandler
	public void onMove(PlayerMoveEvent e) {
		ShipsGame game = ShipsPlugin.instance().getGame();
		
		if(game.getTeamBlue().hasEntry(e.getPlayer().getName())) {
			if(e.getPlayer().getInventory().getHelmet() != null && 
					e.getPlayer().getInventory().getHelmet().getType() == Material.GOLD_BLOCK) {
				if(game.getGameConfiguration().getTeamTreasureBlue().distanceSquared(e.getPlayer().getLocation()) <= 25) {
					game.end(game.getTeamBlue());
				}
			}
		} else if(game.getTeamRed().hasEntry(e.getPlayer().getName())) {
			if(e.getPlayer().getInventory().getHelmet() != null && 
					e.getPlayer().getInventory().getHelmet().getType() == Material.GOLD_BLOCK) {
				if(game.getGameConfiguration().getTeamTreasureRed().distanceSquared(e.getPlayer().getLocation()) <= 25) {
					game.end(game.getTeamRed());
				}
			}
		}
	}
	
}
