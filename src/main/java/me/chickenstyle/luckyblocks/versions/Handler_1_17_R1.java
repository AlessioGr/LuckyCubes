package me.chickenstyle.luckyblocks.versions;

import me.chickenstyle.luckyblocks.NMSHandler;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_17_R1.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class Handler_1_17_R1 implements NMSHandler {
	
	@Override
	public ItemStack addLuckyBlockID(ItemStack item,int id) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		CompoundTag itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();

		itemCompound.putInt("LuckyID", id);
		nmsItem.setTag(itemCompound);
		return CraftItemStack.asBukkitCopy(nmsItem);
	}

	@Override
	public boolean isLuckyBlock(ItemStack item) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		CompoundTag itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
		if (itemCompound.contains("LuckyID")) {
			return true;
		}
		return false;
	}

	@Override
	public int getLuckyBlockID(ItemStack item) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		CompoundTag itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
		return itemCompound.getInt("LuckyID");
	}
	
	
	@Override
	public void playParticles(World world,Location loc, String particle, int amount) {
		world.spawnParticle(Particle.valueOf(particle.toUpperCase()),loc,amount,0,0,0,0.2);
	}
}
