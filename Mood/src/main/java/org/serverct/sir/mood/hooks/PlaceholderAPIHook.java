package org.serverct.sir.mood.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.serverct.sir.mood.configuration.PlayerData;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "mood";
    }

    @Override
    public String getAuthor() {
        return "EntityParrot_";
    }

    @Override
    public String getVersion() {
        return "1.0.0";
    }

    @Override
    public String onRequest(OfflinePlayer player, String identifier){

        if(identifier.equals("mood")){
            return String.valueOf(PlayerData.getInstance().getMoodValue(player.getName()));
        }

        return null;
    }
}
