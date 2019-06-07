package org.serverct.sir.citylifemood.runnable.tasks;

import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.citylifemood.configuration.PlayerDataManager;

public class DataSaveTask extends BukkitRunnable {

    private int counter = 1;

    @Override
    public void run() {
        PlayerDataManager.getInstance().save();
        Bukkit.getLogger().info("[CityLifeMood] >> 第 " + counter + " 次保存玩家数据.");
        counter++;
    }
}
