package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;
import org.serverct.sir.anohanamarry.configuration.PlayerData.StatusType;
import org.serverct.sir.anohanamarry.hook.AMarryEconomy;

import java.util.List;

public class Propose implements SubCommand {

    private Player playerSender;
    private List<String> queueList;

    private PlayerData senderData;
    private PlayerData targetData;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            playerSender = (Player) sender;
            senderData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(playerSender.getName());
            queueList = senderData.getQueue();

            if(args.length == 2) {
                PlayerDataManager.getInstance().sendMarryPropose(playerSender, Bukkit.getPlayer(args[1]));
                return true;
            } else if(args.length == 3) {
                if(queueList.contains(args[2])) {
                    if (args[1].equalsIgnoreCase("accept")) {
                        if(AMarryEconomy.getAMarryEconomyUtil().cost(sender.getName(), args[2], "acceptPropose")) {
                            PlayerDataManager.getInstance().sendProposeResult(Bukkit.getPlayer(args[2]), playerSender, true);
                            return true;
                        }
                    } else if (args[1].equalsIgnoreCase("refuse")) {
                        if(AMarryEconomy.getAMarryEconomyUtil().cost(sender.getName(), args[2], "refusePropose")) {
                            PlayerDataManager.getInstance().sendProposeResult(Bukkit.getPlayer(args[2]), playerSender, false);
                            return true;
                        }
                    } else {
                        playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Param"));
                    }
                } else {
                    playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Propose.Marry.NotInQueueList"));
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
