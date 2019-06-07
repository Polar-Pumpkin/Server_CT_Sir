package org.serverct.sir.citylifemood.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.serverct.sir.citylifecore.data.Area;
import org.serverct.sir.citylifecore.event.AreaEnterEvent;
import org.serverct.sir.citylifemood.configuration.AreaManager;
import org.serverct.sir.citylifemood.data.MoodArea;
import org.serverct.sir.citylifemood.runnable.RunnableManager;

public class AreaEnterListener implements Listener {

    private Area enterdArea;
    private Player enterPlayer;

    @EventHandler
    public void onPlayerEnterArea(AreaEnterEvent event) {
        enterdArea = event.getArea();
        enterPlayer = event.getPlayer();

        if(enterdArea.getId().contains("MOOD")) {
            for(MoodArea moodArea : AreaManager.getInstance().getAreasMap().values()) {
                if(moodArea.getArea() == enterdArea) {
                    RunnableManager.getInstance().registerAreaTask(enterPlayer, moodArea);
                }
            }
        }
    }

}
