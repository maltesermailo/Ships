package de.maltesermailo.ships.listener.lobby;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockExplodeEvent;

public class ListenerLobbyBlockExplode implements Listener {
	
	@EventHandler
	public void onBlockExplode(BlockExplodeEvent e) {
		e.blockList().clear();
	}
}
