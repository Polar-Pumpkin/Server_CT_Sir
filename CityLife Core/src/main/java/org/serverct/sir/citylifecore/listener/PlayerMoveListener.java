package org.serverct.sir.citylifecore.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.data.Area;
import org.serverct.sir.citylifecore.event.AreaEnterEvent;
import org.serverct.sir.citylifecore.event.AreaLeaveEvent;
import org.serverct.sir.citylifecore.manager.AreaManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class PlayerMoveListener implements Listener {

    private Player mover;
    private AreaManager areaManager;

    private Area previousArea;


    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        areaManager = CityLifeCore.getAPI().getAreaAPI();
        mover = event.getPlayer();
        previousArea = areaManager.checkPlayer(mover);

        for(Area area : areaManager.getLoadedArea().values()) {
            if(area.hasEntered(mover.getLocation())) {
                if(previousArea != area) {
                    areaManager.unlogPlayer(previousArea, mover);
                }
                areaManager.logPlayer(area, mover);
                AreaEnterEvent areaEnterEvent = new AreaEnterEvent(mover, area);
                Bukkit.getPluginManager().callEvent(areaEnterEvent);
            } else {
                if(previousArea == area) {
                    areaManager.unlogPlayer(previousArea, mover);
                    AreaLeaveEvent areaLeaveEvent = new AreaLeaveEvent(area, mover);
                    Bukkit.getPluginManager().callEvent(areaLeaveEvent);
                }
            }
        }
    }

}
