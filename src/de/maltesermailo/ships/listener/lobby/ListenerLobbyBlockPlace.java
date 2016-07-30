package de.maltesermailo.ships.listener.lobby;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import de.maltesermailo.ships.GameState;
import de.maltesermailo.ships.ShipsPlugin;

public class ListenerLobbyBlockPlace implements Listener {
	
	@EventHandler
	public void onBlockPlace(BlockPlaceEvent e) {
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.LOBBY) {
			e.setCancelled(true);
		}
	}
}