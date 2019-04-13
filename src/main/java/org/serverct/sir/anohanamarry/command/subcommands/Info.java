package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;

public class Info implements SubCommand {

    private PlayerData senderData;
    private PlayerData targetData;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            senderData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(playerSender.getName());

            if(args.length == 1) {
                for(String msg : senderData.getInfo()) {
                    playerSender.sendMessage(msg);
                }
            } else if(args.length == 2) {
                if(PlayerDataManager.getInstance().getLoadedPlayerDataMap().containsKey(args[1])) {
                    targetData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(args[1]);

                    for(String msg : targetData.getInfo()) {
                        playerSender.sendMessage(msg);
                    }
                } else {
                    playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Player"));
                }
            } else {
                playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Param"));
            }
        }
        return true;
    }
}
