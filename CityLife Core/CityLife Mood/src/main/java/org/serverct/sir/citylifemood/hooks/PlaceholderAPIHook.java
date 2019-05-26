package org.serverct.sir.citylifemood.hooks;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.OfflinePlayer;
import org.serverct.sir.citylifemood.configuration.PlayerDataManager;

public class PlaceholderAPIHook extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "CityLifeMood";
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
            return String.valueOf(PlayerDataManager.getInstance().getMoodValue(player.getName()));
        }

        return null;
    }
}
