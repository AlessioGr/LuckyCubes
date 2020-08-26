package me.chickenstyle.luckyblocks.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

import me.chickenstyle.luckyblocks.AnimationType;
import me.chickenstyle.luckyblocks.LuckyCube;
import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.Utils;

public class TypePrompt extends StringPrompt{
	
	@Override
	public String getPromptText(ConversationContext context) {
		return Utils.color("&7Enter an animation type for the luckycube! (GUI/SPINNING/NONE)");
	}
	
	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		Player player = (Player) context.getForWhom();
		LuckyCube pack = Main.creatingLuckyCube.get(player.getUniqueId());		
		try {
			AnimationType type = AnimationType.valueOf(input.toUpperCase());
			pack.setAnimationType(type);
			Main.creatingLuckyCube.put(player.getUniqueId(), pack);
			return new TexturePrompt();
		} catch(Exception ex) {
			player.sendRawMessage(Utils.color("&cInvalid animation type!"));
			return new TypePrompt();
		}
		
	}


}
