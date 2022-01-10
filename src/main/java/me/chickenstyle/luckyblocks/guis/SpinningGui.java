package me.chickenstyle.luckyblocks.guis;

import me.chickenstyle.luckyblocks.LuckyCube;
import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.Utils;
import me.chickenstyle.luckyblocks.customholders.LuckyCubeHolder;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

public class SpinningGui {
	
	private int itemIndex = 0;
	public SpinningGui(Player player,LuckyCube cube) {
		Main.opening.add(player.getUniqueId());
		Inventory inv = Bukkit.createInventory(new LuckyCubeHolder(), 27, Utils.color(cube.getTitle()));
		ArrayList<ItemStack> contents = cube.getItems();
		
		for (int i = 0;i < 9;i++) {
			inv.setItem(i, Utils.getGrayVersionGlass());
		}
		
		for (int i = 18;i < 27;i++) {
			inv.setItem(i, Utils.getGrayVersionGlass());
		}
		
		shuffle(inv, contents);

		player.openInventory(inv);
		
		player.playSound(player.getLocation(), Utils.getChestOpenSound(), 1f, 1f);
		
		Random r = new Random();
		double seconds = 7.0 + (12.0 - 7.0) * r.nextDouble();
		
		
		new BukkitRunnable() {
			double delay = 0;
			int ticks = 0;
			boolean done = false;
			
			@Override
			public void run() {
				if (done == true) 
					return;
				ticks++;
				delay += 1/(20*seconds);
				
				if (ticks > delay * 10) {
					ticks = 0;
					player.playSound(player.getLocation(), Utils.getPlingSound(), 1f, 1f);
					for (int itemstacks = 9;itemstacks < 18;itemstacks++) {
						inv.setItem(itemstacks, contents.get((itemstacks + itemIndex) % contents.size()));
					}
					
					itemIndex++;
					
					if (delay >= .5) {
						done = true;
						new BukkitRunnable() {
							
							@Override
							public void run() {
								Utils.givePrize(player, inv.getItem(13));
	
								Main.opening.remove(player.getUniqueId());
								player.updateInventory();
								player.closeInventory();
								player.playSound(player.getLocation(), Utils.getChestCloseSound(), 1f, 1f);
							}
						}.runTaskLater(Main.getInstance(), 50);
						cancel();
					}
				}
				
				
			}
		}.runTaskTimer(Main.getInstance(), 0, 1);
		
	}
	
	public void shuffle(Inventory inv,ArrayList<ItemStack> contents) {
		
		int startingIndex = ThreadLocalRandom.current().nextInt(contents.size());
		
		for (int index = 0;index < startingIndex;index++) {
			for (int itemstacks = 9;itemstacks < 18;itemstacks++) {
				inv.setItem(itemstacks, contents.get((itemstacks + itemIndex) % contents.size()));
			}
			itemIndex++;
		}
		//Customize
		ItemStack hopper = new ItemStack(Material.HOPPER);
		ItemMeta meta = hopper.getItemMeta();
		meta.setDisplayName(Utils.color(" "));
		hopper.setItemMeta(meta);
		
		inv.setItem(4, hopper);
		
	}
	
}
