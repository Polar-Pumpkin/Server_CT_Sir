package org.serverct.sir.soulring.command.subcommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.soulring.SoulRing;
import org.serverct.sir.soulring.command.SubCommand;
import org.serverct.sir.soulring.configuration.LocaleManager;

public class Help implements SubCommand {
    private String[] helpMsg = {
            "&c&l> &d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "",
            "  &6&lSoul Ring &7| &d&l魂环 &7>>>",
            "",
            "  &7作者: &c&l&oEntityParrot_",
            "  &7版本: &c&l&o" + SoulRing.PLUGIN_VERSION,
            "",
            "  &6&l指令列表 &7>>>",
            "",
            "  &7&o<> &9- &7&o必填参数.",
            "  &7&o[] &9- &7&o可选参数.",
            "",
            "  &d/sr help &9- &7查看帮助.",
            "",
            "",
            "",
            "&c&l> &d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length == 1) {
            for(String msg : helpMsg) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        } else {
            sender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Commands", "UnknownParam"));
        }
        return true;
    }
}
