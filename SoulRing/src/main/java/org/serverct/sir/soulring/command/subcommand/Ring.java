package org.serverct.sir.soulring.command.subcommand;

import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.soulring.command.SubCommand;
import org.serverct.sir.soulring.configuration.LocaleManager;
import org.serverct.sir.soulring.configuration.RingManager;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
/soulring ring
  -> create <ringKey>
  -> remove <ringKey>
  -> list
  -> get <ringKey> <amount>
  -> give <playerName> <ringKey> <amount>
 */

public class Ring implements SubCommand {

    private String[] ringsListUpper = {
            "&c&l> &d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "",
            "  &6&lSoul Ring &7| &d&l魂环 &7>>>",
            "",
            "  &9&l魂环列表 &7>>>",
            "",
    };

    private String[] ringsListLower = {
            "",
            "&c&l> &d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    private String regEx = "\\d{1,4}";
    private Pattern pattern = Pattern.compile(regEx);

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;

            if(playerSender.hasPermission("SoulRing.Ring")) {
                if(args.length != 1) {
                    switch (args[1]) {
                        case "create":
                            break;
                        case "remove":
                            break;
                        case "list":
                            if(args.length == 2) {
                                for(String msg : ringsListUpper) {
                                    playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                }

                                if(!RingManager.getRingManager().getLoadedRings().isEmpty()) {
                                    for(TextComponent msg : buildGetActionMessage()) {
                                        playerSender.spigot().sendMessage(msg);
                                    }
                                } else {
                                    playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&', "&c&l无."));
                                }

                                for(String msg : ringsListLower) {
                                    playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
                                }
                            } else {
                                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                            }
                            break;
                        case "get":
                            if(args.length == 4) {
                                if(RingManager.getRingManager().getRing(args[2]) != null) {
                                    ItemStack targetRing = RingManager.getRingManager().getRing(args[2]);
                                    Matcher matcher = pattern.matcher(args[3]);

                                    if(matcher.find()) {
                                        targetRing.setAmount(Integer.valueOf(args[3]));
                                    } else {
                                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                                        return true;
                                    }

                                    playerSender.getInventory().addItem(targetRing);

                                    playerSender.sendMessage(
                                            ChatColor.translateAlternateColorCodes(
                                                    '&',
                                                    LocaleManager.getLocaleManager().getMessage("INFO", "Commands", "RingReceivedSuccess")
                                                            .replace("%amount%", args[3])
                                                            .replace("%ringKey%", args[2])
                                            )
                                    );
                                } else {
                                    playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownRing"));
                                }
                            } else {
                                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                            }
                            break;
                        case"give":
                            if(args.length == 5) {
                                Player target = Bukkit.getPlayer(args[2]);
                                if(target != null) {
                                    if(RingManager.getRingManager().getRing(args[3]) != null) {
                                        ItemStack targetRing = RingManager.getRingManager().getRing(args[3]);
                                        Matcher matcher = pattern.matcher(args[4]);

                                        if(matcher.find()) {
                                            targetRing.setAmount(Integer.parseInt(args[4]));
                                        } else {
                                            playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                                            return true;
                                        }

                                        target.getInventory().addItem(targetRing);

                                        playerSender.sendMessage(
                                                ChatColor.translateAlternateColorCodes(
                                                        '&',
                                                        LocaleManager.getLocaleManager().getMessage("INFO", "Commands", "RingGaveSuccess")
                                                                .replace("%player%", target.getName())
                                                                .replace("%amount%", args[4])
                                                                .replace("%ringKey%", args[3])
                                                )
                                        );
                                        target.sendMessage(
                                                ChatColor.translateAlternateColorCodes(
                                                        '&',
                                                        LocaleManager.getLocaleManager().getMessage("INFO", "Commands", "RingReceivedSuccess")
                                                                .replace("%amount%", args[4])
                                                                .replace("%ringKey%", args[3])
                                                )
                                        );
                                    } else {
                                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownRing"));
                                    }
                                } else {
                                    playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "PlayerOffline"));
                                }
                            } else {
                                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                            }
                            break;
                        default:
                            playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                            break;
                    }
                } else {
                    playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                }
            } else {
                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Plugins", "NoPermission"));
            }
        } else {
            sender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Plugins", "NotPlayer"));
        }

        return true;
    }

    private List<TextComponent> buildGetActionMessage() {
        List<TextComponent> result = new ArrayList<>();
        TextComponent baseMsg = new TextComponent(TextComponent.fromLegacyText(ChatColor.translateAlternateColorCodes('&', "  &d&l> &r")));
        for(String key : RingManager.getRingManager().getLoadedRings()) {
            TextComponent message = new TextComponent(key);
            message.addExtra(
                    new TextComponent(
                            TextComponent.fromLegacyText(
                                    ChatColor.translateAlternateColorCodes(
                                            '&',
                                            ChatColor.WHITE + "(" + RingManager.getRingManager().getRingDisplay(key) + ChatColor.WHITE + ")"
                                    )
                            )
                    )
            );
            message.setClickEvent( new ClickEvent( ClickEvent.Action.SUGGEST_COMMAND, "/sr ring get " + key + " 1" ) );
            message.setHoverEvent( new HoverEvent( HoverEvent.Action.SHOW_TEXT, new ComponentBuilder("点击获取该魂环!").create() ) );

            baseMsg.addExtra(message);
            result.add(baseMsg);
        }

        return result;
    }
}
