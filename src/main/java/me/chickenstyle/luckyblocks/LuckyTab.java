package me.chickenstyle.luckyblocks;

import me.chickenstyle.luckyblocks.configs.CustomLuckyBlocks;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class LuckyTab implements TabCompleter{
	
	final List<String> arguments = new ArrayList<>();
	final List<String> result = new ArrayList<>();

	
	@Override
	public List<String> onTabComplete(@NotNull CommandSender sender, @NotNull Command cmd, @NotNull String label, String[] args) {
		arguments.clear();
		result.clear();

		
		if (args.length == 1) {
			result.add("addluckycube");
			result.add("reload");
			result.add("give");
			result.add("help");
		}else if(args.length == 2 && args[0].equalsIgnoreCase("give")) {
			for (Player player:Bukkit.getServer().getOnlinePlayers()) {
				result.add(player.getName());
			}
		}else if(args.length == 3 && args[0].equalsIgnoreCase("give")) {
			for (LuckyCube cube:CustomLuckyBlocks.getLuckyCubes()) {
				result.add(cube.getId() + "");
			}
		}
		
		StringUtil.copyPartialMatches(args[args.length-1], result, arguments);
		return arguments;
		
	}

}
