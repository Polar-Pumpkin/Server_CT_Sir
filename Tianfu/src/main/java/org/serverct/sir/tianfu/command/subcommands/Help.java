package org.serverct.sir.tianfu.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.command.Subcommand;
import org.serverct.sir.tianfu.util.LocaleUtil;

public class Help implements Subcommand {

    private LocaleUtil locale;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        locale = Tianfu.getInstance().getLocale();
        for(String helpMessage : locale.getHelpMessage(Tianfu.getInstance().getLocaleKey())) {
            String text = ChatColor.translateAlternateColorCodes('&', helpMessage);
            if(sender instanceof Player) {
                sender.sendMessage(text);
            } else {
                sender.sendMessage(ChatColor.stripColor(text));
            }
        }
        return true;
    }
}
