package de.maltesermailo.ships.listener.game;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerRespawnEvent;

import de.maltesermailo.ships.ShipsPlugin;

public class ListenerPlayerRespawn implements Listener {

	@EventHandler
	public void onRespawn(PlayerRespawnEvent e) {
		if(ShipsPlugin.instance().getGame().getTeamBlue().hasEntry(e.getPlayer().getName())) {
			e.setRespawnLocation(ShipsPlugin.instance().getGame().getGameConfiguration().getTeamSpawnBlue());
		} else if(ShipsPlugin.instance().getGame().getTeamRed().hasEntry(e.getPlayer().getName())) {
			e.setRespawnLocation(ShipsPlugin.instance().getGame().getGameConfiguration().getTeamSpawnRed());
		}
	}
	
}
