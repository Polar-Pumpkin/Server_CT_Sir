package org.serverct.sir.citylifemood.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.citylifecore.enums.MessageType;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.command.Subcommand;

public class Version implements Subcommand {

    private String[] versionMsg = {
            "&c&l> &d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "",
            "  &6&lMood &7| &d&l心情 &7>>>",
            "",
            "  &7作者: &c&l&oEntityParrot_",
            "  &7版本: &c&l&o" + CityLifeMood.PLUGIN_VERSION,
            "",
            "  &7&o输入 &d/citylifemood help &7查看帮助.",
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
            sender.sendMessage(CityLifeMood.getInstance().getLocale().getMessage(MessageType.ERROR, "Commands", "Unknown.Param"));
        }
        return true;
    }
}
