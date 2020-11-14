package me.chickenstyle.luckyblocks.events;

import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

import me.chickenstyle.luckyblocks.Main;

public class PlayerStandManipulateEvent implements Listener{
	@EventHandler
	public void onPlayerManipulate(PlayerArmorStandManipulateEvent e) {
		if (Main.opening.contains(e.getPlayer().getUniqueId())) {
			e.setCancelled(true);
		}
	}
	
	@EventHandler
	public void onPlayerBreak(EntityDamageEvent e) {
		if (e.getEntity() instanceof ArmorStand) {
			if (Main.stands.contains((ArmorStand) e.getEntity())) {
				e.setCancelled(true);
			}
		}
	}
	
}
