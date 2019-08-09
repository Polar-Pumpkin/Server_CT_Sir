package org.serverct.sir.tianfu.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.command.Subcommand;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.util.LocaleUtil;

public class Reload implements Subcommand {

    private LocaleUtil locale = Tianfu.getInstance().getLocale();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("Tianfu.reload")) {
            Tianfu.getInstance().reloadConfig();
            Tianfu.getInstance().init();
            sender.sendMessage(locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Command", "ReloadSuccess"));
        } else {
            sender.sendMessage(locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "NoPermission"));
        }
        return true;
    }
}
