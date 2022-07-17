package me.chickenstyle.luckyblocks.prompts;

import me.chickenstyle.luckyblocks.LuckyCube;
import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.Utils;
import me.chickenstyle.luckyblocks.guis.ItemsGui;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class LorePrompt extends StringPrompt{
	
	@Override
	public @NotNull String getPromptText(@NotNull ConversationContext context) {
		return Utils.color("&7Now you can create a lore, everytime you send a message it will be a new line in the lore"
				+ "so if you want to stop type '!stop'");
	}
	
	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		Player player = (Player) context.getForWhom();
		LuckyCube pack = Main.creatingLuckyCube.get(player.getUniqueId());
		ArrayList<String> lore = pack.getLore();

		assert input != null;
		if (!input.equalsIgnoreCase("!stop")) {
			lore.add(input);
			pack.setLore(lore);
			Main.creatingLuckyCube.put(player.getUniqueId(), pack);
			return new LorePrompt();
		} else {
			new ItemsGui(player);
			return Prompt.END_OF_CONVERSATION;
		}
		
	}


}
