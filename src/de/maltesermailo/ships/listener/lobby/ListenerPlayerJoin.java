package de.maltesermailo.ships.listener.lobby;

import java.util.concurrent.Callable;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

import de.maltesermailo.ships.CooldownManager;
import de.maltesermailo.ships.Countdown;
import de.maltesermailo.ships.GameState;
import de.maltesermailo.ships.ShipsPlugin;

public class ListenerPlayerJoin implements Listener {
	
	@EventHandler
	public void onJoin(PlayerJoinEvent e) {
		Player p = e.getPlayer();
		
		p.getInventory().clear();
		p.getInventory().setHelmet(null);
		p.getInventory().setChestplate(null);
		p.getInventory().setLeggings(null);
		p.getInventory().setBoots(null);
		
		p.getActivePotionEffects().forEach(ap -> p.removePotionEffect(ap.getType()));
		
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.LOBBY) {
			e.setJoinMessage(ShipsPlugin.instance().getPrefix() + "§7» " + p.getDisplayName() + " §7hat das Spiel betreten.");
			
			p.teleport(ShipsPlugin.instance().getGame().getGameConfiguration().getLobbySpawn());
			
			ShipsPlugin.instance().getGame().getCooldowns().put(p, new CooldownManager(p));
			
			if(Bukkit.getOnlinePlayers().size() >= 2) {
				Countdown.start(new Callable<Void>() {
					
					@Override
					public Void call() throws Exception {
						ShipsPlugin.instance().getGame().startGame();
						
						return null;
					}
				});
			}
		} else if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1 
				|| ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2) {
			p.kickPlayer("&cDas Spiel läuft bereits, du kannst ihm nicht beitreten.");
		} else {
			p.kickPlayer("Das Spiel ist beendet, der Server startet gleich neu.");
		}
	}
}
