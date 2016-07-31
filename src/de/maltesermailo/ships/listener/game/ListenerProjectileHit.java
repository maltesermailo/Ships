package de.maltesermailo.ships.listener.game;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Fireball;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileHitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scoreboard.Score;

import de.maltesermailo.ships.ShipsPlugin;
import de.maltesermailo.ships.game.GameState;

public class ListenerProjectileHit implements Listener {
	
	private static List<BlockFace> faces = Arrays.asList(BlockFace.NORTH, BlockFace.EAST, BlockFace.SOUTH, BlockFace.WEST, BlockFace.UP, BlockFace.DOWN);
	
	@EventHandler
	public void onHit(ProjectileHitEvent event) {
		if (ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1 || ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_2) {
			Projectile projectile = event.getEntity();

			if (projectile instanceof Fireball) {
				if (projectile.hasMetadata("canonball")) {
					this.createExplosion(projectile.getLocation(), 10.0F);
					projectile.remove();
					
					this.damage(projectile, 25);
				}
			} else if (projectile instanceof Arrow) {
				if (projectile.hasMetadata("arrow.explosive")) {
					this.createExplosion(projectile.getLocation(), 3.5F);
					projectile.remove();
					
					this.damage(projectile, 15);
				} else if (projectile.hasMetadata("arrow.fire")) {
					projectile.remove();
					
					if (!this.damage(projectile, 10)) {
						return;
					}
					
					int x = projectile.getLocation().getBlockX() - 2;
					int y = projectile.getLocation().getBlockY() - 2;
					int z = projectile.getLocation().getBlockZ() - 2;
					
					List<Location> firedPlaces = new ArrayList<>();
					
					for (int deltax = 0; deltax < 4; deltax++) {
						for (int deltay = 0; deltay < 4; deltay++) {
							for (int deltaz = 0; deltaz < 4; deltaz++) {
								Block block = new Location(projectile.getWorld(), deltax + x, deltay + y, deltaz + z).getBlock();
								
								if (block.getType().isSolid()) {
									firedPlaces.addAll(this.setOnFire(block));
								}
							}
						}
					}
					
					if (firedPlaces.size() == 0) {
						return;
					}
					
					new BukkitRunnable() {
						
						int ticksLeft = 9;
						
						@Override
						public void run() {
							if (!ListenerProjectileHit.this.damage(projectile, 2) || this.ticksLeft <= 0) {
								this.end();
							}
							
							this.ticksLeft = this.ticksLeft - 1;
						}
						
						private void end() {
							for (Location location : firedPlaces) {
								location.getBlock().setType(Material.AIR);
							}
							
							this.cancel();
						}
						
					}.runTaskTimer(ShipsPlugin.instance(), 20L, 20L);
				} else if(projectile.hasMetadata("arrow.normal")) {
					this.damage(projectile, 2);
				}
			}
		}
	}
	
	private boolean damage(Projectile proj, int damage) {
		if(ShipsPlugin.instance().getGame().getCurrentState() == GameState.PHASE_1) {
			Location loc = proj.getLocation();
			
			if (ShipsPlugin.instance().getGame().getShipBlue().
					contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()) &&
					!ShipsPlugin.instance().getGame().getTeamBlue().hasEntry(((Entity) proj.getShooter()).getName())) {
				Score score = ShipsPlugin.instance().getGame().getScoreShipBlue();
				
				if (score.getScore() == 0) {
					return false;
				}
				
				score.setScore(Math.max(0, score.getScore() - damage));
				
				if(score.getScore() <= 0) {
					ShipsPlugin.instance().getGame().startPhase2();
					
					return false;
				}
				
				return true;
			} else if (ShipsPlugin.instance().getGame().getShipRed().
					contains(loc.getBlockX(), loc.getBlockY(), loc.getBlockZ()) &&
					!ShipsPlugin.instance().getGame().getTeamRed().hasEntry(((Entity) proj.getShooter()).getName())) {
				Score score = ShipsPlugin.instance().getGame().getScoreShipRed();
				
				if (score.getScore() == 0) {
					ShipsPlugin.instance().getGame().startPhase2();
					
					return false;
				}
				
				score.setScore(Math.max(0, score.getScore() - damage));
				
				if(score.getScore() <= 0) {
					ShipsPlugin.instance().getGame().startPhase2();
					
					return false;
				}
				
				return true;
			} else {
				return false;
			}
		}
		return false;
	}
	
	private void createExplosion(Location location, float yield) {
		location.getWorld().createExplosion(location.getX(), location.getY(), location.getZ(), yield, false, false);
	}
	
	private List<Location> setOnFire(Block block) {
		List<Location> result = new ArrayList<>();
		
		for (BlockFace face : ListenerProjectileHit.faces) {
			if (block.getRelative(face).getType() == Material.AIR) {
				block.getRelative(face).setType(Material.FIRE);
				result.add(block.getRelative(face).getLocation());
			}
		}
		
		return result;
	}
	
}
