	package me.chickenstyle.luckyblocks.prompts;

import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.NumericPrompt;
import org.bukkit.conversations.Prompt;
import org.bukkit.entity.Player;

import me.chickenstyle.luckyblocks.LuckyCube;
import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.Utils;

public class IdPrompt extends NumericPrompt{

	@Override
	public String getPromptText(ConversationContext context) {
		return Utils.color("&7Lets start creating new LuckyCube! \nEnter a &6unique &7LuckyCube id. (Should be a number!)");
			
	}

	@Override
	protected Prompt acceptValidatedInput(ConversationContext context, Number number) {
		int id = Integer.valueOf(number.toString());
		Player player = (Player) context.getForWhom();
		player.sendMessage(Utils.color("&a" + id));
		LuckyCube pack = Main.creatingLuckyCube.get(player.getUniqueId());
		pack.setId(id);
		Main.creatingLuckyCube.put(player.getUniqueId(), pack);
		return new TitlePrompt();
	}
	
	@Override
	protected String getFailedValidationText(ConversationContext context,Number input) {
		return Utils.color("&4 " + input + " is invalid id, please use a valid number!");
	}

}
