package de.maltesermailo.ships.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import de.maltesermailo.ships.game.Countdown;

public class StartCmd implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender cs, Command cmd, String label, String[] args) {
		if(cs instanceof Player) {
			Player p = (Player) cs;
			if(p.hasPermission("ships.start")) {
				Countdown.setCountTime(5);
			}
		}
		return false;
	}

}
