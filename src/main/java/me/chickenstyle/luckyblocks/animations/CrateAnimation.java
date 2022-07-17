package me.chickenstyle.luckyblocks.animations;

import me.chickenstyle.luckyblocks.LuckyCube;
import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.Utils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.util.EulerAngle;
import org.bukkit.util.Vector;



public class CrateAnimation extends BukkitRunnable {
	
	ArmorStand stand;
	ArmorStand text;
	Player player;
	Location loc;
	LuckyCube block;
	ItemStack prize;
	String particle = Main.getInstance().getConfig().getString("spinningAnimation.particleType");
	int amount = Main.getInstance().getConfig().getInt("spinningAnimation.particlesAmount");
	int ticks = 0;
	int newTicks = 0;
	boolean runOnce = false;

	
	@SuppressWarnings("deprecation")
	public CrateAnimation(Location loc,Player player,LuckyCube block,ItemStack prize) {
		Main.opening.add(player.getUniqueId());
		loc.add(0.5,0,0.5);
		stand = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
		stand.setArms(true);
		stand.setVisible(false);
		stand.setGravity(false);
		stand.setSmall(true);
		stand.setHelmet(Utils.createLuckyCube(block));
		Main.stands.add(stand);
		this.loc = loc;
		this.player = player;
		this.block = block;
		this.prize = prize;

	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void run() {
		if (ticks <= 120) {
			EulerAngle oldrot = stand.getHeadPose();
			EulerAngle newrot = oldrot.add(0, 0.2f, 0);
			stand.setHeadPose(newrot);
			stand.teleport(stand.getLocation().add(0,0.02,0));
			if (ticks % 6 == 0) {
				player.playSound(player.getLocation(), Utils.getPlingSound(), 1f, 1f);
			}
			
			ticks = ticks + 2;
		} else {
			if (!runOnce) {
				stand.setHelmet(new ItemStack(Material.AIR));
				loc = stand.getLocation().clone().subtract(0,0.6,0);
				stand.setRightArmPose(new EulerAngle(-(Math.PI / 2), -0.69, 0));
				stand.teleport(stand.getLocation().clone().add(0,1,0));
				stand.setSmall(false);
				player.playSound(player.getLocation(), Utils.getChestOpenSound(), 1f, 1f);			
				stand.setItemInHand(prize);
				Main.getVersionHandler().playParticles(loc.getWorld(), stand.getLocation().clone().add(0,0.5,0), particle, amount);
				runOnce = true;
				
				//Text in the up
				text = (ArmorStand) loc.getWorld().spawnEntity(loc, EntityType.ARMOR_STAND);
				Main.stands.add(text);
				
				String title = Main.getInstance().getConfig().getString("spinningAnimation.armortandTitle");
				assert title != null;
				title = title.replace("{player}", player.getName());
				title = title.replace("{amount}", prize.getAmount() + "");
				if (prize.getItemMeta().hasDisplayName()) {
					title = title.replace("{item}", prize.getItemMeta().getDisplayName());
				} else {
					title = title.replace("{item}", Utils.getName(prize.getType()));
				}
				text.setCustomName(Utils.color(title));
				text.setCustomNameVisible(true);
				text.setGravity(false);
				text.setVisible(false);
			}
			
			if (newTicks <= 80) {
				newTicks++;
	            double t = ((double) newTicks%40) * Math.PI / 20;
	            
	            stand.teleport(getLocationAroundCircle(loc, 0.5, t));
	            
			} else {
				Main.stands.remove(stand);
				Main.stands.remove(text);
				stand.remove();
				text.remove();
				Utils.givePrize(player, prize);
				
				Main.getVersionHandler().playParticles(loc.getWorld(), stand.getLocation().clone().add(-0.5,1.8,0), particle, amount);
				
				player.playSound(player.getLocation(), Utils.getChestCloseSound(), 1f, 1f);
				cancel();
				Main.opening.remove(player.getUniqueId());
			}
		}
	}
	
    private Location getLocationAroundCircle(Location center, double radius, double angleInRadian) {
        double x = center.getX() + radius * Math.cos(angleInRadian);
        double z = center.getZ() + radius * Math.sin(angleInRadian);
        double y = center.getY();

        Location loc = new Location(center.getWorld(), x, y, z);
        Vector difference = center.toVector().clone().subtract(loc.toVector());
        loc.setDirection(difference);

        return loc;
    }
	
}
