package de.maltesermailo.ships.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerFishEvent;

public class ListenerPlayerFish implements Listener {
	
	@EventHandler
	public void onFish(PlayerFishEvent e) {
		e.setCancelled(true);
	}
}
