package org.serverct.sir.citylifemood.runnable;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.citylifecore.utils.LocaleUtil;
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

    private LocaleUtil locale = CityLifeMood.getInstance().getLocale();

    @Getter private Map<Player, BukkitRunnable> areaTasksMap = new HashMap<>();

    public void registerAreaTask(Player player, MoodArea moodArea) {
        locale.debug("开始注册心情区域回复任务, 玩家: " + player.getName() + ", 心情回复区域: " + moodArea.getDescription());
        MoodUpdateTask areaTask = new MoodUpdateTask(MoodChangeType.CUSTOM, player, moodArea.getStep(), moodArea.getReason());
        locale.debug("心情更新任务已生成, 心情变动原因: " + moodArea.getReason());
        areaTask.runTaskTimer(CityLifeMood.getInstance(), moodArea.getPeriod(), moodArea.getPeriod());
        locale.debug("心情更新任务已启动.");
        areaTasksMap.put(player, areaTask);
        locale.debug("本次操作已记录.");
    }

    public void unregisterAreaTask(Player player) {
        locale.debug("开始取消心情区域回复任务, 玩家: " + player.getName());
        if(areaTasksMap.containsKey(player)) {
            locale.debug("已确认心情更新任务对象.");
            areaTasksMap.get(player).cancel();
            locale.debug("已取消心情更新任务.");
            areaTasksMap.remove(player);
            locale.debug("已删除记录.");
        }
    }

}
