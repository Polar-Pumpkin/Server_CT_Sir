package org.serverct.sir.anohanamarry.configuration.PlayerData;

import org.bukkit.ChatColor;
import org.serverct.sir.anohanamarry.ANOHANAMarry;

public enum SexType {
    Male, Female, Unknown;

    public static String formatSex(SexType sexType) {
        switch(sexType) {
            case Male:
                return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Sex.Male"));
            case Female:
                return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Sex.Female"));
            case Unknown:
                return ChatColor.translateAlternateColorCodes('&', ANOHANAMarry.getINSTANCE().getConfig().getString("Sex.Unknown"));
            default:
                return ChatColor.translateAlternateColorCodes('&', "&4&l错误");
        }
    }
}
