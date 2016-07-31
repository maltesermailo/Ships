package de.maltesermailo.ships.listener.game;

import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.Event.Result;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.inventory.InventoryMoveItemEvent;

public class ListenerInventoryClick implements Listener {

	@EventHandler
	public void onClick(InventoryClickEvent e) {
		if(e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.GOLD_BLOCK) {
			e.setResult(Result.DENY);
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(InventoryMoveItemEvent e) {
		if(e.getItem() != null && e.getItem().getType() == Material.GOLD_BLOCK) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void on(InventoryCreativeEvent e) {
		if(e.getCurrentItem() != null && e.getCurrentItem().getType() == Material.GOLD_BLOCK) {
			e.setResult(Result.DENY);
			e.setCancelled(true);
		}
	}
	
}
