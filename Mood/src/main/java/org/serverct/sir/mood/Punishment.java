package org.serverct.sir.mood;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffect;

public class Punishment {

    @Getter @Setter PunishmentType punishmentType;
    @Getter @Setter PotionEffect potion;
    @Getter @Setter String command;
    @Getter @Setter double money;
    @Getter @Setter double point;
    @Getter @Setter double health;
    @Getter @Setter float exhaustion;

    public Punishment(PunishmentType type, Object value) {
        this.punishmentType = type;
        switch (type) {
            case POTION:
                if(value instanceof  PotionEffect) {
                    this.potion = (PotionEffect) value;
                } else {
                    Bukkit.getLogger().info("[Mood] >> 生成惩罚时遇到错误.");
                }
                break;
            case COMMAND:
                if(value instanceof String) {
                    this.command = (String) value;
                } else {
                    Bukkit.getLogger().info("[Mood] >> 生成惩罚时遇到错误.");
                }
                break;
            case MONEY:
                if(value instanceof Double) {
                    this.money = (double) value;
                } else {
                    Bukkit.getLogger().info("[Mood] >> 生成惩罚时遇到错误.");
                }
                break;
            case POINT:
                if(value instanceof Double) {
                    this.point = (double) value;
                } else {
                    Bukkit.getLogger().info("[Mood] >> 生成惩罚时遇到错误.");
                }
                break;
            case HEALTH:
                if(value instanceof Double) {
                    this.health = (double) value;
                } else {
                    Bukkit.getLogger().info("[Mood] >> 生成惩罚时遇到错误.");
                }
                break;
            case EXHAUSTION:
                if(value instanceof Float) {
                    this.exhaustion = (float) value;
                } else {
                    Bukkit.getLogger().info("[Mood] >> 生成惩罚时遇到错误.");
                }
                break;
            default:
                break;
        }
    }
}
