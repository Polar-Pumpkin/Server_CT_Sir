package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.Language;
import org.serverct.sir.anohanamarry.configuration.PlayerData;

public class Home implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            if(PlayerData.getPlayerDataManager().hasMarried(playerSender.getName())) {
                if(args.length == 2) {
                    if(args[1].equalsIgnoreCase("set")) {
                        PlayerData.getPlayerDataManager().setHomeOfLove(playerSender.getName(), playerSender.getLocation());
                        playerSender.sendMessage(Language.getLanguageClass().getMessage("info", "Commands.Home.Set"));
                    } else if(args[1].equalsIgnoreCase("delete")) {
                        if (PlayerData.getPlayerDataManager().hasHome(playerSender.getName())) {
                            PlayerData.getPlayerDataManager().removeHomeOfLove(playerSender.getName());
                            playerSender.sendMessage(Language.getLanguageClass().getMessage("info", "Commands.Home.Delete"));
                        }
                    }
                }

            } else {
                playerSender.sendMessage(Language.getLanguageClass().getMessage("error", "Commands.Divorce.NotMarried"));
            }
        }
        return true;
    }
}
