package org.serverct.sir.citylifemood.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.citylifemood.enums.MessageType;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.command.Subcommand;
import org.serverct.sir.citylifemood.configuration.LocaleManager;

public class Reload implements Subcommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("CityLifeMood.reload")) {
            CityLifeMood.getInstance().uninit();
            CityLifeMood.getInstance().init();
            sender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.INFO, "Plugin", "ReloadSuccess"));
        } else {
            sender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Plugin", "NoPermission"));
        }
        return true;
    }
}
