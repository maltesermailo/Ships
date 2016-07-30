package de.maltesermailo.ships.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerBedEnterEvent;

public class ListenerPlayerBedEnter implements Listener {
	
	@EventHandler
	public void onBedEnter(PlayerBedEnterEvent e) {
		e.setCancelled(true);
	}
}
