package org.serverct.sir.mood.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.mood.Consumable;
import org.serverct.sir.mood.MessageType;
import org.serverct.sir.mood.Punishment;
import org.serverct.sir.mood.command.Subcommand;
import org.serverct.sir.mood.configuration.Config;
import org.serverct.sir.mood.configuration.Item;
import org.serverct.sir.mood.configuration.Language;

import java.util.HashMap;
import java.util.Map;

public class List implements Subcommand {

    private String listStartSuffix = "&d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄&r &c&l[ &6Mood &9| &a&l%content% 列表 &c&l]&r ┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄";

    private Consumable targetConsumable;
    private Punishment targetPunishment;
    private String message;
    private Map<Integer, Punishment> cacheMap = new HashMap<>();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length == 2) {
            switch (args[1]) {
                case "consumables":
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', listStartSuffix).replace("%content%", "消耗品"));
                    for(String key : Item.getInstance().getItemMap().keySet()) {
                        targetConsumable = Item.getInstance().getItemMap().get(key);
                        switch (targetConsumable.getType()) {
                            case MOOD:
                                message = "&a&l" + key + "&7: " + targetConsumable.getItem().getItemMeta().getDisplayName() + "&7(回复 &c" + targetConsumable.getValue() + " &7点心情)";
                                break;
                            case HEALTH:
                                message = "&a&l" + key + "&7: " + targetConsumable.getItem().getItemMeta().getDisplayName() + "&7(回复 &c" + targetConsumable.getValue() + " &7点生命)";
                                break;
                            default:
                                break;
                        }
                        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                    }
                    break;
                case "punishment":
                    sender.sendMessage(ChatColor.translateAlternateColorCodes('&', listStartSuffix).replace("%content%", "惩罚项"));
                    for(Integer limit : Config.getInstance().getPunishmentMap().keySet()) {
                        cacheMap = Config.getInstance().getPunishmentMap().get(limit);
                        for(Integer number : cacheMap.keySet()) {
                            targetPunishment = cacheMap.get(number);
                            message = "&a&l" + limit + "&7: " + targetPunishment.info();
                            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', message));
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            sender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
        }
        return true;
    }
}
