package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.PlayerData;
import org.serverct.sir.anohanamarry.configuration.Language;
import org.serverct.sir.anohanamarry.hook.AMarryEconomy;

import java.util.List;

public class Propose implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            List<String> queueList = PlayerData.getInstance().getQueue(playerSender.getName());
            if(args.length == 2) {
                if (!playerSender.getName().equalsIgnoreCase(args[1])) {
                    if (PlayerData.getInstance().hasDataFile(playerSender.getName())) {
                        if (PlayerData.getInstance().hasMarried(playerSender.getName())) {
                            playerSender.sendMessage(Language.getInstance().getMessage("error", "Commands.MarryPropose.PlayerHasMarried"));
                        } else {
                            if(AMarryEconomy.getAMarryEconomyUtil().cost(playerSender.getName(), args[1], "sendPropose")) {
                                PlayerData.getInstance().sendMarryPropose(playerSender.getName(), args[1]);
                                return true;
                            }
                        }
                    } else {
                        playerSender.sendMessage(Language.getInstance().getMessage("error", "Commands.Unknown.Player"));
                    }
                } else {
                    playerSender.sendMessage(Language.getInstance().getMessage("error", "Commands.MarryPropose.CantMarryYourself"));
                }
            } else if(args.length == 3) {
                if(queueList.contains(args[2])) {
                    if (args[1].equalsIgnoreCase("accept")) {
                        if(AMarryEconomy.getAMarryEconomyUtil().cost(sender.getName(), args[2], "acceptPropose")) {
                            PlayerData.getInstance().sendProposeResult(args[2], playerSender.getName(), true);
                            return true;
                        }
                    } else if (args[1].equalsIgnoreCase("refuse")) {
                        if(AMarryEconomy.getAMarryEconomyUtil().cost(sender.getName(), args[2], "refusePropose")) {
                            PlayerData.getInstance().sendProposeResult(args[2], playerSender.getName(), false);
                            return true;
                        }
                    } else {
                        playerSender.sendMessage(Language.getInstance().getMessage("error", "Commands.Unknown.Param"));
                    }
                } else {
                    playerSender.sendMessage(Language.getInstance().getMessage("error", "Commands.MarryPropose.NotInQueueList"));
                }
            } else {
                playerSender.sendMessage(Language.getInstance().getMessage("error", "Commands.Unknown.Param"));
            }
        } else {
            sender.sendMessage(Language.getInstance().getMessage("warn", "Plugins.NotPlayer"));
        }
        return true;
    }
}
