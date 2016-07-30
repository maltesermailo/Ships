package de.maltesermailo.ships.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.FurnaceBurnEvent;

public class ListenerFurnaceBurn implements Listener {
	
	@EventHandler
	public void onFurnaceBurn(FurnaceBurnEvent e) {
		e.setCancelled(true);
	}
}
