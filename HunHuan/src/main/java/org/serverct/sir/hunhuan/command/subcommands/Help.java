package org.serverct.sir.hunhuan.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.hunhuan.HunHuan;
import org.serverct.sir.hunhuan.command.Subcommand;
import org.serverct.sir.hunhuan.utils.LocaleUtil;

public class Help implements Subcommand {

    private LocaleUtil locale = HunHuan.getInstance().getLocale();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        for(String helpMsg : locale.getData().getStringList("Plugin.HelpMessage")) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', helpMsg));
        }
        return true;
    }
}
