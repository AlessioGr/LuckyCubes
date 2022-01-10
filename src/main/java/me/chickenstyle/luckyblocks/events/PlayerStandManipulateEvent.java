package me.chickenstyle.luckyblocks.events;

import me.chickenstyle.luckyblocks.Main;
import org.bukkit.entity.ArmorStand;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.player.PlayerArmorStandManipulateEvent;

public class PlayerStandManipulateEvent implements Listener{
	@EventHandler
	public void onPlayerManipulate(PlayerArmorStandManipulateEvent e) {
		if (Main.stands.contains(e.getRightClicked())) {
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
