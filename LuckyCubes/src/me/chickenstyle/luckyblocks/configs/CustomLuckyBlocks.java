package me.chickenstyle.luckyblocks.configs;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import me.chickenstyle.luckyblocks.AnimationType;
import me.chickenstyle.luckyblocks.LuckyCube;
import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.Utils;

public class CustomLuckyBlocks {
	
	

	
	private static File file;
	private static YamlConfiguration config;
	public CustomLuckyBlocks(Main main) {
  	  file = new File(main.getDataFolder(), "LuckyBlocks.yml");
  	 if (!file.exists()) {
  		 try {
				 file.createNewFile();
		    	 config = YamlConfiguration.loadConfiguration(file);
		    	  	try {
		    				config.save(file);
		    		    	config = YamlConfiguration.loadConfiguration(file);
		    			} catch (IOException e) {
		    				e.printStackTrace();
		    			}
			} catch (IOException e) {
				e.printStackTrace();
			}
  	 }
  	config = YamlConfiguration.loadConfiguration(file);
   }
    
	/*
	 *  ID{int}:
	 *     title: "Looks NICE"
	 *     animationType: {type}
	 *     texture: "long af texture"
	 *     lore:
	 *     - "yes"
	 *     - "no"
	 *     items:
	 *     - {item}
	 *     - {item}
	 *	   craftRecipe:
	 *     - "XIX"
	 *     - "XDX"
	 *     - "XAX"
	 *	   ingredients: 
	 *	   - "I:diamond"
	 *     - "D:stick"
	 *     - "A:iron_ingot"
	 *
	 */
	static public ArrayList<LuckyCube> getLuckyCubes(){
    	ArrayList<LuckyCube> list = new ArrayList<LuckyCube>();
        	if (config.getConfigurationSection("LuckyBlocks") == null) return new ArrayList<LuckyCube>();
        	for (String path:config.getConfigurationSection("LuckyBlocks").getKeys(false)) {
        		int id = Integer.valueOf(path);
        		list.add(getLuckyCubeByID(id));
        	}
        	return list;

    	
    }
    
    @SuppressWarnings("deprecation")
	static public void addLuckyCube(LuckyCube lucky,HashMap<Character,ItemStack> map) {
    		config = YamlConfiguration.loadConfiguration(file);
    		String path = "LuckyBlocks." + lucky.getId();
    		
    		config.set(path + ".title", lucky.getTitle());
    		config.set(path + ".animationType", lucky.getAnimationType().toString());
    		config.set(path + ".texture", lucky.getTexture());
    		config.set(path + ".lore", lucky.getLore());
    		config.set(path + ".items", lucky.getItems());
    		
    		
    		try {
        		config.set(path + ".craftRecipe", lucky.getRecipe().getShape());
        		
        		ArrayList<String> ingredients = new ArrayList<String>();
        		
        		for (char symbol:map.keySet()) {
        			if (map.get(symbol) != null && map.get(symbol).getType() != Material.AIR) {
        		        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")) {
        		        	ingredients.add(symbol + ":" + map.get(symbol).getType());
        		        } else {
        		        	ingredients.add(symbol + ":" + map.get(symbol).getType() + "," + map.get(symbol).getData().getData());
        		        }
        			}
        		}
        		
        		config.set(path + ".ingredients", ingredients);
    		} catch (Exception ex) {
    			config.set(path + ".craftRecipe", "none");
    			config.set(path + ".ingredients", "none");
    		}

    	  	try {
    			config.save(file);
    	    	config = YamlConfiguration.loadConfiguration(file);
    		} catch (IOException e) {
    			e.printStackTrace();
    		}
    }
    
    @SuppressWarnings({ "deprecation", "unchecked" })
	static public LuckyCube getLuckyCubeByID(int id) {
		String path = "LuckyBlocks." + id;
		
		String title = config.getString(path + ".title");
		AnimationType type = AnimationType.valueOf(config.getString(path + ".animationType").toUpperCase());
		String texture = config.getString(path + ".texture");
		ArrayList<String> lore = (ArrayList<String>) config.get(path + ".lore");
		ArrayList<ItemStack> items = (ArrayList<ItemStack>) config.get(path + ".items");
		
		ItemStack item = Utils.createLuckyCube(id, title, texture, lore);
		
		ShapedRecipe recipe;
		if (Bukkit.getVersion().contains("1.12") ||
    			Bukkit.getVersion().contains("1.13") ||
    			Bukkit.getVersion().contains("1.14") ||
    			Bukkit.getVersion().contains("1.15") ||
    			Bukkit.getVersion().contains("1.16")) {
    			recipe = new ShapedRecipe(new NamespacedKey(Main.getInstance(),path),item);
    		} else {
    			recipe = new ShapedRecipe(item);
    		}
		
		
		if (!config.get(path + ".craftRecipe").equals("none")) {
			ArrayList<String> str = (ArrayList<String>) config.get(path + ".craftRecipe");
			String[] someList = {};
			recipe.shape(str.toArray(someList));
			
			
			for (String data: (ArrayList<String>)config.getList(path + ".ingredients")) {
				char symbol = data.split(":")[0].charAt(0);
		        if (Bukkit.getVersion().contains("1.13") || Bukkit.getVersion().contains("1.14") || Bukkit.getVersion().contains("1.15") || Bukkit.getVersion().contains("1.16")) {
		        	ItemStack mat = new ItemStack(Material.valueOf(data.split(":")[1].split(",")[0]));
		        	recipe.setIngredient(symbol, mat.getType());
		        } else {
		        	ItemStack mat = new ItemStack(Material.valueOf(data.split(":")[1].split(",")[0]),1,Byte.valueOf(data.split(":")[1].split(",")[1]));
		        	recipe.setIngredient(symbol, mat.getData());
		        }	
			}
		} else {
			recipe = null;
		}
		
		
		return new LuckyCube(id, title, type, texture, lore, items, recipe);
    }
    	
    static public boolean hasLuckyCube(int id) {
    	if (config.get("LuckyBlocks." + id) != null) {
    		return true;
    	}
    	return false;
    }
	
	static public void reloadConfig() {
   	 config = YamlConfiguration.loadConfiguration(file);
		try {
			config.save(file);
			config = YamlConfiguration.loadConfiguration(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
