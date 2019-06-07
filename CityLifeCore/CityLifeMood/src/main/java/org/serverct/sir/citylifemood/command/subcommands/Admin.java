package org.serverct.sir.citylifemood.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.citylifemood.enums.MessageType;
import org.serverct.sir.citylifemood.command.Subcommand;
import org.serverct.sir.citylifemood.configuration.LocaleManager;
import org.serverct.sir.citylifemood.configuration.PlayerDataManager;

public class Admin implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("CityLifeMood.admin")) {
            if(args.length >= 2) {
                switch (args[1]) {
                    case "set":
                        if(args.length == 4) {
                            if(PlayerDataManager.getInstance().getData().getKeys(false).contains(args[2])) {
                                PlayerDataManager.getInstance().setMoodValue(args[2], Integer.valueOf(args[3]));
                                sender.sendMessage(
                                        LocaleManager.getInstance().getMessage(MessageType.INFO, "CityLifeMood", "Set")
                                                .replace("%player%", args[2])
                                                .replace("%amount%", args[3])
                                );
                            } else {
                                sender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Player"));
                            }
                        }
                        break;
                    default:
                        sender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
                        break;
                }
            } else {
                sender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
            }
        }
        return true;
    }
}
