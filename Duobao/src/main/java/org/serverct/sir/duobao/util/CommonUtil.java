package org.serverct.sir.duobao.util;

import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.serverct.sir.duobao.Duobao;

public class CommonUtil {
    public static String getNoExFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static void broadcast(String message) {
        for(Player player : Duobao.getInstance().getServer().getOnlinePlayers()) {
            player.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
        }
    }
}
