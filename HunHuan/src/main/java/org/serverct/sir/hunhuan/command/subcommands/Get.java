package org.serverct.sir.hunhuan.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.hunhuan.HunHuan;
import org.serverct.sir.hunhuan.command.Subcommand;
import org.serverct.sir.hunhuan.configuration.HunhuanManager;
import org.serverct.sir.hunhuan.enums.MessageType;
import org.serverct.sir.hunhuan.utils.LocaleUtil;

public class Get implements Subcommand {

    private Player user;
    private Inventory playerInv;
    private LocaleUtil locale = HunHuan.getInstance().getLocale();
    private HunhuanManager hunhuanManager;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            hunhuanManager = HunHuan.getInstance().getHunhuanManager();
            user = (Player) sender;
            playerInv = user.getInventory();

            if(user.hasPermission("Hungu.get")) {
                if(hunhuanManager.getHunguList().containsKey(args[1])) {
                    ItemStack target = hunhuanManager.getHunguList().get(args[1]).getItem();

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
