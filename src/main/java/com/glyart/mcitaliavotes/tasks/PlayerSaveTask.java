package com.glyart.mcitaliavotes.tasks;

import com.glyart.mcitaliavotes.MCItaliaVotesPlugin;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerSaveTask extends BukkitRunnable {
    
    private final MCItaliaVotesPlugin plugin = MCItaliaVotesPlugin.getInstance();
    
    @Override
    public void run() {
        plugin.getVotesManager().savePlayers();
    }
    
}
