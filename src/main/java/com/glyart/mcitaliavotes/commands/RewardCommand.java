package com.glyart.mcitaliavotes.commands;

import com.glyart.mcitaliavotes.MCItaliaVotesPlugin;
import com.glyart.mcitaliavotes.objects.Reward;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class RewardCommand implements CommandExecutor {
    
    private final MCItaliaVotesPlugin plugin = MCItaliaVotesPlugin.getInstance();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player))
            return true;

        if (!sender.hasPermission("mcitaliavotes.reward")) {
            sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » " + ChatColor.RED + " Non hai il permesso per eseguire questo comando");
            return true;
        }
        
        Reward reward = plugin.getVotesManager().getReward();
        if (reward == null) {
            sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » " + ChatColor.RED + " Non ci sono rewards disponibili");
            return true;
        }
        
        Player player = (Player) sender;
        if (plugin.getVotesManager().getPlayers().contains(player.getUniqueId())) {
            sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » " + ChatColor.RED + " Hai già preso il reward di oggi");
            return true;
        }
        
        reward.executeReward(player);
        plugin.getVotesManager().getPlayers().add(player.getUniqueId());
        return true;
    }
    
}
