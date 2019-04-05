package org.serverct.sir.soulring.command.subcommand;

import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.soulring.SoulRing;
import org.serverct.sir.soulring.command.SubCommand;
import org.serverct.sir.soulring.configuration.LocaleManager;
import org.serverct.sir.soulring.configuration.RingManager;
import org.serverct.sir.soulring.configuration.SlotManager;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Unload implements SubCommand {

    private Player playerSender;
    private ItemStack targetItem;
    private ItemMeta targetMeta;

    private List<String> targetLore;
    private int targetIndex;
    private String targetRingDisplay;
    private String targetRingKey;

    private Sound successSound;
    private Sound failSound;
    private Effect successEffect;
    private Effect failEffect;
    private Location playerLocation;

    private String regEx = "\\d{1,4}";
    private Pattern pattern = Pattern.compile(regEx);

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            playerSender = (Player) sender;
            targetItem = playerSender.getItemInHand();
            playerLocation = playerSender.getLocation();
            if(SoulRing.getInstance().hasSoundEnabled()) {
                successSound = SoulRing.getInstance().getSound(true);
                failSound = SoulRing.getInstance().getSound(false);
            }
            if(SoulRing.getInstance().hasEffectEnabled()) {
                successEffect = SoulRing.getInstance().getEffect(true);
                failEffect = SoulRing.getInstance().getEffect(false);
            }

            if(args.length == 2) {
                if(targetItem.hasItemMeta()) {
                    targetMeta = targetItem.getItemMeta();
                    targetLore = targetMeta.getLore();
                    Matcher matcher = pattern.matcher(args[1]);

                    if(matcher.find()) {
                        targetIndex = Integer.valueOf(args[1]) - 1;

                        if(targetLore.get(targetIndex).contains(RingManager.getRingManager().getFilledSlotDisplay())) {
                            targetRingDisplay = targetLore.get(targetIndex).replace(RingManager.getRingManager().getFilledSlotDisplay(), "");
                            targetRingKey = RingManager.getRingManager().getRingKey(targetRingDisplay);

                            if(targetRingKey != null) {
                                targetLore.set(targetIndex, RingManager.getRingManager().getEmptySlotDisplay());

                                targetMeta.setLore(targetLore);
                                targetItem.setItemMeta(targetMeta);
                                playerSender.getInventory().setItemInHand(targetItem);

                                playerSender.getInventory().addItem(RingManager.getRingManager().getRing(targetRingKey));

                                if(SoulRing.getInstance().hasSoundEnabled()) {
                                    playerSender.playSound(playerLocation, successSound, 1F, 0F);
                                }
                                if(SoulRing.getInstance().hasEffectEnabled()) {
                                    playerSender.playEffect(playerLocation, successEffect, 0);
                                }
                                playerSender.sendMessage(
                                        LocaleManager.getLocaleManager().getMessage("INFO", "Commands", "UnloadSuccess")
                                                .replace("%ringDisplay%", targetRingDisplay)
                                );
                            } else {
                                if(SoulRing.getInstance().hasSoundEnabled()) {
                                    playerSender.playSound(playerLocation, failSound, 1F, 0F);
                                }
                                if(SoulRing.getInstance().hasEffectEnabled()) {
                                    playerSender.playEffect(playerLocation, failEffect, 0);
                                }
                                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownRing"));
                            }
                        } else {
                            playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "NoRing"));
                        }
                    } else {
                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                    }
                } else {
                    playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "NoMeta"));
                }
            } else {
                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
            }
        } else {
            sender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Plugins", "NotPlayer"));
        }
        return true;
    }
}
