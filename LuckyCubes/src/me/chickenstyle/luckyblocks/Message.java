package me.chickenstyle.luckyblocks;

import org.bukkit.ChatColor;

public enum Message {

    NO_PERMISSION(color(getString("messages.noPermission"))),
    REMOVED_LUCKYCUBE(color(getString("messages.removedLuckyCube"))),
    GIVE_MESSAGE(color(getString("messages.giveMessage"))),
    GET_REWARD(color(getString("messages.getReward"))),
    COOLDOWN(color(getString("messages.cooldown"))),
    PLACE_ON_GROUND(color(getString("messages.placeOnGround")));
    private String error;

    Message(String error) {
        this.error = error;
    }

    public String getMSG() {
        return error;
    }
    
    private static String color(String text) {
    	return ChatColor.translateAlternateColorCodes('&', text);
    }
    
    private static String getString(String path) {
    	return Main.getInstance().getConfig().getString(path);
    }
}