package me.chickenstyle.luckyblocks.versions;

import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_16_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import me.chickenstyle.luckyblocks.NMSHandler;
import net.minecraft.server.v1_16_R1.NBTTagCompound;

public class Handler_1_16_R1 implements NMSHandler {
	
	@Override
	public ItemStack addLuckyBlockID(ItemStack item,int id) {
		net.minecraft.server.v1_16_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();

		itemCompound.setInt("LuckyID", id);
		nmsItem.setTag(itemCompound);
		return CraftItemStack.asBukkitCopy(nmsItem);
	}

	@Override
	public boolean isLuckyBlock(ItemStack item) {
		net.minecraft.server.v1_16_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		if (itemCompound.hasKey("LuckyID")) {
			return true;
		}
		return false;
	}

	@Override
	public int getLuckyBlockID(ItemStack item) {
		net.minecraft.server.v1_16_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		return itemCompound.getInt("LuckyID");
	}
	
	@Override
	public void playParticles(World world,Location loc, String particle, int amount) {
		world.spawnParticle(Particle.valueOf(particle.toUpperCase()),loc,amount,0,0,0,0.2);
	}
}
