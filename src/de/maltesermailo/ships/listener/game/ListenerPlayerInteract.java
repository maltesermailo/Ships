package de.maltesermailo.ships.listener.game;

import org.bukkit.Material;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Score;

import de.maltesermailo.ships.GameState;
import de.maltesermailo.ships.ShipsPlugin;

public class ListenerPlayerInteract implements Listener {
	
	@EventHandler
	public void onInteract(PlayerInteractEvent event) {
		if (ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1 || ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2) {
			if (event.getItem() != null && event.getItem().getType() == Material.FIREBALL) { // TODO equalize real item
				Player player = event.getPlayer();
				
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
					player.sendMessage(ShipsPlugin.instance().getPrefix() + " Das Schiff wurde an dieser Stelle bereits zerstört!");
				}
			}
		}
	}
	
}
