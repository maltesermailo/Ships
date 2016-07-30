package de.maltesermailo.ships.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityExplodeEvent;

public class ListenerEntityExplode implements Listener {

	@EventHandler
	public void onExplode(EntityExplodeEvent e) {
		e.blockList().clear();
	}
	
}
