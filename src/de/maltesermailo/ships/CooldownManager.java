package de.maltesermailo.ships;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.bukkit.entity.Player;

public class CooldownManager {
	
	private Player owner;
	
	private List<Cooldown> toStart;
	private List<Cooldown> toRemove;
	private List<Cooldown> reference;
	private List<Cooldown> cooldowns;
	
	public CooldownManager(Player owner) {
		this.owner = owner;
		
		this.toStart = new ArrayList<>();
		this.toRemove = new ArrayList<>();
		this.reference = new ArrayList<>();
		this.cooldowns = new ArrayList<>();
	}
	
	public void tick() {
		while (!this.toStart.isEmpty()) {
			this.cooldowns.add(this.toStart.get(0));
			
			for (Field field : ReflectionUtil.getNMSClass("Items").getFields()) {
				if (field.getName().equals(this.toStart.get(0).getKey())) {
					CooldownManager.sendCooldownPacket(ReflectionUtil.createStaticObject(field), this.toStart.get(0).getLength(), this.owner);
					break;
				}
			}
			
			this.toStart.remove((int) 0);
		}
		
		while (!this.toRemove.isEmpty()) {
			this.cooldowns.remove(this.toRemove.get(0));
			this.reference.remove(this.toRemove.get(0));
			
			for (Field field : ReflectionUtil.getNMSClass("Items").getFields()) {
				if (field.getName().equals(this.toRemove.get(0).getKey())) {
					CooldownManager.sendCooldownPacket(ReflectionUtil.createStaticObject(field), 0, this.owner);
					break;
				}
			}
			
			this.toRemove.remove(0);
		}
		
		Iterator<Cooldown> iterator = this.cooldowns.iterator();
		
		while (iterator.hasNext()) {
			Cooldown cooldown = iterator.next();
			cooldown.countDown();
			
			if (cooldown.getLength() <= 0) {
				iterator.remove();
				this.reference.remove(cooldown);
			}
		}
	}
	
	public void add(String key, int length) {
		if (this.contains(key)) {
			return;
		}
		
		Cooldown cooldown = new Cooldown(key, length);
		
		this.toStart.add(cooldown);
		this.reference.add(cooldown);
	}
	
	public void reset(String key) {
		Cooldown cooldown = this.fromReference(key);
		
		if (cooldown != null) {
			this.toRemove.add(cooldown);
		}
	}
	
	public boolean contains(String key) {
		return this.fromReference(key) != null;
	}
	
	public int get(String key) {
		Cooldown cooldown = this.fromReference(key);
		
		if (cooldown == null) {
			return 0;
		} else {
			return cooldown.getLength();
		}
	}
	
	public Player getOwner() {
		return this.owner;
	}
	
	private Cooldown fromReference(String key) {
		for (Cooldown cooldown : this.reference) {
			if (cooldown.getKey().equals(key)) {
				return cooldown;
			}
		}
		
		return null;
	}
	
	
	private static class Cooldown {
		
		private int length;
		private String key;
		
		private Cooldown(String key, int length) {
			this.length = length;
			this.key = key;
		}
		
		private void countDown() {
			this.length = this.length - 1;
		}
		
		private int getLength() {
			return this.length;
		}
		
		private String getKey() {
			return this.key;
		}
		
	}
	
	public static void sendCooldownPacket(Object item, int length, Player receiver) {
		Class<?> packetClass = ReflectionUtil.getNMSClass("PacketPlayOutSetCooldown");
		Class<?> craftPlayerClass = ReflectionUtil.getCraftBukkitClass("entity.CraftPlayer");
		Class<?> nmsPlayerClass = ReflectionUtil.getNMSClass("EntityPlayer");
		Class<?> playerConnectionClass = ReflectionUtil.getNMSClass("PlayerConnection");
		
		Object packet = ReflectionUtil.createObject(ReflectionUtil.getConstructor(packetClass,
				new Class<?>[] { ReflectionUtil.getNMSClass("Item"), int.class }), item, length);
		Object nmsPlayer = ReflectionUtil.createObject(ReflectionUtil.getMethod(craftPlayerClass, "getHandle", ReflectionUtil.emtyClassArray()), receiver);
		Object connection = ReflectionUtil.createObject(ReflectionUtil.getField(nmsPlayerClass, "playerConnection"), nmsPlayer);
		
		ReflectionUtil.invokeMethod(ReflectionUtil.getMethod(playerConnectionClass, "sendPacket",
				new Class<?>[] { ReflectionUtil.getNMSClass("Packet") }), connection, packet);
	}
	
}