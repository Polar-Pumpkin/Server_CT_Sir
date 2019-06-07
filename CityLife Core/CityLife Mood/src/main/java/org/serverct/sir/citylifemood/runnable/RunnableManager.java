package org.serverct.sir.citylifemood.runnable;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.data.MoodArea;
import org.serverct.sir.citylifemood.enums.MoodChangeType;
import org.serverct.sir.citylifemood.runnable.tasks.MoodUpdateTask;

import java.util.HashMap;
import java.util.Map;

public class RunnableManager {

    private static RunnableManager instance;

    public static RunnableManager getInstance() {
        if(instance == null) {
            instance = new RunnableManager();
        }
        return instance;
    }

    @Getter private Map<Player, BukkitRunnable> areaTasksMap = new HashMap<>();

    public void registerAreaTask(Player player, MoodArea moodArea) {
        MoodUpdateTask areaTask = new MoodUpdateTask(MoodChangeType.CUSTOM, player, moodArea.getStep(), moodArea.getReason());
        areaTask.runTaskTimer(CityLifeMood.getInstance(), moodArea.getPeriod(), moodArea.getPeriod());
        areaTasksMap.put(player, areaTask);
    }

    public void unregisterAreaTask(Player player) {
        if(areaTasksMap.containsKey(player)) {
            areaTasksMap.get(player).cancel();
        }
    }

}
