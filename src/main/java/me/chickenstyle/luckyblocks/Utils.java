package me.chickenstyle.luckyblocks;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.AuthorNagException;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
	
	public static String color(String text) {
		Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");
		Matcher match = pattern.matcher(text);
		while (match.find()) {
			String color = text.substring(match.start(),match.end());
			text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
		}

		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
	public static ItemStack getVersionSkull() {
		return new ItemStack(Material.PLAYER_HEAD);
    }
    
    public static Sound getChestOpenSound() {
        return Sound.BLOCK_CHEST_OPEN;
    }
    
    public static Sound getChestCloseSound() {
		return Sound.BLOCK_CHEST_CLOSE;
    }
    
    public static Sound getPlingSound() {
        return Sound.BLOCK_NOTE_BLOCK_PLING;
    }
    
	public static ItemStack getGrayVersionGlass() {
    	ItemStack glass = new ItemStack(Material.GRAY_STAINED_GLASS_PANE);
        
        ItemMeta meta = glass.getItemMeta();
        meta.displayName(Component.text(" "));
        glass.setItemMeta(meta);
        return glass;
    }
    
	public static ItemStack getGreenVersionGlass() {
    	ItemStack glass = new ItemStack(Material.GREEN_STAINED_GLASS_PANE);

        
        ItemMeta meta = glass.getItemMeta();
		meta.displayName(Component.text("Click here to save the recipe!", NamedTextColor.GREEN));
        glass.setItemMeta(meta);
        return glass;
    }
    
    public static ItemStack createCustomSkull(String displayName, String texture) {
    	try {
            ItemStack skull = getVersionSkull();
            if (texture.isEmpty()) {
                return skull;
            }
            texture = "http://textures.minecraft.net/texture/" + texture;
            SkullMeta skullMeta = (SkullMeta)skull.getItemMeta();
            skullMeta.setDisplayName(Utils.color(displayName));
            GameProfile profile = new GameProfile(UUID.randomUUID(), null);
            byte[] encodedData = java.util.Base64.getEncoder().encode(String.format("{textures:{SKIN:{url:\"%s\"}}}", texture).getBytes());
            profile.getProperties().put("textures", new Property("textures", new String(encodedData)));
            Field profileField = null;
            try {
                profileField = skullMeta.getClass().getDeclaredField("profile");
            }
            catch (NoSuchFieldException | SecurityException e) {
                e.printStackTrace();
            }
            assert profileField != null;
            profileField.setAccessible(true);
            try {
                profileField.set(skullMeta, profile);
            }
            catch (IllegalArgumentException | IllegalAccessException e) {
                e.printStackTrace();
            }
            skull.setItemMeta(skullMeta);
            return skull;
    	} catch (AuthorNagException e) {
    		e.printStackTrace();
    	}
    	return null;
    }
    
    public static ItemStack createCustomSkull(String displayName, String texture,ArrayList<String> lore) {
    	ArrayList<String> coloredLore = new ArrayList<>();
    	ItemStack skull = createCustomSkull(displayName, texture);
		assert skull != null;
		ItemMeta meta = skull.getItemMeta();
    	for (String str:lore) {
    		coloredLore.add(color(str));
    	}

    	meta.setLore(coloredLore);
    	skull.setItemMeta(meta);
    	return skull;
    }
    
    public static ItemStack createLuckyCube(LuckyCube block) {
    	return Main.getVersionHandler().addLuckyBlockID(createCustomSkull(block.getTitle(), block.getTexture(),block.getLore()), block.getId());
    }
    
    public static ItemStack createLuckyCube(int id,String title, String texture,ArrayList<String> lore) {
    	return Main.getVersionHandler().addLuckyBlockID(createCustomSkull(title, texture,lore), id);
    }
    
    public static String getName(Material mat) {
		String name = mat.name().replace('_',' ').toLowerCase();
		String[] data = name.split("");
		
		for (int i = 0;i < data.length;i++) {
			if (i != 0) {
				if (data[i - 1].equals(" ")) {
					data[i] = data[i].toUpperCase();
				}
			} else {
				data[i] = data[i].toUpperCase();
			}
		}
		
		name = arrayToString(data);
		return name;
	}
    
    public static void givePrize(Player player,ItemStack prize) {
    	String message = Message.GET_REWARD.getMSG();
    	
		ItemMeta meta = prize.getItemMeta();
		
		message = message.replace("{amount}", prize.getAmount() + "");
		if (meta.hasDisplayName()) {
			message = message.replace("{item}", meta.getDisplayName());
		} else {
			message = message.replace("{item}", getName(prize.getType()));
		}
		
		if (Main.getInstance().getConfig().getBoolean("sendMessageOnReward")) {
			player.sendMessage(Utils.color(message));
		}
		
		if (player.getInventory().firstEmpty() != -1) {
			player.getInventory().addItem(prize);
		} else {
			player.getWorld().dropItem(player.getLocation(), prize);
		}
    }
    
	public static String arrayToString(String[] arr) {
		StringBuilder str = new StringBuilder();
		for (String chr:arr) {
			str.append(chr);
		}
		return str.toString();
	}
}
