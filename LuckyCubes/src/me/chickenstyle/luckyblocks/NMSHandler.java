package me.chickenstyle.luckyblocks;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.inventory.ItemStack;

public interface NMSHandler {
	public ItemStack addLuckyBlockID(ItemStack item,int id);
	public boolean isLuckyBlock(ItemStack item);
	public int getLuckyBlockID(ItemStack item); 
	public void playParticles(World world,Location loc,String particle,int amount);
}
