package de.maltesermailo.ships.listener.lobby;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;

import de.maltesermailo.ships.ShipsPlugin;
import de.maltesermailo.ships.game.GameState;

public class ListenerLobbyEntityDamage implements Listener {
	
	@EventHandler
	public void onDamage(EntityDamageEvent e) {
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.LOBBY ||
				ShipsPlugin.instance().getGame().getCurrentState() == GameState.END) {
			e.setCancelled(true);
		}
	}

}
