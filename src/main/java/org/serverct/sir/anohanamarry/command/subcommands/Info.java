package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.Language;
import org.serverct.sir.anohanamarry.configuration.PlayerData;

public class Info implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            if(args.length == 1) {
                for(String msg : Language.getInstance().getPlayerInfo(playerSender.getName())) {
                    playerSender.sendMessage(msg);
                }
            } else if(args.length == 2) {
                if(PlayerData.getInstance().hasDataFile(args[1])) {
                    for(String msg : Language.getInstance().getPlayerInfo(args[1])) {
                        playerSender.sendMessage(msg);
                    }
                } else {
                    playerSender.sendMessage(Language.getInstance().getMessage("error", "Commands.Unknown.Player"));
                }
            } else {
                playerSender.sendMessage(Language.getInstance().getMessage("error", "Commands.Unknown.Param"));
            }
        }
        return true;
    }
}
