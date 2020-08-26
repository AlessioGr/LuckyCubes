package me.chickenstyle.luckyblocks.versions;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.craftbukkit.v1_12_R1.inventory.CraftItemStack;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.chickenstyle.luckyblocks.NMSHandler;
import net.minecraft.server.v1_12_R1.EnumParticle;
import net.minecraft.server.v1_12_R1.PacketPlayOutWorldParticles;
import net.minecraft.server.v1_12_R1.NBTTagCompound;


public class Handler_1_12_R1 implements NMSHandler{
	
	@Override
	public ItemStack addLuckyBlockID(ItemStack item,int id) {
		net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();

		itemCompound.setInt("LuckyID", id);
		nmsItem.setTag(itemCompound);
		return CraftItemStack.asBukkitCopy(nmsItem);
	}

	@Override
	public boolean isLuckyBlock(ItemStack item) {
		net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		if (itemCompound.hasKey("LuckyID")) {
			return true;
		}
		return false;
	}

	@Override
	public int getLuckyBlockID(ItemStack item) {
		net.minecraft.server.v1_12_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(item);
		NBTTagCompound itemCompound = (nmsItem.hasTag()) ? nmsItem.getTag() : new NBTTagCompound();
		return itemCompound.getInt("LuckyID");
	}
	
	@Override
	public void playParticles(World world,Location loc, String particle, int amount) {
		PacketPlayOutWorldParticles packet
        = new PacketPlayOutWorldParticles(EnumParticle.valueOf(particle.toString()),
        false,
        (float) loc.getX(),
        (float) loc.getY(),
        (float) loc.getZ(),
        0,
        0,
        0,
        1,
        amount);
		
		for (Player player:world.getPlayers()) {
			((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
		}
		
	}
}
