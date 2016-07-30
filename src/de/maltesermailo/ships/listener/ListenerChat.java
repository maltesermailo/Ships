package de.maltesermailo.ships.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

import de.maltesermailo.ships.GameState;
import de.maltesermailo.ships.ShipsPlugin;

public class ListenerChat implements Listener {
	
	@EventHandler
	public void onChat(AsyncPlayerChatEvent e) {
		String message = e.getMessage();
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.LOBBY) {
			e.setFormat("§6" + e.getPlayer().getDisplayName() + "§8: §r" + message);
		} else if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1 || ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2) {
			e.setFormat("Jannik ist schwul.");
		} else {
			e.setFormat("§6" + e.getPlayer().getDisplayName() + "§8: §r" + message);
		}
	}
}
