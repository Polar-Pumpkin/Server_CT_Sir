package org.serverct.sir.citylifemood.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifemood.enums.MessageType;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.enums.MoodChangeType;
import org.serverct.sir.citylifemood.command.Subcommand;
import org.serverct.sir.citylifemood.configuration.ConfigManager;
import org.serverct.sir.citylifemood.configuration.LocaleManager;
import org.serverct.sir.citylifemood.configuration.PlayerDataManager;
import org.serverct.sir.citylifemood.hooks.VaultHook;

public class Do implements Subcommand {

    private Player playerSender;
    private int price;
    private int stepping;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            playerSender = (Player) sender;
            price = ConfigManager.getInstance().getData().getInt("Increase.Command.Cost");

            if(CityLifeMood.getInstance().isVaultHook()) {
                if(VaultHook.getInstance().getBalances(playerSender) >= price) {
                    VaultHook.getInstance().take(playerSender, price);
                    playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.INFO, "Commands", "Cost").replace("%money%", String.valueOf(price)));
                } else {
                    return true;
                }
            }

            stepping = ConfigManager.getInstance().getData().getInt("Increase.Command.Amount");
            PlayerDataManager.getInstance().addMoodValue(playerSender.getName(), stepping, MoodChangeType.COMMAND, null);
        } else {
            sender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Plugin", "NotPlayer"));
        }
        return true;
    }
}
