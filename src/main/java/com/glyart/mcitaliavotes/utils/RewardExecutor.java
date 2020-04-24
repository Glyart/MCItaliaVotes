package com.glyart.mcitaliavotes.utils;

import com.glyart.mcitaliavotes.MCItaliaVotesPlugin;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public final class RewardExecutor {

    private RewardExecutor() {
    }

    public static void execute(Player player, String s) {
        String[] split = s.split(": ");
        String data = split[1].replaceAll("%player", player.getName());
        
        switch (split[0].toUpperCase()) {
            case "CONSOLE-CMD":
                Bukkit.dispatchCommand(Bukkit.getConsoleSender(), data);
                return;
                
            case "PLAYER-CMD":
                Bukkit.dispatchCommand(player, data);
                return;
                
            case "MESSAGE":
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', data));
                return;
                
            default:
                MCItaliaVotesPlugin.getInstance().getLogger().warning("Reward parse error: " + s);
        }
    }
    
}
