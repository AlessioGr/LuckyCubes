package me.chickenstyle.luckyblocks;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public interface NMSHandler {
	ItemStack addLuckyBlockID(ItemStack item,int id);
	boolean isLuckyBlock(ItemStack item);
	int getLuckyBlockID(ItemStack item);
	void playParticles(World world,Location loc,String particle,int amount);
}
