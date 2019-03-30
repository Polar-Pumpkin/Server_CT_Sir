package org.serverct.sir.soulring.command.subcommand;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.soulring.command.SubCommand;
import org.serverct.sir.soulring.configuration.LocaleManager;
import org.serverct.sir.soulring.configuration.RingManager;
import org.serverct.sir.soulring.configuration.SlotManager;

public class Absorb implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            ItemStack targetItem = playerSender.getItemInHand();
            ItemStack targetRing = playerSender.getInventory().getItem(8);
            ItemStack result;

            if(targetItem.getAmount() == 1) {
                if(SlotManager.getSlotManager().getFirstEmptySlot(targetItem) != -1) {
                    if(targetRing == null || targetRing.getType() == Material.AIR) {
                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "RingSlotEmpty"));
                    } else {
                        if(RingManager.getRingManager().isRing(targetRing)) {
                            if(targetRing.getAmount() == 1) {
                                result = SlotManager.getSlotManager().inlay(targetRing, targetItem);
                                playerSender.getInventory().setItemInHand(result);
                                playerSender.getInventory().setItem(8, new ItemStack(Material.AIR));
                                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Commands", "InlaySuccess").replace("%ringDisplay%", RingManager.getRingManager().getRingDisplay(targetRing)));
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
