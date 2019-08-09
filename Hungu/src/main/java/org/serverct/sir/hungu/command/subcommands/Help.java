package org.serverct.sir.hungu.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.hungu.Hungu;
import org.serverct.sir.hungu.command.Subcommand;
import org.serverct.sir.hungu.utils.LocaleUtil;

public class Help implements Subcommand {

    private LocaleUtil locale = Hungu.getInstance().getLocale();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        for(String helpMsg : locale.getData().getStringList("Plugin.HelpMessage")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMsg));
        }
        return true;
    }
}
