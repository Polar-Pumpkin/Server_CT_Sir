package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;
import org.serverct.sir.anohanamarry.configuration.PlayerData.SexType;

public class Gender implements SubCommand {

    private PlayerData senderData;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            senderData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(playerSender.getName());

            if(args.length == 2) {
                if(args[1].equalsIgnoreCase("male")) {
                    senderData.setSex(SexType.Male);
                    PlayerDataManager.getInstance().saveData(senderData);
                    playerSender.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Commands.Gender.Change").replace("%gender%", "男性"));
                } else if(args[1].equalsIgnoreCase("female")) {
                    senderData.setSex(SexType.Female);
                    PlayerDataManager.getInstance().saveData(senderData);
                    playerSender.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Commands.Gender.Change").replace("%gender%", "女性"));
                } else {
                    playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Param"));
                }
            } else {
                playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Param"));
            }
        } else {
            sender.sendMessage(Language.getInstance().getMessage(MessageType.ERROR, "Plugins.NotPlayer"));
        }
        return true;
    }
}
