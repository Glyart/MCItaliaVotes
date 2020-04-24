package com.glyart.mcitaliavotes.managers;

import com.glyart.mcitaliavotes.MCItaliaVotesPlugin;
import com.glyart.mcitaliavotes.objects.Reward;
import com.glyart.mcitaliavotes.tasks.CheckerTask;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;

public class VotesManager {
    
    private final MCItaliaVotesPlugin plugin = MCItaliaVotesPlugin.getInstance();
    
    private final List<Reward> rewards = new ArrayList<>();
    
    @Getter private String serverName;
    @Getter private long lastUpdate;
    
    @Getter private int monthlyVotes;
    @Getter private int dailyVotes;
    
    public void enable() {
        serverName = plugin.getConfiguration().getConfig().getString("serverName");
        lastUpdate = plugin.getDataConfig().getConfig().getLong("lastUpdate");
        monthlyVotes = plugin.getDataConfig().getConfig().getInt("votes.monthly");
        dailyVotes = plugin.getDataConfig().getConfig().getInt("votes.daily");
        
        new CheckerTask().runTaskTimerAsynchronously(plugin, 0, 20*60*15);
        
        updateRewards();
    }
    
    public Reward getReward() {
        Reward maxReward = null;
        for (Reward reward : rewards) {
            if (reward.getVotes() > dailyVotes)
                continue;
            
            if (maxReward != null && maxReward.getVotes() > reward.getVotes())
                continue;
            
            maxReward = reward;
        }
        
        return maxReward;
    }
    
    public void updateRewards() {
        rewards.clear();

        ConfigurationSection section = plugin.getConfiguration().getConfig().getConfigurationSection("dailyRewards");
        for (String key : section.getKeys(false)) {
            try {
                int votes = Integer.parseInt(key);
                rewards.add(new Reward(votes, section.getStringList(key)));
            } catch (NumberFormatException e) {
                plugin.getLogger().log(Level.SEVERE, "Reward non validi, controllare il file di configurazione");
            }
        }
    }
    
    public void updateVotes(int votes) {
        if (monthlyVotes == -1) 
            dailyVotes = 0;
        else if (monthlyVotes < votes)
            dailyVotes = votes;
        else
            dailyVotes = votes - monthlyVotes;
        
        monthlyVotes = votes;
        lastUpdate = System.currentTimeMillis();
        
        plugin.getLogger().info("Voti aggiornati, Voti mensili: " + monthlyVotes + ", Voti giornalieri: " + dailyVotes);
        
        plugin.getDataConfig().getConfig().set("votes.daily", dailyVotes);
        plugin.getDataConfig().getConfig().set("votes.monthly", monthlyVotes);
        plugin.getDataConfig().getConfig().set("lastUpdate", lastUpdate);
        plugin.getDataConfig().save();
    }
    
}
