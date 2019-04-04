package org.serverct.sir.soulring.command.subcommand;

import org.bukkit.Effect;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.soulring.SoulRing;
import org.serverct.sir.soulring.command.SubCommand;
import org.serverct.sir.soulring.configuration.LocaleManager;
import org.serverct.sir.soulring.configuration.RingManager;
import org.serverct.sir.soulring.configuration.SlotManager;
import org.serverct.sir.soulring.hook.VaultHook;

public class Absorb implements SubCommand {
    private Player playerSender;
    private ItemStack targetItem;
    private ItemStack targetRing;
    private int price;
    private Sound successSound;
    private Sound failSound;
    private Effect successEffect;
    private Effect failEffect;
    private Location playerLocation;
    private String targetRingKey;
    private int absorbLimit;
    private ItemStack result;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            playerSender = (Player) sender;
            targetItem = playerSender.getItemInHand();
            targetRing = playerSender.getInventory().getItem(8);
            price = SoulRing.getInstance().getConfig().getInt("Economy.Absorb");
            playerLocation = playerSender.getLocation();
            if(SoulRing.getInstance().hasSoundEnabled()) {
                successSound = SoulRing.getInstance().getSound(true);
                failSound = SoulRing.getInstance().getSound(false);
            }
            if(SoulRing.getInstance().hasEffectEnabled()) {
                successEffect = SoulRing.getInstance().getEffect(true);
                failEffect = SoulRing.getInstance().getEffect(false);
            }

            if(targetItem.getAmount() == 1) {
                if(SlotManager.getSlotManager().getFirstEmptySlot(targetItem) != -1) {
                    if(targetRing == null || targetRing.getType() == Material.AIR) {
                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "RingSlotEmpty"));
                    } else {
                        if(RingManager.getRingManager().isRing(targetRing)) {
                            targetRingKey = RingManager.getRingManager().getRingKey(targetRing);
                            absorbLimit = RingManager.getRingManager().getLimit(targetRingKey);

                            if(targetRing.getAmount() == 1) {
                                if(RingManager.getRingManager().countRing(targetItem, targetRingKey) < absorbLimit) {
                                    if(VaultHook.getInstance().getBalances(playerSender) >= price) {
                                        VaultHook.getInstance().take(playerSender, price);
                                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Plugins", "CostSuccess").replace("%money%", String.valueOf(price)));
                                        result = SlotManager.getSlotManager().inlay(targetRing, targetItem);
                                        playerSender.getInventory().setItemInHand(result);
                                        playerSender.getInventory().setItem(8, new ItemStack(Material.AIR));

                                        if(SoulRing.getInstance().hasSoundEnabled()) {
                                            playerSender.playSound(playerLocation, successSound, 1F, 0F);
                                        }
                                        if(SoulRing.getInstance().hasEffectEnabled()) {
                                            playerSender.playEffect(playerLocation, successEffect, 0);
                                        }
                                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Commands", "InlaySuccess").replace("%ringDisplay%", RingManager.getRingManager().getRingDisplay(targetRing)));
                                    } else {
                                        if(SoulRing.getInstance().hasSoundEnabled()) {
                                            playerSender.playSound(playerLocation, failSound, 1F, 0F);
                                        }
                                        if(SoulRing.getInstance().hasEffectEnabled()) {
                                            playerSender.playEffect(playerLocation, failEffect, 0);
                                        }
                                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("WARN", "Plugins", "NotEnoughMoney").replace("%money%", String.valueOf(price)));
                                    }
                                } else {
                                    playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("WARN", "Commands", "CantAbsorbMore").replace("%amount%", String.valueOf(absorbLimit)));
                                }
                            } else {
                                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("WARN", "Commands", "TooManyRing"));
                            }
                        } else {
                            playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownRing"));
                        }
                    }
                } else {
                    playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "NoSlot"));
                }
            } else {
                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("WARN", "Commands", "AmountError"));
            }
        } else {
            sender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Plugins", "NotPlayer"));
        }
        return true;
    }
}
