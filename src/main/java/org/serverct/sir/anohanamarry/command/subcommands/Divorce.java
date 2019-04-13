package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.PlayerData;
import org.serverct.sir.anohanamarry.configuration.Language;

public class Divorce implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            if(args.length == 1) {
                PlayerData.getInstance().divorce(playerSender.getName());
            } else {
                playerSender.sendMessage(Language.getInstance().getMessage("error", "Commands.Unknown.Param"));
            }
        } else {
            sender.sendMessage(Language.getInstance().getMessage("warn", "Plugins.NotPlayer"));
        }
        return true;
    }
}
