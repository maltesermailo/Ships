package de.maltesermailo.ships.listener.game;

import org.bukkit.Location;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;

import de.maltesermailo.ships.GameState;
import de.maltesermailo.ships.ShipsPlugin;

public class ListenerProjectileHit implements Listener {

	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		if (ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1 || ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2) {
			Projectile projectile = event.getEntity();

			if (projectile instanceof Fireball) {
				if (projectile.hasMetadata("canonball")) {
					this.createExplosion(projectile.getLocation(), 10.0F);
					projectile.remove();
				}
			} else if (projectile instanceof Arrow) {
				if (projectile.hasMetadata("arrow.explosive")) {
					this.createExplosion(projectile.getLocation(), 3.5F);
					projectile.remove();
				}
			}
		}
	}
	
	private void createExplosion(Location location, float yield) {
		location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), yield, false, false);
	}
	
}
