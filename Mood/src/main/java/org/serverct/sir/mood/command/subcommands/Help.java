package org.serverct.sir.mood.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.mood.MessageType;
import org.serverct.sir.mood.Mood;
import org.serverct.sir.mood.command.Subcommand;
import org.serverct.sir.mood.configuration.Language;

public class Help implements Subcommand {

    private String[] helpMsg = {
            "&d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "",
            "  &6&lMood &7| &d&l心情 &7>>>",
            "",
            "  &7作者: &c&l&oEntityParrot_",
            "  &7版本: &c&l&o" + Mood.PLUGIN_VERSION,
            "",
            "  &6&l指令列表 &7>>>",
            //"",
            //"  &7&o<> &9- &7&o必填参数.",
            //"  &7&o[] &9- &7&o可选参数.",
            "",
            "  &d/mood help &9- &7查看帮助.",
            "  &d/mood version &9- &7查看版本信息.",
            "  &d/mood reload &9- &7重载插件.",
            "",
            "  &d/mood do &9- &7消耗金钱回复心情.",
            "",
            "  &d/mood item get &7<&c消耗品ID&7> &7<&c数量&7> &9- &7获取指定数量的指定消耗品.",
            "  &d/mood item give &7<&c玩家&7> &7<&c消耗品ID&7> &7<&c数量&7> &9- &7给予指定玩家指定数量的指定消耗品.",
            "  &d/mood item save &7<&c消耗品ID&7> &7<&c类型&7> &7<&c值&7> &9- &7将手持物品保存为消耗品.",
            "",
            "&d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length == 1) {
            for(String msg : helpMsg) {
                sender.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
            }
        } else {
            sender.sendMessage(Language.getInstance().getMessage(MessageType.ERROR, "Commands", "Unknown.Param"));
        }
        return true;
    }
}
