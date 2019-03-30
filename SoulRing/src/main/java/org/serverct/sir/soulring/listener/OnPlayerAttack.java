package org.serverct.sir.soulring.listener;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
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
import org.serverct.sir.soulring.configuration.RingManager;
import org.serverct.sir.soulring.configuration.SlotManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class OnPlayerAttack implements Listener {

    private Map<Attributes, Double> finalAttributesMap = new HashMap<>();
    private Map<Attributes, Double> cacheAttributesMap;

    Random r = new Random();

    private Player damager;
    private ItemStack targetItem;
    private List<String> inlayRings;
    private String color;
    private double heal;
    private double damage;
    private Player target;
    private PotionEffect potionEffect;
    private int duration;

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent evt) {
        if(evt.getDamager() instanceof Player) {
            damager = (Player) evt.getDamager();
            targetItem = damager.getItemInHand();

            // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7玩家攻击事件触发."));

            if(SlotManager.getSlotManager().containSlot(targetItem)) {
                inlayRings = SlotManager.getSlotManager().getInlayRings(targetItem);
                // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7武器包含孔位."));

                if(!inlayRings.isEmpty()) {
                    // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7武器镶嵌有有效魂环."));

                    if(!finalAttributesMap.isEmpty()) {
                        // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7清空先前属性缓存."));
                        finalAttributesMap.clear();
                    }

                    for(String key : inlayRings) {
                        cacheAttributesMap = RingManager.getRingManager().getRingAttributes(key);

                        for(Attributes attribute : cacheAttributesMap.keySet()) {
                            if(finalAttributesMap.containsKey(attribute)) {
                                if(AttributeManager.getInstance().isPercentValue(attribute)) {
                                    finalAttributesMap.put(attribute, finalAttributesMap.get(attribute) + cacheAttributesMap.get(attribute) * 100);
                                    // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7获取到百分比属性 " + attribute.toString() + ", 覆盖属性值 " + String.valueOf(finalAttributesMap.get(attribute)) + " ."));
                                } else {
                                    finalAttributesMap.put(attribute, finalAttributesMap.get(attribute) + cacheAttributesMap.get(attribute));
                                    // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7获取到非百分比属性 " + attribute.toString() + ", 覆盖属性值 " + String.valueOf(finalAttributesMap.get(attribute)) + " ."));
                                }
                            } else {
                                if(AttributeManager.getInstance().isPercentValue(attribute)) {
                                    finalAttributesMap.put(attribute, cacheAttributesMap.get(attribute) * 100);
                                    // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7获取到百分比属性 " + attribute.toString() + ", 属性值 " + String.valueOf(finalAttributesMap.get(attribute)) + " ."));
                                } else {
                                    finalAttributesMap.put(attribute, cacheAttributesMap.get(attribute));
                                    // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7获取到非百分比属性 " + attribute.toString() + ", 属性值 " + String.valueOf(finalAttributesMap.get(attribute)) + " ."));
                                }
                            }
                        }
                    }

                    if(finalAttributesMap.keySet().contains(Attributes.VAMPIRE_RATE) && finalAttributesMap.keySet().contains(Attributes.VAMPIRE_PERCENT)) {
                        if(r.nextInt(100) <= finalAttributesMap.get(Attributes.VAMPIRE_RATE)) {
                            // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7生命吸取触发."));
                            heal = Math.ceil(evt.getDamage() * ((finalAttributesMap.get(Attributes.VAMPIRE_PERCENT) / 100) + 1));
                            color = AttributeManager.getInstance().getColor(Attributes.VAMPIRE_RATE).toString();
                            // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7回复血量 " + String.valueOf(heal) + " ."));
                            if(damager.getHealth() + heal <= damager.getMaxHealth()) {
                                // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7回复后血量 " + String.valueOf(damager.getHealth() + heal) + " ."));
                                damager.setHealth(damager.getHealth() + heal);
                            }

                            damager.sendMessage(
                                    LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "VAMPIRE")
                                            .replace("%amount%", String.valueOf(heal))
                                            .replace("%color%", color)
                            );
                        }
                    }

                    if(finalAttributesMap.keySet().contains(Attributes.CRITICAL_RATE)) {
                        if(r.nextInt(100) <= finalAttributesMap.get(Attributes.CRITICAL_RATE)) {
                            // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7暴击触发."));
                            if(finalAttributesMap.keySet().contains(Attributes.CRITICAL_DAMAGE)) {
                                // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7原始伤害 " + String.valueOf(evt.getDamage()) + " ."));
                                damage = evt.getDamage() * finalAttributesMap.get(Attributes.CRITICAL_DAMAGE);
                                color = AttributeManager.getInstance().getColor(Attributes.CRITICAL_DAMAGE).toString();
                                // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7暴击伤害 " + String.valueOf(damage) + " ."));

                                evt.setDamage(damage);

                                damager.sendMessage(
                                        LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "CRITICAL")
                                                .replace("%amount%", String.valueOf(damage))
                                                .replace("%color%", color)
                                );
                            } else {
                                // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7原始伤害 " + String.valueOf(evt.getDamage()) + " ."));
                                damage = evt.getDamage() * 2;
                                // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7暴击伤害 " + String.valueOf(damage) + " ."));

                                evt.setDamage(damage);

                                damager.sendMessage(
                                        LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "CRITICAL")
                                                .replace("%amount%", String.valueOf(damage))
                                                .replace("%color%", AttributeManager.getInstance().getColor(Attributes.CRITICAL_DAMAGE).toString())
                                );
                            }
                        }
                    }

                    if(evt.getEntity() instanceof Player) {
                        target = (Player) evt.getEntity();

                        if(finalAttributesMap.keySet().contains(Attributes.NAUSEA)) {
                            if(r.nextInt(100) <= finalAttributesMap.get(Attributes.NAUSEA)) {
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

                        if(finalAttributesMap.keySet().contains(Attributes.IMPRISONMENT)) {
                            if(r.nextInt(100) <= finalAttributesMap.get(Attributes.IMPRISONMENT)) {
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

                        if(finalAttributesMap.keySet().contains(Attributes.BURN)) {
                            if(r.nextInt(100) <= finalAttributesMap.get(Attributes.BURN)) {
                                // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', " &d> &7点燃触发."));
                                color = AttributeManager.getInstance().getColor(Attributes.BURN).toString();

                                target.setFireTicks(200);

                                damager.sendMessage(
                                        LocaleManager.getLocaleManager().getMessage("INFO", "Attributes", "BURN")
                                                .replace("%color%", color)
                                );
                            }
                        }
                    }
                }
            }
        }
    }
}
