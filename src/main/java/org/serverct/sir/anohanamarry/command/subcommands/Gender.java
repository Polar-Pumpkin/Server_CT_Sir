package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.PlayerData;
import org.serverct.sir.anohanamarry.configuration.Language;

public class Gender implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            if(args.length == 2) {
                if(args[1].equalsIgnoreCase("male")) {
                    PlayerData.getPlayerDataManager().setSex(playerSender.getName(), true);
                } else if(args[1].equalsIgnoreCase("female")) {
                    PlayerData.getPlayerDataManager().setSex(playerSender.getName(), false);
                } else {
                    playerSender.sendMessage(Language.getLanguageClass().getMessage("error", "Commands.Unknown.Param"));
                }
            } else {
                playerSender.sendMessage(Language.getLanguageClass().getMessage("error", "Commands.Unknown.Param"));
            }
        } else {
            sender.sendMessage(Language.getLanguageClass().getMessage("warn", "Plugins.NotPlayer"));
        }
        return true;
    }
}
