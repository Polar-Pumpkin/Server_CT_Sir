package org.serverct.sir.hungu.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.hungu.Hungu;
import org.serverct.sir.hungu.command.Subcommand;
import org.serverct.sir.hungu.configuration.HunguManager;
import org.serverct.sir.hungu.enums.MessageType;
import org.serverct.sir.hungu.utils.LocaleUtil;

public class Get implements Subcommand {

    private Player user;
    private Inventory playerInv;
    private LocaleUtil locale = Hungu.getInstance().getLocale();
    private HunguManager hunguManager;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            hunguManager = Hungu.getInstance().getHunguManager();
            user = (Player) sender;
            playerInv = user.getInventory();

            if(user.hasPermission("Hungu.get")) {
                if(hunguManager.getHunguList().containsKey(args[1])) {
                    ItemStack target = hunguManager.getHunguList().get(args[1]).getItem();

                    target.setAmount(Integer.valueOf(args[2]));
                    playerInv.addItem(target);
                } else {
                    user.sendMessage(locale.getMessage(MessageType.WARN, "Command", "Invalid.Hungu.InvalidID"));
                }
            } else {
                user.sendMessage(locale.getMessage(MessageType.ERROR, "Plugin", "NoPermission"));
            }
        } else {
            sender.sendMessage(locale.getMessage(MessageType.ERROR, "Plugin", "NotPlayer"));
        }
        return true;
    }
}
