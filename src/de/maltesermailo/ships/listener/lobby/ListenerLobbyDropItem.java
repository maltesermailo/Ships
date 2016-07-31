package de.maltesermailo.ships.listener.lobby;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerDropItemEvent;

import de.maltesermailo.ships.ShipsPlugin;
import de.maltesermailo.ships.game.GameState;

public class ListenerLobbyDropItem implements Listener {
	
	@EventHandler
	public void onDrop(PlayerDropItemEvent e) {
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.LOBBY) {
			e.setCancelled(true);
		}
		
		if(e.getItemDrop().getItemStack().getType() == Material.GOLD_BLOCK) {
			e.setCancelled(true);
		}
	}
}
