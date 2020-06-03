package com.glyart.mcitaliavotes.managers;

import com.glyart.mcitaliavotes.MCItaliaVotesPlugin;
import com.glyart.mcitaliavotes.objects.Reward;
import lombok.Getter;
import org.bukkit.configuration.ConfigurationSection;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.logging.Level;
import java.util.stream.Collectors;

public class VotesManager {
    
    private final MCItaliaVotesPlugin plugin = MCItaliaVotesPlugin.getInstance();
    
    private final List<Reward> rewards = new ArrayList<>();
    @Getter private final List<UUID> players = Collections.synchronizedList(new ArrayList<>());
    
    @Getter private String serverName;
    @Getter private long lastUpdate;
    
    @Getter private int monthlyVotes;
    @Getter private int dailyVotes;
    
    public void enable() {
        serverName = plugin.getConfiguration().getConfig().getString("serverName");
        lastUpdate = plugin.getDataConfig().getConfig().getLong("lastUpdate");
        monthlyVotes = plugin.getDataConfig().getConfig().getInt("votes.monthly");
        dailyVotes = plugin.getDataConfig().getConfig().getInt("votes.daily");
        
        if (plugin.getDataConfig().getConfig().contains("players"))
            players.addAll(plugin.getDataConfig().getConfig().getStringList("players").stream().map(UUID::fromString).collect(Collectors.toList()));
        
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
        else if (votes < monthlyVotes)
            dailyVotes = votes;
        else
            dailyVotes = votes - monthlyVotes;
        
        monthlyVotes = votes;
        lastUpdate = System.currentTimeMillis();
        
        plugin.getLogger().info("Voti aggiornati, Voti mensili: " + monthlyVotes + ", Voti giornalieri: " + dailyVotes);
        
        plugin.getDataConfig().getConfig().set("votes.daily", dailyVotes);
        plugin.getDataConfig().getConfig().set("votes.monthly", monthlyVotes);
        plugin.getDataConfig().getConfig().set("lastUpdate", lastUpdate);
    
        plugin.getDataConfig().getConfig().set("players", players.stream().map(UUID::toString).collect(Collectors.toList()));
    
        plugin.getDataConfig().save();
    }
    
    public void savePlayers() {
        plugin.getDataConfig().getConfig().set("players", players.stream().map(UUID::toString).collect(Collectors.toList()));
        plugin.getDataConfig().save();
    }
    
}
