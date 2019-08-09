package org.serverct.sir.tianfu.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.command.Subcommand;
import org.serverct.sir.tianfu.config.PlayerDataManager;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.util.LocaleUtil;
import org.serverct.sir.tianfu.util.PlaceholderUtil;

public class Stats implements Subcommand {

    private Player user;
    private PlayerData target;
    private String text;

    private LocaleUtil locale;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        locale = Tianfu.getInstance().getLocale();

        if(sender instanceof Player) {
            user = (Player) sender;
        }

        if(args.length == 1) {
            if(user != null) {
                target = PlayerDataManager.getInstance().getPlayerData(user.getName());
            } else {
                sender.sendMessage(ChatColor.stripColor(locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "NotPlayer")));
                return true;
            }
        } else if(args.length == 2) {
            target = PlayerDataManager.getInstance().getPlayerData(args[1]);
        } else {
            text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.Command");
            sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
            return true;
        }

        if(target != null) {
            for(String text : PlaceholderUtil.checkAll(locale.getLocales().get(Tianfu.getInstance().getLocaleKey()).getStringList("Command.Info.Player"), null, target)) {
                String result = ChatColor.translateAlternateColorCodes('&', text);
                sender.sendMessage(user != null ? result : ChatColor.stripColor(result));
            }
        } else {
            text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.Player");
            sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
        }
        return true;
    }
}
