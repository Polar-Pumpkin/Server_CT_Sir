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
            "  &d/sr version &9- &7查看版本信息.",
            "  &d/sr reload &9- &7重载插件.",
            "",
            "  &d/sr stat &9- &7查看自身属性.",
            "  &d/sr punch &7&l[&c行数&7&l] &9- &7给手中的武器打孔.",
            "  &d/sr ring &7&l<&c参数&7&l>",
            "    &9&l&o-> &dlist &9- &7列出所有魂环.",
            "    &9&l&o-> &dget &7&l<&c魂环ID&7&l> &7&l<&c数量&7&l> &9- &7获取指定魂环.",
            "    &9&l&o-> &dgive &7&l<&c玩家ID&7&l> &7&l<&c魂环ID&7&l> &7&l<&c数量&7&l> &9- &7给予某玩家指定魂环.",
            "  &d/sr absorb &9- &7给手中的武器吸收魂环.",
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
