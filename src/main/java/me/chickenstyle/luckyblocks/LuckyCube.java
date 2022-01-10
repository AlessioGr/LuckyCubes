package me.chickenstyle.luckyblocks;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;

import java.util.ArrayList;

public class LuckyCube {
	
	private int id;
	private String title;
	private AnimationType type;
	private String texture;
	private ArrayList<String> lore;
	private ArrayList<ItemStack> items;
	private ShapedRecipe recipe;
	
	public LuckyCube(int id, String title,AnimationType type, String texture, ArrayList<String> lore,
			ArrayList<ItemStack> items, ShapedRecipe recipe) {
		this.id = id;
		this.type = type;
		this.title = title;
		this.texture = texture;
		this.recipe = recipe;
		this.lore = lore;
		this.items = items;
	}


	public int getId() {
		return id;
	}


	public void setId(int id) {
		this.id = id;
	}


	public String getTitle() {
		return title;
	}


	public void setTitle(String title) {
		this.title = title;
	}


	public String getTexture() {
		return texture;
	}


	public void setTexture(String texture) {
		this.texture = texture;
	}


	public ShapedRecipe getRecipe() {
		return recipe;
	}


	public void setRecipe(ShapedRecipe recipe) {
		this.recipe = recipe;
	}


	public ArrayList<String> getLore() {
		return lore;
	}


	public void setLore(ArrayList<String> lore) {
		this.lore = lore;
	}


	public ArrayList<ItemStack> getItems() {
		return items;
	}


	public void setItems(ArrayList<ItemStack> items) {
		this.items = items;
	}


	public AnimationType getAnimationType() {
		return type;
	}


	public void setAnimationType(AnimationType type) {
		this.type = type;
	}
	
	
	
}
