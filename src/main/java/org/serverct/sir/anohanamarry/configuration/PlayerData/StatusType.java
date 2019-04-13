package org.serverct.sir.anohanamarry.configuration.PlayerData;

import org.bukkit.ChatColor;
import org.serverct.sir.anohanamarry.ANOHANAMarry;

public enum StatusType {
    Single, FallInLove, Married;

    public static String formatStatus(StatusType statusType) {
        switch(statusType) {
            case Single:
                return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Status.Single"));
            case Married:
                return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Status.Married"));
            case FallInLove:
                return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Status.FallInLove"));
            default:
                return ChatColor.translateAlternateColorCodes('&', "&4&l错误");
        }
    }
}
