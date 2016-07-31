package de.maltesermailo.ships.listener.lobby;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBurnEvent;

import de.maltesermailo.ships.ShipsPlugin;
import de.maltesermailo.ships.game.GameState;

public class ListenerLobbyBlockBurn implements Listener {
	
	@EventHandler
	public void onBlockBurn(BlockBurnEvent e) {
		e.setCancelled(true);
	}
}
