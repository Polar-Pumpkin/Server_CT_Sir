package org.serverct.sir.mood.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.Area;
import org.serverct.sir.mood.Mood;
import org.serverct.sir.mood.MoodUpdateTaskType;
import org.serverct.sir.mood.runnable.tasks.MoodUpdateTask;

import java.util.HashMap;
import java.util.Map;

public class PlayerMoveListener implements Listener {

    private Map<Player, BukkitRunnable> areaTask = new HashMap<>();
    private Map<Integer, Area> taskIDMap = new HashMap<>();

    private Player player;
    private BukkitRunnable moodUpdateTask;

    private Location point1;
    private double sumPoint1;
    private Location point2;
    private double sumPoint2;
    private double max;
    private double min;
    private double sumTarget;

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        player = event.getPlayer();
        for(Area area : org.serverct.sir.mood.configuration.Area.getInstance().getAreasMap().values()) {
            if(isEnter(player.getLocation(), area)) {
                if(areaTask.containsKey(player)) {
                    if(!taskIDMap.get(areaTask.get(player).getTaskId()).equals(area)) {
                        taskIDMap.remove(areaTask.get(player).getTaskId());
                        areaTask.get(player).cancel();
                        areaTask.remove(player);

                        moodUpdateTask = new MoodUpdateTask(MoodUpdateTaskType.CUSTOM, player, area.getStep());
                        areaTask.put(player, moodUpdateTask);
                        taskIDMap.put(moodUpdateTask.getTaskId(), area);
                        moodUpdateTask.runTaskTimer(Mood.getInstance(), 600, area.getPeriod());
                    }
                }
            } else {
                if(areaTask.containsKey(player)) {
                    areaTask.get(player).cancel();
                    taskIDMap.remove(areaTask.get(player).getTaskId());
                    areaTask.remove(player);
                }
            }
        }
    }

    private boolean isEnter(Location loc, Area area) {
        point1 = area.getPoint1();
        point2 = area.getPoint2();
        sumPoint1 = point1.getX() + point1.getY() + point1.getZ();
        sumPoint2 = point2.getX() + point2.getY() + point2.getZ();
        max = Math.max(sumPoint1, sumPoint2);
        min = Math.min(sumPoint1, sumPoint2);
        sumTarget = loc.getX() + loc.getY() + loc.getZ();

        if(sumTarget >= min && sumTarget <= max) {
            return true;
        } else {
            return false;
        }
    }
}
