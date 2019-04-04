package org.serverct.sir.soulring.command.subcommand;

import org.bukkit.Effect;
import org.bukkit.Location;
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

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Punch implements SubCommand{

    private String regEx = "\\d{1,2}";
    private Pattern pattern = Pattern.compile(regEx);

    private Player playerSender;
    private ItemStack targetItem;
    private int price;
    private int punchLimit;
    private Sound successSound;
    private Sound failSound;
    private Effect successEffect;
    private Effect failEffect;
    private Location playerLocation;
    private ItemStack result;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            playerSender = (Player) sender;
            targetItem = playerSender.getItemInHand();
            price = SoulRing.getInstance().getConfig().getInt("Economy.Punch");
            punchLimit = RingManager.getRingManager().getSettingData().getInt("PunchLimit");
            playerLocation = playerSender.getLocation();
            if(SoulRing.getInstance().hasSoundEnabled()) {
                successSound = SoulRing.getInstance().getSound(true);
                failSound = SoulRing.getInstance().getSound(false);
            }
            if(SoulRing.getInstance().hasEffectEnabled()) {
                successEffect = SoulRing.getInstance().getEffect(true);
                failEffect = SoulRing.getInstance().getEffect(false);
            }

            if(playerSender.hasPermission("SoulRing.Punch")) {
                if(targetItem.getAmount() == 1) {
                    if(SlotManager.getSlotManager().countAllSlot(targetItem) < punchLimit) {
                        if(VaultHook.getInstance().getBalances(playerSender) >= price) {
                            switch (args.length) {
                                case 1:
                                    VaultHook.getInstance().take(playerSender, price);
                                    playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Plugins", "CostSuccess").replace("%money%", String.valueOf(price)));
                                    result = SlotManager.getSlotManager().punch(targetItem);
                                    playerSender.getInventory().setItemInHand(result);
                                    if(SoulRing.getInstance().hasSoundEnabled()) {
                                        playerSender.playSound(playerLocation, successSound, 1F, 0F);
                                    }
                                    if(SoulRing.getInstance().hasEffectEnabled()) {
                                        playerSender.playEffect(playerLocation, successEffect, 0);
                                    }
                                    playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Commands", "PunchSuccess"));
                                    break;
                                case 2:
                                    Matcher matcher = pattern.matcher(args[1]);
                                    if(matcher.find()) {
                                        VaultHook.getInstance().take(playerSender, price);
                                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Plugins", "CostSuccess").replace("%money%", String.valueOf(price)));
                                        result = SlotManager.getSlotManager().punch(targetItem, Integer.valueOf(args[1]));
                                        playerSender.getInventory().setItemInHand(result);
                                        if(SoulRing.getInstance().hasSoundEnabled()) {
                                            playerSender.playSound(playerLocation, successSound, 1F, 0F);
                                        }
                                        if(SoulRing.getInstance().hasEffectEnabled()) {
                                            playerSender.playEffect(playerLocation, successEffect, 0);
                                        }
                                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Commands", "PunchSuccess"));
                                    } else {
                                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                                    }
                                    break;
                                default:
                                    playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                                    break;
                            }
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
                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("WARN", "Commands", "CantPunchMore").replace("%amount%", String.valueOf(punchLimit)));
                    }
                } else {
                    playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "AmountError"));
                }
            } else {
                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Plugins", "NoPermission"));
            }
        } else {
            sender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Plugins", "NotPlayer"));
        }
        return true;
    }
}
