package me.chickenstyle.luckyblocks.prompts;

import me.chickenstyle.luckyblocks.LuckyCube;
import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.Utils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class TitlePrompt extends StringPrompt{
	
	@Override
	public @NotNull String getPromptText(@NotNull ConversationContext context) {
		return Utils.color("&7Enter a title for the LuckyCube!");
	}
	
	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		Player player = (Player) context.getForWhom();
		LuckyCube pack = Main.creatingLuckyCube.get(player.getUniqueId());
		pack.setTitle(input);
		Main.creatingLuckyCube.put(player.getUniqueId(), pack);
		return new TypePrompt();
	}


}
