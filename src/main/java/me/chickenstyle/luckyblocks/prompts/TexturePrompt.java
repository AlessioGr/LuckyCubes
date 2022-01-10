package me.chickenstyle.luckyblocks.prompts;

import me.chickenstyle.luckyblocks.LuckyCube;
import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.Utils;
import org.bukkit.conversations.ConversationContext;
import org.bukkit.conversations.Prompt;
import org.bukkit.conversations.StringPrompt;
import org.bukkit.entity.Player;

public class TexturePrompt extends StringPrompt{
	
	@Override
	public String getPromptText(ConversationContext context) {
		return Utils.color("&7Enter a luckyblock's texture\n (to get the texture go to &6https://minecraft-heads.com/custom-heads &7 \n"
				+ "choose a texture,"
				+ "copy the 'Minecraft-URL' part and paste it here)");
	}
	
	@Override
	public Prompt acceptInput(ConversationContext context, String input) {
		Player player = (Player) context.getForWhom();
		LuckyCube pack = Main.creatingLuckyCube.get(player.getUniqueId());
		pack.setTexture(input);
		Main.creatingLuckyCube.put(player.getUniqueId(), pack);
		return new LorePrompt();
	}


}
