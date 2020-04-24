package com.glyart.mcitaliavotes.commands;

import com.glyart.mcitaliavotes.MCItaliaVotesPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

public class MainCommand implements CommandExecutor {
    
    private final MCItaliaVotesPlugin plugin = MCItaliaVotesPlugin.getInstance();
    
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0) {
            sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » Comandi disponibili:");
            sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » /miv reload - Reload dei rewards");
            sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » /miv credits - Crediti del plugin");
            sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » /reward - Comando per ottenere i rewards");
            credits(sender);
            return true;
        }
        
        switch (args[0].toUpperCase()) {
            case "RELOAD":
                reload(sender);
                return true;
                
            case "CREDITS":
                credits(sender);
                return true;
                
            default:
                sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » " + ChatColor.RED + "Comando non trovato");
                return true;
        }
    }
    
    private void reload(CommandSender sender) {
        if (!sender.hasPermission("mcitaliavotes.reload")) {
            sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » " + ChatColor.RED + " Non hai il permesso per eseguire questo comando");
            return;
        }

        plugin.getVotesManager().updateRewards();
        sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » Rewards ricaricati");
        credits(sender);
    }
    
    private void credits(CommandSender sender) {
        sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » Plugin creato da " + ChatColor.GOLD + "xQuickGlare (https://t.me/xQuickGlare)");
        sender.sendMessage(ChatColor.GREEN + "MCItaliaVotes » Download disponibile al seguente link: https://github.com/Glyart/MCItaliaVotes");
    }
    
}
