package org.serverct.sir.hungu.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.hungu.Hungu;
import org.serverct.sir.hungu.command.Subcommand;
import org.serverct.sir.hungu.enums.MessageType;
import org.serverct.sir.hungu.utils.LocaleUtil;

public class Reload implements Subcommand {

    private LocaleUtil locale = Hungu.getInstance().getLocale();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("Hungu.reload")) {
            Hungu.getInstance().reload(sender);
        } else {
            sender.sendMessage(locale.getMessage(MessageType.ERROR, "Plugin", "NoPermission"));
        }
        return true;
    }
}
