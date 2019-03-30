package org.serverct.sir.anohanamarry.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.configuration.PlayerData;

public class AMarryExpansion extends PlaceholderExpansion {
    @Override
    public String getIdentifier() {
        return "amarry";
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

        // %amarry_gender%
        if(identifier.equals("gender")){
            if(PlayerData.getPlayerDataManager().hasDataFile(player.getName())) {
                if(PlayerData.getPlayerDataManager().getSex(player.getName()).equalsIgnoreCase("Male")) {
                    return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Male"));
                } else if(PlayerData.getPlayerDataManager().getSex(player.getName()).equalsIgnoreCase("Female")) {
                    return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Female"));
                } else if(PlayerData.getPlayerDataManager().getSex(player.getName()).equalsIgnoreCase("Unknown")) {
                    return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Unknown"));
                }
            }
            return "ERROR";
        }

        // %amarry_status%
        if(identifier.equals("status")){
            if(PlayerData.getPlayerDataManager().hasDataFile(player.getName())) {
                if(!PlayerData.getPlayerDataManager().getLover(player.getName()).equalsIgnoreCase("None")) {
                    return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Married"));
                } else {
                    return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Single"));
                }
            }
            return "ERROR";
        }

        // %amarry_lover%
        if(identifier.equals("lover")){
            if(PlayerData.getPlayerDataManager().hasDataFile(player.getName())) {
                if(PlayerData.getPlayerDataManager().hasMarried(player.getName())) {
                    return PlayerData.getPlayerDataManager().getLover(player.getName());
                }
            }
            return "";
        }

        // We return null if an invalid placeholder (f.e. %example_placeholder3%)
        // was provided
        return null;
    }
}
