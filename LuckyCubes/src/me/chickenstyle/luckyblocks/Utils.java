package me.chickenstyle.luckyblocks;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.AuthorNagException;

import com.mojang.authlib.GameProfile;
import com.mojang.authlib.properties.Property;

public class Utils {
	
	public static String color(String text) {
		if (Bukkit.getVersion().contains("1.16")) {
			Pattern pattern = Pattern.compile("#[a-fA-f0-9]{6}");
			Matcher match = pattern.matcher(text);
			while (match.find()) {
				String color = text.substring(match.start(),match.end());
				text = text.replace(color, net.md_5.bungee.api.ChatColor.of(color) + "");
			}
		}

		return ChatColor.translateAlternateColorCodes('&', text);
	}
	
    @SuppressWarnings("deprecation")
	public static ItemStack getVersionSkull() {
        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") ||
        		Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")) {
            return new ItemStack(Material.valueOf("PLAYER_HEAD"));
        } else {
            return new ItemStack(Material.valueOf("SKULL_ITEM"), 1, (short) 3);
        }
    }
    
    public static Sound getChestOpenSound() {
    	Sound glass = null;
        if (Bukkit.getVersion().contains("1.8")) {
        	glass = Sound.valueOf("CHEST_OPEN");
        } else {
        	glass = Sound.valueOf("BLOCK_CHEST_OPEN");
        }
        return glass;
    }
    
    public static Sound getChestCloseSound() {
    	Sound glass = null;
        if (Bukkit.getVersion().contains("1.8")) {
        	glass = Sound.valueOf("CHEST_CLOSE");
        } else {
        	glass = Sound.valueOf("BLOCK_CHEST_CLOSE");
        }
        return glass;
    }
    
    public static Sound getPlingSound() {
    	Sound sound = null;
        if (Bukkit.getVersion().contains("1.8")) {
        	sound = Sound.valueOf("NOTE_PLING");
        } else if (Bukkit.getVersion().contains("1.9") ||
        		   Bukkit.getVersion().contains("1.10") ||
        		   Bukkit.getVersion().contains("1.11") ||
        		   Bukkit.getVersion().contains("1.12")) {
        	sound = Sound.valueOf("BLOCK_NOTE_PLING");
        } else {
        	sound = Sound.valueOf("BLOCK_NOTE_BLOCK_PLING");
        }

        return sound;
    }
    
    @SuppressWarnings("deprecation")
	public static ItemStack getGrayVersionGlass() {
    	ItemStack glass = null;
        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") ||
        		Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")) {
            glass = new ItemStack(Material.valueOf("GRAY_STAINED_GLASS_PANE"));
        } else {
        	glass = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 7);
        }
        
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(" ");
        glass.setItemMeta(meta);
        return glass;
    }
    
    @SuppressWarnings("deprecation")
	public static ItemStack getGreenVersionGlass() {
    	ItemStack glass = null;
        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") ||
        		Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")) {
            glass = new ItemStack(Material.valueOf("GREEN_STAINED_GLASS_PANE"));
        } else {
        	glass = new ItemStack(Material.valueOf("STAINED_GLASS_PANE"), 1, (short) 13);
        }
        
        ItemMeta meta = glass.getItemMeta();
        meta.setDisplayName(ChatColor.GREEN + "Click here to save the recipe!");
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
    	ArrayList<String> coloredLore = new ArrayList<String>();
    	ItemStack skull = createCustomSkull(displayName, texture);
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
		String str = "";
		for (String chr:arr) {
			str = str + chr;
		}
		return str;
	}
}
