package com.glyart.mcitaliavotes;

import com.glyart.mcitaliavotes.commands.MainCommand;
import com.glyart.mcitaliavotes.commands.RewardCommand;
import com.glyart.mcitaliavotes.managers.VotesManager;
import com.glyart.mcitaliavotes.tasks.CheckerTask;
import com.glyart.mcitaliavotes.tasks.PlayerSaveTask;
import com.glyart.mcitaliavotes.tasks.VersionCheckTask;
import com.glyart.mcitaliavotes.utils.YAMLConfiguration;
import com.google.gson.Gson;
import lombok.Getter;
import org.bstats.bukkit.Metrics;
import org.bukkit.plugin.java.JavaPlugin;

public class MCItaliaVotesPlugin extends JavaPlugin {

    @Getter private static MCItaliaVotesPlugin instance;
    @Getter private final Gson gson = new Gson();
    
    @Getter private YAMLConfiguration configuration;
    @Getter private YAMLConfiguration dataConfig;
    
    @Getter private VotesManager votesManager;
    
    @Getter private Metrics metrics;
    
    @Override
    public void onEnable() {
        instance = this;
        
        long time = System.currentTimeMillis();
        
        registerConfigs();
        registerInstances();
        registerCommands();
        registerTasks();
    
        metrics = new Metrics(this, 7309);
        
        getLogger().info("Plugin avviato in " + (System.currentTimeMillis()-time) + "ms");
        getLogger().info("Plugin creato da xQuickGlare (https://t.me/xQuickGlare)");
        getLogger().info("Download disponibile su: https://github.com/Glyart/MCItaliaVotes");
    }

    @Override
    public void onDisable() {
        
    }
    
    private void registerConfigs() {
        configuration = new YAMLConfiguration(this, "config");
        dataConfig = new YAMLConfiguration(this, "data");
    }
    
    private void registerInstances() {
        votesManager = new VotesManager();
        votesManager.enable();
    }
    
    private void registerCommands() {
        getCommand("mcitaliavotes").setExecutor(new MainCommand());
        getCommand("reward").setExecutor(new RewardCommand());
    }
    
    private void registerTasks() {
        new VersionCheckTask().runTaskTimerAsynchronously(this, 0, 20*60*60);
        new CheckerTask().runTaskTimerAsynchronously(this, 0, 20*60*15);
        new PlayerSaveTask().runTaskTimerAsynchronously(this, 20*60*5, 20*60*5);
    }
    
}
