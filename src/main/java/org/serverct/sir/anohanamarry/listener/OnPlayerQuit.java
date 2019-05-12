package org.serverct.sir.anohanamarry.listener;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;
import org.serverct.sir.anohanamarry.configuration.PlayerData.SexType;
import org.serverct.sir.anohanamarry.configuration.PlayerData.StatusType;

import java.util.ArrayList;

public class OnPlayerQuit implements Listener {

    private PlayerData targetData;

    @EventHandler
    public void onQuit(PlayerQuitEvent evt) {
        if(!PlayerDataManager.getInstance().getLoadedPlayerDataMap().containsKey(evt.getPlayer().getName())) {
            PlayerDataManager.getInstance().saveData(
                    new PlayerData(
                            evt.getPlayer().getName(),
                            SexType.Unknown,
                            StatusType.Single,
                            "",
                            0,
                            0,
                            "",
                            0,
                            new Location(Bukkit.getWorld("world"), 0, 0, 0),
                            new ArrayList<>(),
                            true,
                            true
                    )
            );
        } else {
            targetData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(evt.getPlayer().getName());

            if(targetData.getStatus().equals(StatusType.Married)) {
                if(Bukkit.getPlayer(targetData.getLover()).isOnline()) {
                    Language.getInstance().sendSubtitle(targetData.getLover(), Language.getInstance().getMessage("Common.LoverOffline.Subtitle").replace("%lover%", evt.getPlayer().getName()));
                }
            }
        }
    }

}
