package org.serverct.sir.mood.runnable.tasks;

import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.configuration.PlayerData;

public class DataSaveTask extends BukkitRunnable {
    @Override
    public void run() {
        PlayerData.getInstance().save();
    }
}
