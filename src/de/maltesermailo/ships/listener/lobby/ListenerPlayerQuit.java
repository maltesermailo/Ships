package de.maltesermailo.ships.listener.lobby;

import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

import de.maltesermailo.ships.Countdown;
import de.maltesermailo.ships.GameState;
import de.maltesermailo.ships.ShipsPlugin;

public class ListenerPlayerQuit implements Listener {
	
	@EventHandler
	public void onQuit(PlayerQuitEvent e) {
		Player p = e.getPlayer();
		
		e.setQuitMessage(ShipsPlugin.instance().getPrefix() + "§7« " + p.getDisplayName() + " §7hat das Spiel verlassen.");
		
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.LOBBY) {
			if(Bukkit.getOnlinePlayers().size() < 2) {
				Countdown.stop();
			}
		}
		
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1 ||
				ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2) {
			if(ShipsPlugin.instance().getGame().getTeamBlue().hasEntry(p.getName())) {
				ShipsPlugin.instance().getGame().getTeamBlue().removeEntry(p.getName());
				
                if(ShipsPlugin.instance().getGame().getTeamBlue().getEntries().size() == 0) {
					ShipsPlugin.instance().getGame().end(ShipsPlugin.instance().getGame().getTeamRed());
				}
			} else if(ShipsPlugin.instance().getGame().getTeamRed().hasEntry(p.getName())) {
                ShipsPlugin.instance().getGame().getTeamRed().removeEntry(p.getName());
				
				if(ShipsPlugin.instance().getGame().getTeamRed().getEntries().size() == 0) {
					ShipsPlugin.instance().getGame().end(ShipsPlugin.instance().getGame().getTeamBlue());
				}
			}
		}
	}
}
