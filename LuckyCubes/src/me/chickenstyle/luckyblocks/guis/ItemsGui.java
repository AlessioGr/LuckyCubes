package me.chickenstyle.luckyblocks.guis;


import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import me.chickenstyle.luckyblocks.Utils;
import me.chickenstyle.luckyblocks.customholders.ItemsHolder;


public class ItemsGui {
	public ItemsGui(Player player) {
		Inventory gui = Bukkit.createInventory(new ItemsHolder(), 54, Utils.color("&8&lPut the items here!"));
		ItemStack green = Utils.getGreenVersionGlass();
		ItemMeta meta = green.getItemMeta();
		meta.setDisplayName(Utils.color("&aClick here to save the contents!"));
		green.setItemMeta(meta);
		gui.setItem(53, green);
		
		player.openInventory(gui);
	}
}
