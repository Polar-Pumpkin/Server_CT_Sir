package org.serverct.sir.mood.runnable;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.Mood;
import org.serverct.sir.mood.Punishment;
import org.serverct.sir.mood.runnable.tasks.PunishTask;

import java.util.HashMap;
import java.util.Map;

public class PunishTaskManager {

    private static PunishTaskManager instance;

    public static PunishTaskManager getInstance() {
        if(instance == null) {
            instance = new PunishTaskManager();
        }
        return instance;
    }

    @Getter @Setter private Map<Player, Map<Integer, BukkitRunnable>> punishTaskMap = new HashMap<>();
    private Map<Integer, BukkitRunnable> cachePunishmentMap = new HashMap<>();

    private Map<Integer, Punishment> taskIDMap = new HashMap<>();

    private BukkitRunnable punishTask;

    public void startPunish(Player player, Punishment punishment) {
        punishTask = new PunishTask(player, punishment);
        punishTask.runTaskTimer(Mood.getInstance(), 20, 160);

        if(!cachePunishmentMap.isEmpty()) {
            cachePunishmentMap.clear();
        }

        if(punishTaskMap.containsKey(player)) {
            cachePunishmentMap = punishTaskMap.get(player);
            cachePunishmentMap.put(cachePunishmentMap.size() + 1, punishTask);
            punishTaskMap.put(player, cachePunishmentMap);
        } else {
            cachePunishmentMap.put(1, punishTask);
            punishTaskMap.put(player, cachePunishmentMap);
        }

        taskIDMap.put(punishTask.getTaskId(), punishment);
    }

    public void cancelPunish(Player player, Punishment punishment) {
        if(punishTaskMap.containsKey(player)) {
            if(!cachePunishmentMap.isEmpty()) {
                cachePunishmentMap.clear();
            }
            cachePunishmentMap = punishTaskMap.get(player);

            for(BukkitRunnable task : cachePunishmentMap.values()) {
                if(taskIDMap.containsKey(task.getTaskId())) {
                    if(taskIDMap.get(task.getTaskId()).equals(punishment)) {
                        task.cancel();

                        cachePunishmentMap.remove(getPunishTaskID(player, task));
                        punishTaskMap.put(player, cachePunishmentMap);
                    }
                } else {
                    task.cancel();
                }
            }
        }
    }

    public void cancelAll(Player player) {
        if(punishTaskMap.containsKey(player)) {
            if(!cachePunishmentMap.isEmpty()) {
                cachePunishmentMap.clear();
            }
            cachePunishmentMap = punishTaskMap.get(player);

            for(BukkitRunnable task : cachePunishmentMap.values()) {
                task.cancel();
            }
            punishTaskMap.remove(player);
        }
    }

    private int getPunishTaskID(Player player, BukkitRunnable punishTask) {
        if(punishTaskMap.containsKey(player)) {
            for(int id : punishTaskMap.get(player).keySet()) {
                if(punishTaskMap.get(player).get(id).equals(punishTask)) {
                    return id;
                }
            }
        }
        return -1;
    }
}
