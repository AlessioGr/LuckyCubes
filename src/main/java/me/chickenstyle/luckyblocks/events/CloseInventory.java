package me.chickenstyle.luckyblocks.events;

import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.customholders.CreateRecipeHolder;
import me.chickenstyle.luckyblocks.customholders.ItemsHolder;
import me.chickenstyle.luckyblocks.customholders.LuckyCubeHolder;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class CloseInventory implements Listener{
	@EventHandler
	public void onCloseInventory(InventoryCloseEvent e) {
		Player player = (Player) e.getPlayer();
		if (e.getInventory().getHolder() instanceof LuckyCubeHolder) {
			if (!Main.getInstance().getConfig().getBoolean("allowExitInGUI")) {
				if (Main.opening.contains(player.getUniqueId())) {
					new BukkitRunnable() {
						
						@Override
						public void run() {
							player.openInventory(e.getInventory());
						}
						
					}.runTaskLater(Main.getInstance(), 1);
				}
			}
		}
		
		if (e.getInventory().getHolder() instanceof CreateRecipeHolder) {
			if (Main.creatingLuckyCube.containsKey(player.getUniqueId())) {
				player.sendMessage(ChatColor.RED + "LuckyCube creation has been disbanded!");
				Main.creatingLuckyCube.remove(player.getUniqueId());	
			}
		}
		
		if (e.getInventory().getHolder() instanceof ItemsHolder) {
			if (Main.creatingLuckyCube.get(player.getUniqueId()).getItems().isEmpty()) {
				player.sendMessage(ChatColor.RED + "LuckyCube creation has been disbanded!");
				Main.creatingLuckyCube.remove(player.getUniqueId());
				new BukkitRunnable() {
					@Override
					public void run() {
						player.closeInventory();
					}
				}.runTaskLater(Main.getInstance(), 2);	
			}
		}
		
	}
}
