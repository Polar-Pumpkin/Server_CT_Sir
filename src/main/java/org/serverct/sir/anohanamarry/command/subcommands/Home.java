package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerData;
import org.serverct.sir.anohanamarry.configuration.PlayerData.PlayerDataManager;
import org.serverct.sir.anohanamarry.configuration.PlayerData.StatusType;

public class Home implements SubCommand {

    private PlayerData senderData;
    private Location defaultHome;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            senderData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(playerSender.getName());
            defaultHome = new Location(Bukkit.getWorld("world"), 0, 0, 0);

            if(senderData.getStatus().equals(StatusType.Married)) {
                if(args.length == 2) {
                    if(args[1].equalsIgnoreCase("set")) {
                        senderData.setHome(playerSender.getLocation());
                        PlayerDataManager.getInstance().saveData(senderData);
                        playerSender.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Commands.Home.Set"));
                    } else if(args[1].equalsIgnoreCase("delete")) {
                        if (!senderData.getHome().equals(defaultHome)) {
                            senderData.setHome(defaultHome);
                            PlayerDataManager.getInstance().saveData(senderData);
                            playerSender.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Commands.Home.Delete"));
                        }
                    }
                }

            } else {
                playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Divorce.NotMarried"));
            }
        }
        return true;
    }
}
