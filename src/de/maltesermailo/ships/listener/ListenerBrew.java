package de.maltesermailo.ships.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.BrewEvent;

public class ListenerBrew implements Listener {
	
	@EventHandler
	public void onBrew(BrewEvent e) {
		e.setCancelled(true);
	}
}
