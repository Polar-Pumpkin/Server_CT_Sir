package org.serverct.sir.citylifemood.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.potion.PotionEffect;
import org.serverct.sir.citylifemood.enums.PunishmentType;

public class Punishment {

    @Getter @Setter
    PunishmentType punishmentType;
    @Getter @Setter PotionEffect potion;
    @Getter @Setter String command;
    @Getter @Setter double money;
    @Getter @Setter double point;
    @Getter @Setter double health;
    @Getter @Setter float exhaustion;
    @Getter @Setter String message;

    private String info;

    public Punishment(PunishmentType type, Object value) {
        this.punishmentType = type;
        switch (type) {
            case POTION:
                if(value instanceof  PotionEffect) {
                    this.potion = (PotionEffect) value;
                } else {
                    Bukkit.getLogger().info("[CityLifeMood] >> 生成惩罚时遇到错误.");
                }
                break;
            case COMMAND:
                if(value instanceof String) {
                    this.command = (String) value;
                } else {
                    Bukkit.getLogger().info("[CityLifeMood] >> 生成惩罚时遇到错误.");
                }
                break;
            case MONEY:
                if(value instanceof Double) {
                    this.money = (double) value;
                } else {
                    Bukkit.getLogger().info("[CityLifeMood] >> 生成惩罚时遇到错误.");
                }
                break;
            case POINT:
                if(value instanceof Double) {
                    this.point = (double) value;
                } else {
                    Bukkit.getLogger().info("[CityLifeMood] >> 生成惩罚时遇到错误.");
                }
                break;
            case HEALTH:
                if(value instanceof Double) {
                    this.health = (double) value;
                } else {
                    Bukkit.getLogger().info("[CityLifeMood] >> 生成惩罚时遇到错误.");
                }
                break;
            case EXHAUSTION:
                if(value instanceof Float) {
                    this.exhaustion = (float) value;
                } else {
                    Bukkit.getLogger().info("[CityLifeMood] >> 生成惩罚时遇到错误.");
                }
                break;
            case MESSAGE:
                if(value instanceof String) {
                    this.message = ChatColor.translateAlternateColorCodes('&', (String) value);
                } else {
                    Bukkit.getLogger().info("[CityLifeMood] >> 生成惩罚时遇到错误.");
                }
            default:
                break;
        }
    }

    public String info() {
        switch (this.punishmentType) {
            case POTION:
                info = "&9&l" + this.punishmentType.getType() + " &7-> " + this.potion.toString();
                break;
            case COMMAND:
                info = "&9&l" + this.punishmentType.getType() + " &7-> " + this.command;
                break;
            case MESSAGE:
                info = "&9&l" + this.punishmentType.getType() + " &7-> " + this.getMessage();
                break;
            default:
                info = "(构建惩罚项概要信息遇到错误.)";
                break;
        }
        return ChatColor.translateAlternateColorCodes('&', info);
    }
}
