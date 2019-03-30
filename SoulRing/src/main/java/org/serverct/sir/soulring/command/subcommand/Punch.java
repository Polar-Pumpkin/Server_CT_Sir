package org.serverct.sir.soulring.command.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.soulring.command.SubCommand;
import org.serverct.sir.soulring.configuration.LocaleManager;
import org.serverct.sir.soulring.configuration.SlotManager;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Punch implements SubCommand{

    private String regEx = "\\d{1,2}";
    private Pattern pattern = Pattern.compile(regEx);
    private ItemStack result;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            ItemStack targetItem = playerSender.getItemInHand();

            if(targetItem.getAmount() == 1) {
                switch (args.length) {
                    case 1:
                        result = SlotManager.getSlotManager().punch(targetItem);
                        playerSender.getInventory().setItemInHand(result);
                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Commands", "PunchSuccess"));
                        break;
                    case 2:
                        Matcher matcher = pattern.matcher(args[1]);
                        if(matcher.find()) {
                            result = SlotManager.getSlotManager().punch(targetItem, Integer.valueOf(args[1]));
                            playerSender.getInventory().setItemInHand(result);
                            playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Commands", "PunchSuccess"));
                        } else {
                            playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                        }
                        break;
                    default:
                        playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
                }
            } else {
                playerSender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "AmountError"));
            }
        } else {
            sender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Plugins", "NotPlayer"));
        }
        return true;
    }
}
