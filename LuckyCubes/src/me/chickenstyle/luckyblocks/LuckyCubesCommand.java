package me.chickenstyle.luckyblocks;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.conversations.Conversation;
import org.bukkit.conversations.ConversationFactory;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import me.chickenstyle.luckyblocks.configs.CustomLuckyBlocks;
import me.chickenstyle.luckyblocks.prompts.IdPrompt;
import net.md_5.bungee.api.ChatColor;

public class LuckyCubesCommand implements CommandExecutor {

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if (args.length >= 1) {
			switch (args[0].toLowerCase()) {
			case "addluckycube":
				if (sender instanceof Player) {
					Player player = (Player) sender;
					if (player.hasPermission("FancyBags.Admin") || player.hasPermission("FancyBags." + args[0].toString())) {
						Main.creatingLuckyCube.put(player.getUniqueId(), new LuckyCube(0, "", null, "", new ArrayList<String>(), null, null));
						ConversationFactory factory = new ConversationFactory(Main.getInstance());
						Conversation conversation = factory.withFirstPrompt(new IdPrompt()).withLocalEcho(true).buildConversation(player);
						conversation.begin();
						return true;
					} else {
						player.sendMessage(Message.NO_PERMISSION.getMSG());
					}
				}
			break;
			
			case "reload":
				if (sender.hasPermission("LuckyCubes.Admin") || sender.hasPermission("LuckyCubes." + args[0].toString())) {
					CustomLuckyBlocks.reloadConfig();
					Main.getInstance().saveConfig();
					Main.getInstance().reloadConfig();
					Main.getInstance().loadRecipes();
					sender.sendMessage(ChatColor.GREEN + "Configs and recipes have been reloaded!");
					return true;
				} else {
					sender.sendMessage(Message.NO_PERMISSION.getMSG());
				}
			break;
			
			case "give":
				if (sender.hasPermission("LuckyCubes.Admin") || sender.hasPermission("LuckyCubes." + args[0].toString())) {
					if (args.length == 4) {
						if (Bukkit.getServer().getOnlinePlayers().contains(Bukkit.getPlayer(args[1]))) {
							if (isInt(args[2])) {
								if (CustomLuckyBlocks.hasLuckyCube(Integer.valueOf(args[2]))) {
									if (isInt(args[3])) {
										Player target = Bukkit.getPlayer(args[1]);
										LuckyCube cube = CustomLuckyBlocks.getLuckyCubeByID(Integer.valueOf(args[2]));		
										ItemStack cubes = Utils.createLuckyCube(cube);
										cubes.setAmount(Integer.valueOf(args[3]));
										if (target.getInventory().firstEmpty() != -1) {
											target.getInventory().addItem(cubes);
										} else {
											target.getWorld().dropItemNaturally(target.getLocation(), cubes);
										}
										
										target.sendMessage(Utils.color(Message.GIVE_MESSAGE.getMSG().replace("{player}", sender.getName())
												.replace("{backpack}", cube.getTitle())));
										
										sender.sendMessage(Utils.color("&aYou gave &6" + cube.getTitle() + " &ato &6" + target.getDisplayName()));
										return true;
									} else {
										sender.sendMessage(ChatColor.RED + "Invalid Number!");
									}
								} else {
									sender.sendMessage(ChatColor.RED + "There is no luckycube with this id!");
								}
							} else {
								sender.sendMessage(ChatColor.RED + "Invalid LuckyCube id!");
							}
						} else {
							sender.sendMessage(ChatColor.RED + "This player is offline :(");
						}
					} else {
						sender.sendMessage(ChatColor.RED + "Invalid usage");
						sender.sendMessage(ChatColor.GRAY + "/lc give {player} {LuckyCube ID} {amount}");
					}			
				} else {
					sender.sendMessage(Message.NO_PERMISSION.getMSG());
				}
			break;
			
			case "help":
				if (sender.hasPermission("LuckyCubes.Admin") || sender.hasPermission("LuckyCubes." + args[0].toString())) {
					sender.sendMessage(Utils.color("&f----------[LuckyCubes]----------"));
					sender.sendMessage(ChatColor.WHITE + "/lc give {player} {luckycube_id}");
					sender.sendMessage(ChatColor.WHITE + "");
					sender.sendMessage(ChatColor.WHITE + "/lc addluckycube");
					sender.sendMessage(ChatColor.WHITE + "");
					sender.sendMessage(ChatColor.WHITE + "/lc reload");
					sender.sendMessage(ChatColor.WHITE + "");
					sender.sendMessage(ChatColor.WHITE + "/lc help");
					sender.sendMessage(ChatColor.WHITE + "------------------------------");
					return true;
				} else {
					sender.sendMessage(Message.NO_PERMISSION.getMSG());
				}
			break;
			
			default:
				sender.sendMessage(Utils.color("&7Invalid Usage!"));
				sender.sendMessage(Utils.color("&7use /lc help!"));
			}
		} else {
			sender.sendMessage(Utils.color("&7Invalid Usage!"));
			sender.sendMessage(Utils.color("&7use /lc help!"));
		}
		return false;
	}
	
	private boolean isInt(String str) {
		try {
			Integer.parseInt(str);
			return true;
		} catch (Exception e) {
			return false;
		}
	}

}
