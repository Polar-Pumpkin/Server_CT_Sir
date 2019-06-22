package org.serverct.sir.citylifemood.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.citylifecore.enums.MessageType;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.configuration.ItemManager;
import org.serverct.sir.citylifemood.configuration.PlayerDataManager;
import org.serverct.sir.citylifemood.data.Consumable;
import org.serverct.sir.citylifemood.enums.MoodChangeType;

public class PlayerInteractListener implements Listener {

    private Player user;
    private ItemStack targetItem;
    private Consumable targetConsumable;
    private double targetValue;
    private int value;
    private int targetItemAmount;

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent event) {
        targetItem = event.getItem();
        user = event.getPlayer();

        if(targetItem != null && targetItem.getType() != Material.AIR) {
            targetItemAmount = targetItem.getAmount();

            for(String key : ItemManager.getInstance().getItemMap().keySet()) {
                targetConsumable = ItemManager.getInstance().getItemMap().get(key);

                if(targetConsumable.getItem().isSimilar(targetItem)) {
                    value = targetConsumable.getValue();

                    switch (targetConsumable.getType()) {
                        case MOOD:
                            PlayerDataManager.getInstance().addMoodValue(user.getName(), value, MoodChangeType.CONSUMABLE, targetConsumable.getItem().getItemMeta().getDisplayName());
                            break;
                        case HEALTH:
                            targetValue = user.getHealth();
                            user.setHealth(targetValue + value);
                            break;
                        default:
                            break;
                    }

                    if(targetItemAmount > 1) {
                        targetItem.setAmount(targetItemAmount - 1);
                        user.setItemInHand(targetItem);
                    } else {
                        user.setItemInHand(new ItemStack(Material.AIR));
                    }
                    user.sendMessage(CityLifeMood.getInstance().getLocale().getMessage(MessageType.INFO, "Mood", "Reason.Consumables").replace("%item%", targetConsumable.getItem().getItemMeta().getDisplayName()));
                }
            }
        }
    }
}
