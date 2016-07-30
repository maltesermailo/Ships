package de.maltesermailo.ships.listener.lobby;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;

import de.maltesermailo.ships.GameState;
import de.maltesermailo.ships.ShipsPlugin;

public class ListenerLobbyBlockBreak implements Listener {
	
	@EventHandler
	public void onBlockBreak(BlockBreakEvent e) {
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.LOBBY) {
			e.setCancelled(true);
		}
		
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2  ||
				ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1) {
			if(!ShipsPlugin.instance().getGame().getPlayerBlocks().containsKey(e.getBlock().getLocation())) {
				e.setCancelled(true);
			}
		}
	}
}
