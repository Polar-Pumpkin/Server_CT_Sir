package org.serverct.sir.anohanamarry.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.EquipmentSlot;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.anohanamarry.configuration.ItemData;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;
import org.serverct.sir.anohanamarry.hook.AMarryEconomy;

import java.util.List;

public class OnPlayerInteractEntity implements Listener {

    private Player player;
    private Player targetPlayer;
    private List<String> targetQueue;

    private PlayerData targetData;

    @EventHandler
    public void onInteract(PlayerInteractEntityEvent evt) {
        if(evt.getRightClicked() instanceof Player) {
            targetPlayer = (Player) evt.getRightClicked();
            targetData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(targetPlayer.getName());
            player = evt.getPlayer();

            if(evt.getHand() == EquipmentSlot.HAND) {
                if(player.isSneaking()) {
                    if(ItemData.getInstance().isItem(player.getInventory().getItemInMainHand())) {
                        switch(ItemData.getInstance().getItemKey(player.getInventory().getItemInMainHand())) {
                            case "Bouquet":

                                player.getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                                break;
                            case "Ring":
                                PlayerDataManager.getInstance().sendMarryPropose(player, targetPlayer);
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
