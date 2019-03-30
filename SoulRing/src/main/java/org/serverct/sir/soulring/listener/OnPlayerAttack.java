package org.serverct.sir.soulring.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.serverct.sir.soulring.Attributes;
import org.serverct.sir.soulring.configuration.AttributeManager;
import org.serverct.sir.soulring.configuration.LocaleManager;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class OnPlayerAttack implements Listener {

    private Map<Attributes, Double> damagerAttributesMap = new HashMap<>();
    private Map<Attributes, Double> targetAttributesMap = new HashMap<>();

    Random r = new Random();

    private Player damager;
    private ItemStack damagerItem;

    private String color;
    private double value;
    private Player target;
    private ItemStack targetItem;
    private PotionEffect potionEffect;
    private int duration;

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent evt) {
        if (evt.getDamager() instanceof Player) {
            damager = (Player) evt.getDamager();
            damagerItem = damager.getItemInHand();
            damagerAttributesMap = AttributeManager.getInstance().getAttributesFromItem(damagerItem);
            // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7玩家攻击事件触发."));

            if(damagerAttributesMap.keySet().contains(Attributes.VAMPIRE_RATE) && damagerAttributesMap.keySet().contains(Attributes.VAMPIRE_PERCENT)) {
                if(r.nextInt(100) <= damagerAttributesMap.get(Attributes.VAMPIRE_RATE)) {
                    // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7生命吸取触发."));
                    value = Math.ceil(evt.getDamage() * ((damagerAttributesMap.get(Attributes.VAMPIRE_PERCENT) / 100) + 1));
                    color = AttributeManager.getInstance().getColor(Attributes.VAMPIRE_RATE).toString();
                    // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7回复血量 " + String.valueOf(heal) + " ."));
                    if(damager.getHealth() + value <= damager.getMaxHealth()) {
                        // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7回复后血量 " + String.valueOf(damager.getHealth() + heal) + " ."));
                        damager.setHealth(damager.getHealth() + value);
                    }

                    damager.sendMessage(
                            LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "VAMPIRE")
                                    .replace("%amount%", String.valueOf(value))
                                    .replace("%color%", color)
                    );
                }
            }

            if(damagerAttributesMap.keySet().contains(Attributes.CRITICAL_RATE)) {
                if(r.nextInt(100) <= damagerAttributesMap.get(Attributes.CRITICAL_RATE)) {
                    // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7暴击触发."));
                    if(damagerAttributesMap.keySet().contains(Attributes.CRITICAL_DAMAGE)) {
                        // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7原始伤害 " + String.valueOf(evt.getDamage()) + " ."));
                        value = evt.getDamage() * damagerAttributesMap.get(Attributes.CRITICAL_DAMAGE);
                        color = AttributeManager.getInstance().getColor(Attributes.CRITICAL_DAMAGE).toString();
                        // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7暴击伤害 " + String.valueOf(damage) + " ."));

                        evt.setDamage(value);

                        damager.sendMessage(
                                LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "CRITICAL")
                                        .replace("%amount%", String.valueOf(value))
                                        .replace("%color%", color)
                        );
                    } else {
                        // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7原始伤害 " + String.valueOf(evt.getDamage()) + " ."));
                        value = evt.getDamage() * 2;
                        // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7暴击伤害 " + String.valueOf(damage) + " ."));

                        evt.setDamage(value);

                        damager.sendMessage(
                                LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "CRITICAL")
                                        .replace("%amount%", String.valueOf(value))
                                        .replace("%color%", AttributeManager.getInstance().getColor(Attributes.CRITICAL_DAMAGE).toString())
                        );
                    }
                }
            }

            if(evt.getEntity() instanceof Player) {
                target = (Player) evt.getEntity();
                targetItem = target.getItemInHand();
                targetAttributesMap = AttributeManager.getInstance().getAttributesFromItem(targetItem);

                if (damagerAttributesMap.keySet().contains(Attributes.NAUSEA)) {
                    if (r.nextInt(100) <= damagerAttributesMap.get(Attributes.NAUSEA)) {
                        // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7反胃触发."));
                        duration = AttributeManager.getInstance().getDuration(Attributes.NAUSEA);
                        potionEffect = new PotionEffect(PotionEffectType.CONFUSION, duration, 1);
                        color = AttributeManager.getInstance().getColor(Attributes.NAUSEA).toString();

                        potionEffect.apply(target);

                        damager.sendMessage(
                                LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "NAUSEA")
                                        .replace("%amount%", String.valueOf(duration))
                                        .replace("%color%", color)
                        );
                    }
                }

                if (damagerAttributesMap.keySet().contains(Attributes.IMPRISONMENT)) {
                    if (r.nextInt(100) <= damagerAttributesMap.get(Attributes.IMPRISONMENT)) {
                        // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7禁锢触发."));
                        duration = AttributeManager.getInstance().getDuration(Attributes.IMPRISONMENT);
                        potionEffect = new PotionEffect(PotionEffectType.SLOW, duration, 10);
                        color = AttributeManager.getInstance().getColor(Attributes.IMPRISONMENT).toString();

                        potionEffect.apply(target);

                        damager.sendMessage(
                                LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "IMPRISONMENT")
                                        .replace("%amount%", String.valueOf(duration))
                                        .replace("%color%", color)
                        );
                    }
                }

                if (damagerAttributesMap.keySet().contains(Attributes.BURN)) {
                    if (r.nextInt(100) <= damagerAttributesMap.get(Attributes.BURN)) {
                        // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7点燃触发."));
                        color = AttributeManager.getInstance().getColor(Attributes.BURN).toString();

                        target.setFireTicks(200);

                        damager.sendMessage(
                                LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "BURN")
                                        .replace("%color%", color)
                        );
                    }
                }

                if(targetAttributesMap.keySet().contains(Attributes.PHYSICAL_DEFENSE)) {
                    color = AttributeManager.getInstance().getColor(Attributes.PHYSICAL_DEFENSE).toString();
                    value = targetAttributesMap.get(Attributes.PHYSICAL_DEFENSE);

                    evt.setDamage(evt.getDamage() - value);

                    target.sendMessage(
                            LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "PHYSICAL_DEFENSE")
                                    .replace("%color%", color)
                                    .replace("%amount%", String.valueOf(value))
                    );
                }
            }
        }
    }
}
