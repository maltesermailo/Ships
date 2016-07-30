package de.maltesermailo.ships.listener.game;

import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityShootBowEvent;
import org.bukkit.metadata.FixedMetadataValue;
import org.bukkit.scoreboard.Score;

import de.maltesermailo.ships.ShipsPlugin;

public class ListenerEntityShootArrow implements Listener {
	
	@EventHandler
	public void onShoot(EntityShootBowEvent event) {
		if (event.getEntity() instanceof Player) {
			if (event.getBow().getItemMeta().hasDisplayName() &&
					event.getBow().getItemMeta().getDisplayName().
					equals(ShipsPlugin.instance().getGame().getBowExplosion().getItemMeta().getDisplayName())) { // TODO equalize real item
				event.setCancelled(true);
				Player player = (Player) event.getEntity();
				
				Score score;
				
				if (ShipsPlugin.instance().getGame().getTeamBlue().getEntries().contains(player.getName())) {
					score = ShipsPlugin.instance().getGame().getScoreShipBlue();
				} else {
					score = ShipsPlugin.instance().getGame().getScoreShipRed();
				}
				
				if (score.getScore() > 0) {
					Arrow arrow = player.launchProjectile(Arrow.class);
					arrow.setBounce(false);
					arrow.setMetadata("arrow.explosive", new FixedMetadataValue(ShipsPlugin.instance(), true));
				} else {
					player.sendMessage(ShipsPlugin.instance().getPrefix() + " Dein Schiff ist zu stark beschädigt!");
				}
			} else if (event.getBow().getItemMeta().hasDisplayName() &&
					event.getBow().getItemMeta().getDisplayName().
					equals(ShipsPlugin.instance().getGame().getBowFire().getItemMeta().getDisplayName())) { // TODO equalize real item
				event.setCancelled(true);
				Player player = (Player) event.getEntity();
				
				Arrow arrow = player.launchProjectile(Arrow.class);
				arrow.setBounce(false);
				arrow.setMetadata("arrow.fire", new FixedMetadataValue(ShipsPlugin.instance(), true));
			} else if(!event.getBow().getItemMeta().hasDisplayName()) {
				event.setCancelled(true);
				Player player = (Player) event.getEntity();
				
				Arrow arrow = player.launchProjectile(Arrow.class);
				arrow.setBounce(false);
				arrow.setMetadata("arrow.normal", new FixedMetadataValue(ShipsPlugin.instance(), true));
			}
		}
	}
	
}
