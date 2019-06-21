package org.serverct.sir.citylifecore.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.configuration.ConfigData;
import org.serverct.sir.citylifecore.manager.SelectionManager;

public class PlayerInteractListener implements Listener {

    private Player player;
    private Block targetBlock;
    private Location targetLocation;

    private SelectionManager selectAPI = CityLifeCore.getAPI().getSelectionAPI();

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        player = event.getPlayer();
        targetBlock = event.getClickedBlock();

        if(targetBlock != null && targetBlock.getType() != Material.AIR) {
            targetLocation = event.getClickedBlock().getLocation();

            if(player.getItemInHand().equals(ConfigData.getInstance().getSelector())) {
                if(event.getAction() == Action.LEFT_CLICK_BLOCK) {
                    selectAPI.setPoint1(player, targetLocation);
                    event.setCancelled(true);
                } else if(event.getAction() == Action.RIGHT_CLICK_BLOCK) {
                    selectAPI.setPoint2(player, targetLocation);
                    event.setCancelled(true);
                }
            }

            if(!player.hasPermission("CityLifeCore.blockblacklist.bypass")) {
                if(ConfigData.getInstance().getBlackList().contains(String.valueOf(targetBlock.getType().getId()))) {
                    event.setCancelled(true);
                }
            }
        }
    }

}
