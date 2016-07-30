package de.maltesermailo.ships.listener.lobby;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.maltesermailo.ships.CooldownManager;
import de.maltesermailo.ships.GameState;
import de.maltesermailo.ships.ShipsPlugin;

public class ListenerPlayerJoin implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.LOBBY) {
			e.setJoinMessage(ShipsPlugin.instance().getPrefix() + "§7» " + p.getDisplayName() + " §7hat das Spiel betreten.");
			
			ShipsPlugin.instance().getGame().getCooldowns().put(p, new CooldownManager(p));
		}
	}
}
