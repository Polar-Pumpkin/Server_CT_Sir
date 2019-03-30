package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.command.SubCommand;

public class Version implements SubCommand {

    private String[] versionMsg = {
            "&c&l> &d&m                              ",
            "",
            "  &6&lANOHANA Marry &7>>>",
            "",
            "  &7作者: &c&l&oEntityParrot_",
            "  &7版本: &c&l&o" + ANOHANAMarry.PLUGIN_VERSION,
            "",
            "  &7&o输入 &d/amarry help &7查看帮助.",
            "",
            "&c&l> &d&m                              "
    };

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        for(String msg : versionMsg) {
            sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
        return true;
    }
}
