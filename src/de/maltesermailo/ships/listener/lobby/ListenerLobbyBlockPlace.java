package de.maltesermailo.ships.listener.lobby;

import org.bukkit.Material;
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
		} else if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2 ||
				ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1) {
			ShipsPlugin.instance().getGame().getPlayerBlocks().put(e.getBlockPlaced().getLocation(), e.getBlockReplacedState());
		}
		
		if(e.getBlockPlaced().getType().equals(Material.GOLD_BLOCK)) {
			e.setBuild(false);
			e.setCancelled(true);
		}
	}
}
