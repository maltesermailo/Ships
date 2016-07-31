package de.maltesermailo.ships.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.maltesermailo.ships.ShipsPlugin;
import de.maltesermailo.ships.game.ShipsGame;

public class MainCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cs instanceof Player) {
			Player p = (Player)cs;
			
			if(args.length > 0) {
				if(args.length == 1) {
					if(args[0].equalsIgnoreCase("setLobby")) {
						ShipsPlugin.instance().getGame().getGameConfiguration().setLobbySpawn(p.getLocation());
						
						p.sendMessage("Lobby set!");
					} else if(args[0].equalsIgnoreCase("setTreasureBlue")) {
						ShipsPlugin.instance().getGame().getGameConfiguration().setTeamTreasureBlue(p.getLocation());
						
						p.sendMessage("Treasure blue set!");
					} else if(args[0].equalsIgnoreCase("setTreasureRed")) {
						ShipsPlugin.instance().getGame().getGameConfiguration().setTeamTreasureRed(p.getLocation());
						
						p.sendMessage("Treasure red set!");
					} else if(args[0].equalsIgnoreCase("setSpawnBlue")) {
						ShipsPlugin.instance().getGame().getGameConfiguration().setTeamSpawnBlue(p.getLocation());
						
						p.sendMessage("Spawn blue set!");
					} else if(args[0].equalsIgnoreCase("setSpawnRed")) {
						ShipsPlugin.instance().getGame().getGameConfiguration().setTeamSpawnRed(p.getLocation());
						
						p.sendMessage("Spawn red set!");
					} else if(args[0].equalsIgnoreCase("giveitems")) {
						ShipsGame game = ShipsPlugin.instance().getGame();
						
						p.getInventory().addItem(game.getBowExplosion(), game.getBowFire(), game.getCannonBall());
					} else if(args[0].equalsIgnoreCase("save")) {
						ShipsPlugin.instance().getGame().getGameConfiguration().saveConfiguration();
						
						p.sendMessage("saved.");
					}
				}
			} else {
				sendHelp(cs);
			}
		}
		
		return false;
	}

	private void sendHelp(CommandSender cs) {
		
	}
	
}
