package me.chickenstyle.luckyblocks.versions;

import me.chickenstyle.luckyblocks.NMSHandler;
import net.minecraft.nbt.CompoundTag;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_18_R2.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

public class Handler_1_18_R2 implements NMSHandler {
	
	@Override
	public ItemStack addLuckyBlockID(ItemStack item,int id) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		CompoundTag itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();

		assert itemCompound != null;
		itemCompound.putInt("LuckyID", id);
		nmsItem.setTag(itemCompound);
		return CraftItemStack.asBukkitCopy(nmsItem);
	}

	@Override
	public boolean isLuckyBlock(ItemStack item) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		CompoundTag itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
		assert itemCompound != null;
		return itemCompound.contains("LuckyID");
	}

	@Override
	public int getLuckyBlockID(ItemStack item) {
		net.minecraft.world.item.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		CompoundTag itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new CompoundTag();
		assert itemCompound != null;
		return itemCompound.getInt("LuckyID");
	}
	
	
	@Override
	public void playParticles(World world,Location loc, String particle, int amount) {
		world.spawnParticle(Particle.valueOf(particle.toUpperCase()),loc,amount,0,0,0,0.2);
	}
}
