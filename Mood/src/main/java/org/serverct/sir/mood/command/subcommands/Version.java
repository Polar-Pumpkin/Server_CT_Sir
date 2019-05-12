package org.serverct.sir.mood.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.mood.MessageType;
import org.serverct.sir.mood.Mood;
import org.serverct.sir.mood.command.Subcommand;
import org.serverct.sir.mood.configuration.Language;

public class Version implements Subcommand {

    private String[] versionMsg = {
            "&c&l> &d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "",
            "  &6&lMood &7| &d&l心情 &7>>>",
            "",
            "  &7作者: &c&l&oEntityParrot_",
            "  &7版本: &c&l&o" + Mood.PLUGIN_VERSION,
            "",
            "  &7&o输入 &d/mood help &7查看帮助.",
            "",
            "&c&l> &d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length == 1) {
            for(String msg : versionMsg) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        } else {
            sender.sendMessage(Language.getInstance().getMessage(MessageType.ERROR, "Commands", "Unknown.Param"));
        }
        return true;
    }
}
