package org.serverct.sir.mood.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.mood.MessageType;
import org.serverct.sir.mood.command.Subcommand;
import org.serverct.sir.mood.configuration.Language;
import org.serverct.sir.mood.configuration.PlayerData;

public class Admin implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("Mood.admin")) {
            if(args.length >= 2) {
                switch (args[1]) {
                    case "set":
                        if(args.length == 4) {
                            if(PlayerData.getInstance().getData().getKeys(false).contains(args[2])) {
                                PlayerData.getInstance().setMoodValue(args[2], Integer.valueOf(args[3]));
                                sender.sendMessage(
                                        Language.getInstance().getMessage(MessageType.INFO, "Mood", "Set")
                                                .replace("%player%", args[2])
                                                .replace("%amount%", args[3])
                                );
                            } else {
                                sender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Player"));
                            }
                        }
                        break;
                    default:
                        sender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
                        break;
                }
            } else {
                sender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
            }
        }
        return true;
    }
}
