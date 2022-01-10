package me.chickenstyle.luckyblocks.events;

import me.chickenstyle.luckyblocks.LuckyCube;
import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.Message;
import me.chickenstyle.luckyblocks.Utils;
import me.chickenstyle.luckyblocks.animations.CrateAnimation;
import me.chickenstyle.luckyblocks.configs.CustomLuckyBlocks;
import me.chickenstyle.luckyblocks.guis.SpinningGui;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.world.ChunkUnloadEvent;
import org.bukkit.inventory.ItemStack;

import java.util.*;

public class PlaceBlockEvent implements Listener{
	
	@EventHandler
	public void onPlayerCommand(PlayerCommandPreprocessEvent e) {
		if (Main.getInstance().getConfig().getBoolean("disableCommandsOnOpen")) {
			if (Main.opening.contains(e.getPlayer().getUniqueId())) {
				e.setCancelled(true);
				e.getPlayer().sendMessage(Message.DISABLE_COMMAND.getMSG());
			}
		}
	}
	
	@EventHandler
	public void onChunkUnload(ChunkUnloadEvent e) {
		List<Entity> ents = new ArrayList<>();
		for (Entity ent:e.getChunk().getEntities()) {
			if (Main.stands.contains(ent)) {
				ent.remove();
				ents.add(ent);
			}
		}
		
		ents.forEach(ent -> {
			Main.stands.remove(ent);
		});
		
	}
	
	
	HashMap<UUID,Long> cooldown = new HashMap<>();
	
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onBlockPlace(PlayerInteractEvent e) {
		Player player = e.getPlayer();
		
		if (e.getAction() != Action.RIGHT_CLICK_AIR && e.getAction() != Action.RIGHT_CLICK_BLOCK) return;
		if (player.getItemInHand() != null && !player.getItemInHand().getType().equals(Material.AIR)) {
			if (Main.getVersionHandler().isLuckyBlock(player.getItemInHand())) {
				e.setCancelled(true);
				int id = Main.getVersionHandler().getLuckyBlockID(player.getItemInHand());
				if (CustomLuckyBlocks.hasLuckyCube(id)) {
					LuckyCube cube = CustomLuckyBlocks.getLuckyCubeByID(id);
					ArrayList<ItemStack> items = cube.getItems();
					ItemStack prize = items.get(new Random().nextInt(items.size()));
					
					if (cooldown.containsKey(player.getUniqueId())) {
						if (System.currentTimeMillis() < cooldown.get(player.getUniqueId())) {
							long whenItEnds = cooldown.get(player.getUniqueId());
							long now2 = System.currentTimeMillis();
							long difference = whenItEnds - now2;
							
							int differenceInSeconds = (int) (difference / 1000);
							player.sendMessage(Message.COOLDOWN.getMSG().replace("{seconds}", differenceInSeconds + ""));
							return;
						}
					}
					long now = System.currentTimeMillis();
					long tenMillis = Main.getInstance().getConfig().getInt("cooldownTime") * 1000;
					long nowPlusTen = now + tenMillis;
					
					cooldown.put(player.getUniqueId(), nowPlusTen);
					
					switch (cube.getAnimationType()) {
					case GUI:
						new SpinningGui(player, cube);
					break;

					case NONE:
						Utils.givePrize(player, prize);
					break;
					
					case SPINNING:
						if (e.getClickedBlock() != null) {
				              if (e.getClickedBlock().getLocation().clone().add(0.0D, 1.0D, 0.0D).getBlock().getType() == Material.AIR && !e.getAction().equals(Action.RIGHT_CLICK_AIR)) {
				                new CrateAnimation(e.getClickedBlock().getLocation().clone().add(0.0D, 0.3D, 0.0D), player, cube, prize).runTaskTimer(Main.getInstance(), 0L, 1L);
				              } else {
				                player.sendMessage(Message.PLACE_ON_GROUND.getMSG());
				                cooldown.put(player.getUniqueId(), System.currentTimeMillis());
				                return;
				              } 
				            } else {
				              player.sendMessage(Message.PLACE_ON_GROUND.getMSG());
				              cooldown.put(player.getUniqueId(), System.currentTimeMillis());
				              return;
				            } 
					break;
					}
					ItemStack item = player.getItemInHand();
					item.setAmount(item.getAmount() - 1);
					player.setItemInHand(item);
				} else {
					player.sendMessage(Message.REMOVED_LUCKYCUBE.getMSG());
				}
			}
		}
	}
}
