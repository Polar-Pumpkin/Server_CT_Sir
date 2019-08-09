package org.serverct.sir.hunhuan.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.hunhuan.HunHuan;
import org.serverct.sir.hunhuan.command.Subcommand;
import org.serverct.sir.hunhuan.enums.MessageType;
import org.serverct.sir.hunhuan.utils.LocaleUtil;

public class Reload implements Subcommand {

    private LocaleUtil locale = HunHuan.getInstance().getLocale();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("Hungu.reload")) {
            HunHuan.getInstance().reload(sender);
        } else {
            sender.sendMessage(locale.getMessage(MessageType.ERROR, "Plugin", "NoPermission"));
        }
        return true;
    }
}
