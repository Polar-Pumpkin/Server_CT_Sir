package org.serverct.sir.hunhuan.listener;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerItemHeldEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.hunhuan.HunHuan;
import org.serverct.sir.hunhuan.configuration.EffectManager;
import org.serverct.sir.hunhuan.enums.HunHuanEffect;
import org.serverct.sir.hunhuan.utils.LocaleUtil;

import java.util.ArrayList;
import java.util.List;

public class ItemHeldListener implements Listener {

    private LocaleUtil locale;
    private EffectManager em;
    private Player user;
    private Inventory userInv;
    private ItemStack previousItem;
    private ItemStack targetItem;

    @EventHandler
    public void onSwitch(PlayerItemHeldEvent event) {
        locale = HunHuan.getInstance().getLocale();
        em = EffectManager.getInstance();
        user = event.getPlayer();
        userInv = user.getInventory();
        previousItem = userInv.getItem(event.getPreviousSlot());
        targetItem = userInv.getItem(event.getNewSlot());
        locale.debug("切换物品事件触发.");

        List<HunHuanEffect> previousEffects = em.listEffect(previousItem);
        List<HunHuanEffect> targetEffects = em.listEffect(targetItem);
        locale.debug("旧物品特效列表: " + previousEffects.toString());
        locale.debug("新物品特效列表: " + targetEffects.toString());

        if(previousItem == null || previousItem.getType() == Material.AIR) {
            em.clearAll(user);
            for(HunHuanEffect effect : targetEffects) {
                em.registerTask(effect, user);
            }
            return;
        }

        if(targetItem == null || targetItem.getType() == Material.AIR) {
            em.clearAll(user);
            return;
        }

        List<HunHuanEffect> middle = new ArrayList<>(targetEffects);
        middle.addAll(previousEffects);
        List<HunHuanEffect> plus = em.removeDuplicates(middle);
        for(HunHuanEffect effect : plus) {
            if(previousEffects.contains(effect) && !targetEffects.contains(effect)) {
                em.clear(effect, user);
            } else if(!previousEffects.contains(effect) && targetEffects.contains(effect)) {
                em.registerTask(effect, user);
            }
        }
    }
}
