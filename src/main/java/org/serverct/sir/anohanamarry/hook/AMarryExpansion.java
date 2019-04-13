package org.serverct.sir.anohanamarry.hook;

import me.clip.placeholderapi.expansion.PlaceholderExpansion;
import org.bukkit.ChatColor;
import org.bukkit.OfflinePlayer;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;
import org.serverct.sir.anohanamarry.configuration.PlayerData.StatusType;

public class AMarryExpansion extends PlaceholderExpansion {

    private PlayerData targetData;

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
            if(PlayerDataManager.getInstance().getLoadedPlayerDataMap().containsKey(player.getName())) {
                targetData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(player.getName());

                switch (targetData.getSex()) {
                    case Male:
                        return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Male"));
                    case Female:
                        return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Female"));
                    case Unknown:
                        return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Unknown"));
                }
            }
            return "ERROR";
        }

        // %amarry_status%
        if(identifier.equals("status")){
            if(PlayerDataManager.getInstance().getLoadedPlayerDataMap().containsKey(player.getName())) {
                targetData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(player.getName());

                switch (targetData.getStatus()) {
                    case Single:
                        return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Single"));
                    case Married:
                        return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Married"));
                    case FallInLove:
                        return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.FallInLove"));
                }
            }
            return "ERROR";
        }

        // %amarry_lover%
        if(identifier.equals("lover")){
            if(PlayerDataManager.getInstance().getLoadedPlayerDataMap().containsKey(player.getName())) {
                targetData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(player.getName());

                if(targetData.getStatus().equals(StatusType.Married)) {
                    return targetData.getLover();
                }
            }
            return "";
        }

        // We return null if an invalid placeholder (f.e. %example_placeholder3%)
        // was provided
        return null;
    }
}
