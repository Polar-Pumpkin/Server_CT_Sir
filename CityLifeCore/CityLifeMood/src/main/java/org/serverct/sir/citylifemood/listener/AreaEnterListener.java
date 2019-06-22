package org.serverct.sir.citylifemood.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.serverct.sir.citylifecore.data.Area;
import org.serverct.sir.citylifecore.event.AreaEnterEvent;
import org.serverct.sir.citylifecore.utils.LocaleUtil;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.configuration.AreaManager;
import org.serverct.sir.citylifemood.data.MoodArea;
import org.serverct.sir.citylifemood.runnable.RunnableManager;

public class AreaEnterListener implements Listener {

    private LocaleUtil locale = CityLifeMood.getInstance().getLocale();

    private Area enterdArea;
    private Player enterPlayer;

    @EventHandler
    public void onPlayerEnterArea(AreaEnterEvent event) {
        enterdArea = event.getArea();
        enterPlayer = event.getPlayer();

        if(enterdArea.getId().contains("MOOD")) {
            locale.debug("玩家 " + enterPlayer.getName() + " 进入 CL Mood 区域: " + enterdArea.getId());
            for(MoodArea moodArea : AreaManager.getInstance().getAreasMap().values()) {
                locale.debug("判断心情回复区域: " + moodArea.getId());
                if(moodArea.getArea().equals(enterdArea)) {
                    locale.debug("已确认心情回复区域, 启动心情回复任务: " + moodArea.getDescription());
                    RunnableManager.getInstance().registerAreaTask(enterPlayer, moodArea);
                }
            }
        }
    }

}
