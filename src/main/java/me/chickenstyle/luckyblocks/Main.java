package me.chickenstyle.luckyblocks;

import me.chickenstyle.luckyblocks.configs.CustomLuckyBlocks;
import me.chickenstyle.luckyblocks.events.ClickInventoryEvent;
import me.chickenstyle.luckyblocks.events.CloseInventory;
import me.chickenstyle.luckyblocks.events.PlaceBlockEvent;
import me.chickenstyle.luckyblocks.events.PlayerStandManipulateEvent;
import me.chickenstyle.luckyblocks.versions.Handler_18_19;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.minimessage.MiniMessage;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.ArmorStand;
import org.bukkit.inventory.Recipe;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.util.*;



public class Main extends JavaPlugin{
	public static Set<ArmorStand> stands;
	public static Set<UUID> opening;
	public static HashMap<UUID,LuckyCube> creatingLuckyCube;
	private static NMSHandler versionHandler;  
	private static Main instance;
	private MiniMessage miniMessage;


	private ArrayList<NamespacedKey> recipes;
	
	
	@Override
	public void onEnable() {
		this.miniMessage = MiniMessage.miniMessage();

		//Detects server's version
		getServerVersion();
		
		instance = this;
		opening = new HashSet<>();
		creatingLuckyCube = new HashMap<>();
		stands = new HashSet<>();
		
		File config = new File(getDataFolder(),"config.yml");

		if (!config.exists()) {
			this.getConfig().options().copyDefaults();
		    saveDefaultConfig();
		}
	    new CustomLuckyBlocks(this);
		
		//Loads proper data :)
		recipes = new ArrayList<>();
		
		
		//Getting data
        int pluginId = 8664;
        Metrics metrics = new Metrics(this, pluginId);
        metrics.addCustomChart(new Metrics.SimplePie("chart_id", () -> "My value"));
		
       
		loadListeners();
		loadRecipes();
		
		Objects.requireNonNull(getCommand("luckycubes")).setExecutor(new LuckyCubesCommand());
		Objects.requireNonNull(getCommand("luckycubes")).setTabCompleter(new LuckyTab());
		
		getServer().getConsoleSender().sendMessage(Utils.color("&aLuckyCubes plugin has been enabled!"));
		
	}
	
	
	@Override
	public void onDisable() {
		
		if (!stands.isEmpty()) {
			for (ArmorStand stand:stands) {
				stand.remove();
			}
		}


		ArrayList<LuckyCube> luckyBlocks = CustomLuckyBlocks.getLuckyCubes();
		if (!luckyBlocks.isEmpty()) {
			for (LuckyCube ignored :luckyBlocks) {
				removeRecipe();
			}
		}
	}

	
	public void getServerVersion() {
		String version = Bukkit.getServer().getClass().getPackage().getName();
		version = version.substring(version.lastIndexOf(".") + 1);
		boolean isValid = true;
		if (!(version.startsWith("v1_18") || version.startsWith("v1_19"))) {
			isValid = false;
			getServer().getConsoleSender().sendMessage(parse("<RED>LuckyCubes >>> This version isn't supported!"));
			getServer().getConsoleSender().sendMessage(parse("<YELLOW>LuckyCubes >>> LuckyCubes will run anyways. However, I cannot guarantee that it will work."));
		}
		versionHandler = new Handler_18_19();
		if (isValid) {
			getServer().getConsoleSender().sendMessage(parse("<GREEN>LuckyCubes >>> NMS Version Detected: " + version));
		}
		
	}
	
	public void loadListeners() {
		
		Bukkit.getPluginManager().registerEvents(new PlaceBlockEvent(), this);
		Bukkit.getPluginManager().registerEvents(new ClickInventoryEvent(), this);
		Bukkit.getPluginManager().registerEvents(new CloseInventory(), this);
		Bukkit.getPluginManager().registerEvents(new PlayerStandManipulateEvent(), this);
	}
	
	public void loadRecipes() {
		ArrayList<LuckyCube> luckyBlocks = CustomLuckyBlocks.getLuckyCubes();

		if (!luckyBlocks.isEmpty()) {
			for (LuckyCube lucky: luckyBlocks) {
				if (lucky.getRecipe() != null) removeRecipe();
			}

		}
		
		

		if (!luckyBlocks.isEmpty()) {
			int recipesAmount = 0;
			for (LuckyCube lucky:luckyBlocks) {
				
				if (lucky.getRecipe() != null) {
					getServer().addRecipe(lucky.getRecipe());

					recipes.add(lucky.getRecipe().getKey());
				}
				recipesAmount++;

			}
			
			if (recipesAmount != 0) {
				getServer().getConsoleSender().sendMessage(ChatColor.GREEN + "LuckyCubes >>> loaded " + recipesAmount + " luckycubes!");
			} else {
				getServer().getConsoleSender().sendMessage(ChatColor.RED + "LuckyCubes >>> No luckycubes detected!");
			}
		} else {
			getServer().getConsoleSender().sendMessage(ChatColor.RED + "LuckyCubes >>> No recipes detected!");
		}
	}
	
	public void removeRecipe(){
        Iterator<Recipe> it = getServer().recipeIterator();
       
        while(it.hasNext()){
            Recipe itRecipe = it.next();
            if(itRecipe instanceof ShapedRecipe itShaped){
				if (recipes.contains(itShaped.getKey())) {
                	it.remove();
                }
            }
        }
    }
	
	public static NMSHandler getVersionHandler() {
		return versionHandler;
	}
	
	public static Main getInstance() {
		return instance;
	}

	public final Component parse(final String miniMessageString){
		return miniMessage.deserialize(miniMessageString);
	}

}
