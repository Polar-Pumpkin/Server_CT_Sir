package org.serverct.sir.mood.runnable.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.configuration.PlayerData;

public class DataSaveTask extends BukkitRunnable {

    private int counter = 1;

    @Override
    public void run() {
        PlayerData.getInstance().save();
        Bukkit.getLogger().info("[Mood] >> 第 " + counter + " 次保存玩家数据.");
        counter++;
    }
}
