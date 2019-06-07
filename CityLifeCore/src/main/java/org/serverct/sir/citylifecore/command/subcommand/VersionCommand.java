package org.serverct.sir.citylifecore.command.subcommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.command.Subcommand;
import org.serverct.sir.citylifecore.configuration.LanguageData;
import org.serverct.sir.citylifecore.enums.MessageType;

public class VersionCommand implements Subcommand {

    private String[] versionMsg = {
            "&c&l> &d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "",
            "  &6&lCore &7| &d&l核心 &7>>>",
            "",
            "  &7作者: &c&l&oEntityParrot_",
            "  &7版本: &c&l&o" + CityLifeCore.getPLUGIN_VERSION(),
            "",
            "  &7&o输入 &d/clc help &7查看帮助.",
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
            sender.sendMessage(LanguageData.getInstance().getMessage(MessageType.ERROR, "Commands", "Unknown.Param"));
        }
        return true;
    }
}
