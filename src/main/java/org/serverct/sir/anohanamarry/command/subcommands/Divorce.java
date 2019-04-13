package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;

public class Divorce implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            if(args.length == 1) {
                PlayerDataManager.getInstance().divorce(playerSender);
            } else {
                playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Param"));
            }
        } else {
            sender.sendMessage(Language.getInstance().getMessage(MessageType.ERROR, "Plugins.NotPlayer"));
        }
        return true;
    }
}
