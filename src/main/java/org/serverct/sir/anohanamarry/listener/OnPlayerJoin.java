package org.serverct.sir.anohanamarry.listener;

import fr.minuskube.inv.SmartInventory;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.configuration.PlayerData;
import org.serverct.sir.anohanamarry.inventory.SelectSex;

public class OnPlayerJoin implements Listener {

    private static final SmartInventory SexSelector = SmartInventory.builder()
            .id("SexSelector")
            .provider(new SelectSex())
            .size(5, 9)
            .title("请选择您的性别")
            .build();

    @EventHandler
    public void onJoin(PlayerJoinEvent evt) {
        if(!PlayerData.getPlayerDataManager().hasDataFile(evt.getPlayer().getName())) {
            PlayerData.getPlayerDataManager().addNewPlayer(evt.getPlayer().getName());
        } else {
            if(PlayerData.getPlayerDataManager().isLoverOnline(evt.getPlayer().getName())) {
                PlayerData.getPlayerDataManager().sendLoverLoginStatusMsg(evt.getPlayer().getName(), true);
            }

            PlayerData.getPlayerDataManager().clearQueue(evt.getPlayer().getName());

            if(PlayerData.getPlayerDataManager().getSex(evt.getPlayer().getName()).equalsIgnoreCase("Unknown")) {
                SexSelector.open(evt.getPlayer());
            }
        }
    }

}
