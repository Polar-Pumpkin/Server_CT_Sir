package org.serverct.sir.citylifemood.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.serverct.sir.citylifecore.data.Area;
import org.serverct.sir.citylifecore.event.AreaLeaveEvent;
import org.serverct.sir.citylifecore.utils.LocaleUtil;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.configuration.AreaManager;
import org.serverct.sir.citylifemood.data.MoodArea;
import org.serverct.sir.citylifemood.runnable.RunnableManager;

public class AreaLeaveListener implements Listener {

    private LocaleUtil locale = CityLifeMood.getInstance().getLocale();

    private Area leaveArea;
    private Player leavePlayer;

    @EventHandler
    public void onPlayerLeaveArea(AreaLeaveEvent event) {
        leaveArea = event.getArea();
        leavePlayer = event.getPlayer();

        if(leaveArea.getId().contains("MOOD")) {
            locale.debug("玩家 " + leavePlayer.getName() + " 离开 CL Mood 区域: " + leaveArea.getId());
            for(MoodArea moodArea : AreaManager.getInstance().getAreasMap().values()) {
                locale.debug("判断心情回复区域: " + moodArea.getId());
                if(moodArea.getArea().equals(leaveArea)) {
                    locale.debug("已确认心情回复区域, 结束心情回复任务: " + moodArea.getDescription());
                    RunnableManager.getInstance().unregisterAreaTask(leavePlayer);
                }
            }
        }
    }

}
