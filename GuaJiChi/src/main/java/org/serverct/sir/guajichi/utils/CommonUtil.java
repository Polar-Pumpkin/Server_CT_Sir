package org.serverct.sir.guajichi.utils;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.serverct.sir.guajichi.GuaJiChi;

public class CommonUtil {

    public static String color(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }

    public static void sendMessageAsync(String msg, Player target) {
        BukkitTask task = new BukkitRunnable() {
            @Override
            public void run() {
                target.sendMessage(color(msg));
            }
        }.runTaskLater(GuaJiChi.getInstance(), 1);
    }

    public static String getFilenameWithoutEx(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static String getNoExFileName(String fileName) {
        if ((fileName != null) && (fileName.length() > 0)) {
            int dot = fileName.lastIndexOf('.');
            if ((dot >-1) && (dot < (fileName.length()))) {
                return fileName.substring(0, dot);
            }
        }
        return fileName;
    }

    public static World getWorld(String name) {
        for(World world : Bukkit.getServer().getWorlds()) {
            if(world.getName().equals(name)) {
                return world;
            }
        }
        return null;
    }
}
