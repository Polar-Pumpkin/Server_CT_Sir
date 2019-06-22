package org.serverct.sir.citylifemood.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.citylifecore.enums.MessageType;
import org.serverct.sir.citylifecore.utils.LocaleUtil;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.command.Subcommand;

public class Reload implements Subcommand {

    private LocaleUtil locale = CityLifeMood.getInstance().getLocale();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("CityLifeMood.reload")) {
            CityLifeMood.getInstance().uninit();
            CityLifeMood.getInstance().init();
            sender.sendMessage(locale.getMessage(MessageType.INFO, "Plugin", "ReloadSuccess"));
        } else {
            sender.sendMessage(locale.getMessage(MessageType.WARN, "Plugin", "NoPermission"));
        }
        return true;
    }
}
