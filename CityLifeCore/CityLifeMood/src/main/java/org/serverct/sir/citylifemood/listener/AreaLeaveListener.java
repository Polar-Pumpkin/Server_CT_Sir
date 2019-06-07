package org.serverct.sir.citylifemood.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.serverct.sir.citylifecore.data.Area;
import org.serverct.sir.citylifecore.event.AreaLeaveEvent;
import org.serverct.sir.citylifemood.configuration.AreaManager;
import org.serverct.sir.citylifemood.data.MoodArea;
import org.serverct.sir.citylifemood.runnable.RunnableManager;

public class AreaLeaveListener implements Listener {

    private Area leaveArea;
    private Player leavePlayer;

    @EventHandler
    public void onPlayerLeaveArea(AreaLeaveEvent event) {
        leaveArea = event.getArea();
        leavePlayer = event.getPlayer();

        if(leaveArea.getId().contains("MOOD")) {
            for(MoodArea moodArea : AreaManager.getInstance().getAreasMap().values()) {
                if(moodArea.getArea() == leaveArea) {
                    RunnableManager.getInstance().unregisterAreaTask(leavePlayer);
                }
            }
        }
    }

}
