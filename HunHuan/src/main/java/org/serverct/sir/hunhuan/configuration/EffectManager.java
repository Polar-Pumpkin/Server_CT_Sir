package org.serverct.sir.hunhuan.configuration;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.hunhuan.HunHuan;
import org.serverct.sir.hunhuan.data.HunhuanData;
import org.serverct.sir.hunhuan.enums.HunHuanEffect;
import org.serverct.sir.hunhuan.task.EnderEffect;
import org.serverct.sir.hunhuan.task.FlameEffect;
import org.serverct.sir.hunhuan.task.PotionEffect;
import org.serverct.sir.hunhuan.task.SmokeEffect;
import org.serverct.sir.hunhuan.utils.HunhuanUtil;
import org.serverct.sir.hunhuan.utils.LocaleUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EffectManager {

    private static LocaleUtil locale;
    private static EffectManager instance;
    public static EffectManager getInstance() {
        if(instance == null) {
            instance = new EffectManager();
        }
        locale = HunHuan.getInstance().getLocale();
        return instance;
    }

    private Map<String, Map<HunHuanEffect, BukkitRunnable>> effectTask = new HashMap<>();

    public BukkitRunnable buildEffect(HunHuanEffect effect, Player player) {
        switch (effect) {
            case ENDER:
                return new EnderEffect(player);
            case FLAME:
                return new FlameEffect(player);
            case POTION:
                return new PotionEffect(player);
            case SMOKE:
                return new SmokeEffect(player);
            default:
                return null;
        }
    }

    public void registerTask(HunHuanEffect type, Player player) {
        Map<HunHuanEffect, BukkitRunnable> playerTask = effectTask.containsKey(player.getName()) ? effectTask.get(player.getName()) : new HashMap<>();
        if(!playerTask.containsKey(type)) {
            BukkitRunnable task = buildEffect(type, player);
            task.runTaskTimer(HunHuan.getInstance(), 1, 5);
            playerTask.put(type, task);
        }
        effectTask.put(player.getName(), playerTask);
    }

    public void clearAll(Player player) {
        Map<HunHuanEffect, BukkitRunnable> playerTask = effectTask.containsKey(player.getName()) ? effectTask.get(player.getName()) : new HashMap<>();
        for(BukkitRunnable task : playerTask.values()) {
            task.cancel();
        }
        playerTask.clear();
        effectTask.put(player.getName(), playerTask);
    }

    public void clear(HunHuanEffect type, Player player) {
        Map<HunHuanEffect, BukkitRunnable> playerTask = effectTask.containsKey(player.getName()) ? effectTask.get(player.getName()) : new HashMap<>();
        if(playerTask.containsKey(type)) {
            BukkitRunnable task = playerTask.get(type);
            task.cancel();
            playerTask.remove(type);
        }
        effectTask.put(player.getName(), playerTask);
    }

    public List<HunHuanEffect> listEffect(ItemStack item) {
        List<HunHuanEffect> effects = new ArrayList<>();
        if(item != null && item.getType() != Material.AIR) {
            if(item.hasItemMeta() && item.getItemMeta().hasLore()) {
                for(HunhuanData hunhuan : HunhuanUtil.getInstance().hasHungu(item.getItemMeta().getLore())) {
                    effects.addAll(hunhuan.getEffects());
                }
            }
        }
        return removeDuplicates(effects);
    }

    public List<HunHuanEffect> removeDuplicates(List<HunHuanEffect> effects) {
        List<HunHuanEffect> result = new ArrayList<>();
        for(HunHuanEffect effect : effects) {
            if(!result.contains(effect)) {
                result.add(effect);
            }
        }
        return result;
    }

}
