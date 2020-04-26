package com.glyart.mcitaliavotes.tasks;

import com.glyart.mcitaliavotes.MCItaliaVotesPlugin;
import com.glyart.mcitaliavotes.utils.WebUtil;
import com.google.gson.JsonObject;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.IOException;
import java.util.Calendar;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

public class CheckerTask extends BukkitRunnable {

    private final MCItaliaVotesPlugin plugin = MCItaliaVotesPlugin.getInstance();
    
    public void run() {
        if (plugin.getVotesManager().getServerName().equalsIgnoreCase("NO-SERVER"))
            return;
        
        if (!isToCheck())
            return;

        try {
            JsonObject json = WebUtil.getJsonData(String.format("https://www.minecraft-italia.it/api/server/item/%s", plugin.getVotesManager().getServerName())).getAsJsonObject();
            
            if (!json.get("status").getAsString().equalsIgnoreCase("success")) {
                plugin.getLogger().warning("Si è verificato un errore durante l'aggiornamento dei voti");
                plugin.getLogger().warning(json.toString());
                return;
            }
            
            JsonObject item = json.getAsJsonObject("item");
            int votes = item.get("votes").getAsInt();
            
            plugin.getVotesManager().updateVotes(votes);
        } catch (IOException e) {
            plugin.getLogger().log(Level.WARNING, "Si è verificato un errore durante l'aggiornamento dei voti", e);
        }
    }
    
    private boolean isToCheck() {
        if (plugin.getVotesManager().getMonthlyVotes() == -1)
            return true;

        if (System.currentTimeMillis() - plugin.getVotesManager().getLastUpdate() > TimeUnit.DAYS.toMillis(1))
            return true;
        
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("Europe/Rome"));
        return (System.currentTimeMillis() - plugin.getVotesManager().getLastUpdate()) > TimeUnit.HOURS.toMillis(1) && calendar.get(Calendar.HOUR_OF_DAY) == 0;
    }
    
}
