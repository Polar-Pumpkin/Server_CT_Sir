package org.serverct.sir.mood.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.mood.MessageType;
import org.serverct.sir.mood.Mood;
import org.serverct.sir.mood.MoodChangeType;
import org.serverct.sir.mood.command.Subcommand;
import org.serverct.sir.mood.configuration.Config;
import org.serverct.sir.mood.configuration.Language;
import org.serverct.sir.mood.configuration.PlayerData;
import org.serverct.sir.mood.hooks.VaultHook;

public class Do implements Subcommand {

    private Player playerSender;
    private int price;
    private int stepping;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            playerSender = (Player) sender;
            price = Config.getInstance().getData().getInt("Increase.Command.Cost");

            if(Mood.vaultHook) {
                if(VaultHook.getInstance().getBalances(playerSender) >= price) {
                    VaultHook.getInstance().take(playerSender, price);
                    playerSender.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Commands", "Cost").replace("%money%", String.valueOf(price)));
                } else {
                    return true;
                }
            }

            stepping = Config.getInstance().getData().getInt("Increase.Command.Amount");
            PlayerData.getInstance().addMoodValue(playerSender.getName(), stepping, MoodChangeType.COMMAND, null);
        } else {
            sender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Plugin", "NotPlayer"));
        }
        return true;
    }
}
