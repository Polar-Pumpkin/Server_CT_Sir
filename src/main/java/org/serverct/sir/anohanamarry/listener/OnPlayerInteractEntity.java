package org.serverct.sir.anohanamarry.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.anohanamarry.configuration.ItemData;
import org.serverct.sir.anohanamarry.configuration.PlayerData;
import org.serverct.sir.anohanamarry.hook.AMarryEconomy;

public class OnPlayerInteractEntity implements Listener {

    private Player player;
    private Player tatgetPlayer;

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent evt) {
        if(evt.getRightClicked() instanceof Player) {
            tatgetPlayer = (Player) evt.getRightClicked();
            player = evt.getPlayer();

            if(evt.getHand() == EquipmentSlot.HAND) {
                if(player.isSneaking()) {
                    if(ItemData.getInstance().isItem(player.getInventory().getItemInMainHand())) {
                        switch(ItemData.getInstance().getItemKey(player.getInventory().getItemInMainHand())) {
                            case "Bouquet":

                                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                break;
                            case "Ring":
                                if(AMarryEconomy.getAMarryEconomyUtil().cost(player.getName(), tatgetPlayer.getName(), "sendPropose")) {
                                    PlayerData.getInstance().sendMarryPropose(player.getName(), tatgetPlayer.getName());
                                }
                                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                break;
                            case "Certificate":
                                break;
                            default:
                                break;
                        }
                    }
                }
            }
        }
    }
}
