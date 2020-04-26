package com.glyart.mcitaliavotes.tasks;

import com.glyart.mcitaliavotes.MCItaliaVotesPlugin;
import com.glyart.mcitaliavotes.utils.WebUtil;
import com.google.gson.JsonObject;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.logging.Level;

public class VersionCheckTask extends BukkitRunnable {
    
    private final MCItaliaVotesPlugin plugin = MCItaliaVotesPlugin.getInstance();
    
    @Override
    public void run() {
        String actualVersion = plugin.getDescription().getVersion();

        try {
            JsonObject latestData = WebUtil.getJsonData("https://api.github.com/repos/Glyart/MCItaliaVotes/releases/latest").getAsJsonObject();
            String version = latestData.get("tag_name").getAsString();
            
            if (version.equalsIgnoreCase(actualVersion))
                return;
            
            String downloadUrl = latestData.get("html_url").getAsString();
            plugin.getLogger().info("===================================================");
            plugin.getLogger().info("Stai usando una versione vecchia del plugin:");
            plugin.getLogger().info("Versione attuale: " + actualVersion);
            plugin.getLogger().info("Nuova versione: " + version);
            plugin.getLogger().info("Download URL: " + downloadUrl);
            plugin.getLogger().info("===================================================");
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Si è verificato un errore durante la verifica della versione", e);
        }
    }
    
}
