package me.chickenstyle.luckyblocks.events;

import me.chickenstyle.luckyblocks.LuckyCube;
import me.chickenstyle.luckyblocks.Main;
import me.chickenstyle.luckyblocks.Utils;
import me.chickenstyle.luckyblocks.configs.CustomLuckyBlocks;
import me.chickenstyle.luckyblocks.customholders.CreateRecipeHolder;
import me.chickenstyle.luckyblocks.customholders.ItemsHolder;
import me.chickenstyle.luckyblocks.customholders.LuckyCubeHolder;
import me.chickenstyle.luckyblocks.guis.CreateRecipeGui;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Random;



public class ClickInventoryEvent implements Listener{
	@SuppressWarnings("deprecation")
	@EventHandler
	public void onClickInventory(InventoryClickEvent e) {
		if (e.getInventory() == null) return;
		Player player = (Player) e.getWhoClicked();

		
		if (e.getInventory().getHolder() instanceof CreateRecipeHolder) {
			LuckyCube cube = Main.creatingLuckyCube.get(player.getUniqueId());
			if (e.getCurrentItem() == null) {
				return;
			}
			
			if (e.getCurrentItem().equals(Utils.getGrayVersionGlass())) {
				e.setCancelled(true);
			}
			if (e.getCurrentItem().equals(Utils.getGreenVersionGlass())) {
				ArrayList<Integer> emptySlots = new ArrayList<Integer>();
				emptySlots.add(12);
				emptySlots.add(13);
				emptySlots.add(14);
				emptySlots.add(21);
				emptySlots.add(22);
				emptySlots.add(23);
				emptySlots.add(30);
				emptySlots.add(31);
				emptySlots.add(32);

				ArrayList<ItemStack> materials = new ArrayList<ItemStack>();
				int airAmount = 0;
				for (Integer i:emptySlots) {
					if (e.getInventory().getItem(i) == null || e.getInventory().getItem(i).getType().equals(Material.AIR)) {
						materials.add(new ItemStack(Material.AIR));
						airAmount++;
					} else {
						materials.add(e.getInventory().getItem(i));
					}
				}


				if (airAmount == 9) {
					cube.setRecipe(null);
					CustomLuckyBlocks.addLuckyCube(cube,new HashMap<Character,ItemStack>());
					Main.creatingLuckyCube.remove(player.getUniqueId());
					player.sendMessage(ChatColor.GREEN + "LuckyCube has been created! type /lc reload to load the recipe!");
					e.setCancelled(true);
					player.closeInventory();
					return;
				}
				
				HashMap<Character,ItemStack> ingredients = new HashMap<Character,ItemStack>();
				for (ItemStack mat:materials) {
					boolean contains = false;	
					do {
						Random r = new Random();
						char symbol = (char)(r.nextInt(26) + 'a');
						if (!ingredients.isEmpty()) {
							if (!ingredients.containsKey(symbol)) {
								boolean containsMaterial = false;
								for (Entry<Character, ItemStack> entry : ingredients.entrySet()) {
								    if (entry.getValue().equals(mat)) {
								    	containsMaterial = true;
								    }
								}
								
								if (containsMaterial == false) {
									ingredients.put(symbol, mat);
								} 
								contains = true;
							}
						} else {
							ingredients.put(symbol, mat);
							contains = true;
						}
						
					} while (contains == false);
				}
				
				char matAir = 0;
				ArrayList<Character> symbols = new ArrayList<Character>(); 
				for (ItemStack mat:materials) {
					for (Entry<Character, ItemStack> entry : ingredients.entrySet()) {
					    if (mat.equals(entry.getValue())) {
					    	symbols.add(entry.getKey());
					    }
					    if (entry.getValue().getType().equals(Material.AIR)) {
					    	matAir = entry.getKey();
					    }
					}
				}
				
				
				
				String firstLine = "";
				String secondLine = "";
				String thirdLine = "";
				for (int i = 0; i < 3;i++) firstLine = (firstLine + symbols.get(i)).replace(matAir, ' ');
				for (int i = 3; i < 6;i++) secondLine = (secondLine + symbols.get(i)).replace(matAir, ' ');
				for (int i = 6; i < 9;i++) thirdLine = (thirdLine + symbols.get(i)).replace(matAir, ' ');
				
				
				ItemStack bagItem = Utils.createLuckyCube(cube);
				
        		ShapedRecipe recipe = new ShapedRecipe(new NamespacedKey(Main.getInstance(),"key"),bagItem);
				
				recipe.shape(firstLine,secondLine,thirdLine);
				
				for (Entry<Character, ItemStack> entry : ingredients.entrySet()) {
					if (!entry.getValue().getType().equals(Material.AIR) && !(entry.getValue() == null)) {
						recipe.setIngredient(entry.getKey(), entry.getValue().getData());	
					}
				}
				
				cube.setRecipe(recipe);
				CustomLuckyBlocks.addLuckyCube(cube,ingredients);
				Main.creatingLuckyCube.remove(player.getUniqueId());
				player.sendMessage(ChatColor.GREEN + "LuckyCube has been added! type /lc reload to load the recipe!");
				e.setCancelled(true);
				player.closeInventory();
			}
			
		}
		
		if (e.getInventory().getHolder() instanceof ItemsHolder) {
			ItemStack green = Utils.getGreenVersionGlass();
			ItemMeta meta = green.getItemMeta();
			meta.setDisplayName(Utils.color("&aClick here to save the items!"));
			green.setItemMeta(meta);

			if (e.getSlot() == 53) {
				e.setCancelled(true);
				ArrayList<ItemStack> items = new ArrayList<ItemStack>();
				ItemStack[] contents = e.getView().getTopInventory().getContents();
				for (int i = 0;i < 53;i++) {
					if (contents[i] != null && !contents[i].getType().equals(Material.AIR)) {
						items.add(contents[i]);
					}
				}
				
				LuckyCube cube = Main.creatingLuckyCube.get(player.getUniqueId());
				cube.setItems(items);
				Main.creatingLuckyCube.put(player.getUniqueId(), cube);
				player.sendMessage(Utils.color("&aOkey, and the now lets create the recipes!"));
				
				new CreateRecipeGui(player);
					
				
			}
		}
		
		
		if (e.getInventory().getHolder() instanceof LuckyCubeHolder) {
			e.setCancelled(true);
		}
		

	}
	
	@SuppressWarnings("deprecation")
	public static boolean isSimilar(ItemStack a, ItemStack b) {
		
		
	    if(a == null || b == null)
	        return false;
	    
	    if(a.getType() != b.getType())
	        return false;

		/*if (Bukkit.getVerrsion().contains("1.8") ||
			Bukkit.getVerrsion().contains("1.9") ||
			Bukkit.getVerrsion().contains("1.10")||
			Bukkit.getVerrsion().contains("1.11")||
			Bukkit.getVerrsion().contains("1.12")) {
			ItemStack item = new ItemStack(b.getType(),1,b.getData().getData());
			
			if (!item.getType().isBlock()) {
				item.setDurability((short) 0);
				
				
				if (a.getData().getData() != item.getData().getData()) {
					return false;
				} 
			} else {
				if (a.getData().getData() != item.getData().getData()) {
					return false;
				} 
			}
		}*/
	    

	    ItemMeta first = a.getItemMeta();
	    ItemMeta second = b.getItemMeta();
	    

	    if (first.hasDisplayName() != second.hasDisplayName())
	    	return false;
	    
	    if (first.hasDisplayName() && second.hasDisplayName()) {
		    if (!first.getDisplayName().equals(second.getDisplayName()))
		    	return false;
	    }
	    
	    
	    if (first.hasLore() && second.hasLore()) {
		    if (!first.getLore().equals(second.getLore())) 
		    	return false;
	    }
	    
	    if (!first.getEnchants().equals(second.getEnchants()))
	    	return false;  
	    
	    return true;
	}
	
}
