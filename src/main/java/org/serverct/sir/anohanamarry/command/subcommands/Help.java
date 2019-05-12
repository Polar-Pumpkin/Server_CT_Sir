package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;

public class Help implements SubCommand {

    private String[] helpMsg = {
            "&c&l> &d&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "",
            "  &6&lANOHANA Marry &7>>>",
            "",
            "  &7作者: &c&l&oEntityParrot_",
            "  &7版本: &c&l&o" + ANOHANAMarry.PLUGIN_VERSION,
            "",
            "  &6&l指令列表 &7>>>",
            "",
            "  &7&o<> &9- &7&o必填参数.",
            "  &7&o[] &9- &7&o可选参数.",
            "",
            "  &d/amarry help &9- &7查看帮助.",
            "",
            "  &d/amarry gender &7&l<&cmale&7/&cfemale&7&l> &9- &7设置自身性别.",
            "  &d/amarry propose &7&l<&c参数&7&l>",
            "    &7-> &7&l<&c玩家&7&l> &9- &7向指定玩家求婚.",
            "    &7-> &daccept &7&l<&c玩家&7&l> &9- &7同意最新一次求婚请求(或指定玩家的求婚请求).",
            "    &7-> &drefuse &7&l<&c玩家&7&l> &9- &7拒绝最新一次求婚请求(或指定玩家的求婚请求).",
            "",
            "  &d/amarry home &7&l<&c参数&7&l>",
            "    &7-> &d空&7/&dteleport &9- &7传送至爱之家.",
            "    &7-> &dset &9- &7设置爱之家.",
            "    &7-> &ddelete &9- &7删除爱之家.",
            "",
            "  &d/amarry divorce &9- &7离婚.",
            "",
            "  &d/amarry info &7&l[&c玩家&7&l] &9- &7查看自身(或指定玩家)的详细信息.",
            "",
            "  &d/amarry item &7&l<&c参数&7&l>",
            "    &7-> &dget &7&l<&c物品ID&7&l> &7&l<&c数量&7&l> &9- &7获取指定物品.",
            "    &7-> &dgive &7&l<&c玩家&7&l> &7&l<&c物品ID&7&l> &7&l<&c数量&7&l> &9- &给予指定玩家指定物品.",
            "    &7-> &dsavegift &7&l<&c礼物ID&7&l> &7&l<&c亲密点数&7&l> &9- &7保存手持物品为一个礼物.",
            "    &7-> &dremovegift &7&l<&c礼物ID&7&l> &9- &7删除指定礼物.",
            "",
            "  &d/amarry admin &7&l<&c参数&7&l>",
            "    &7-> &dpropose &7&l<&c玩家1&7&l> <&c玩家2&7&l> &9- &7强行使玩家1向玩家2求婚.",
            "    &7-> &dmarried &7&l<&c玩家1&7&l> <&c玩家2&7&l> &9- &7将两位指定玩家强行结婚(无需费用).",
            "    &7-> &ddivorce &7&l<&c玩家&7&l> &9- &7将指定玩家强行离婚.",
            "    &7-> &dgender &7&l<&c玩家&7&l> &7&l<&cmale&7/&cfemale&7&l> &9- &7强行设置指定玩家的性别.",
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
            sender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Param"));
        }
        return true;
    }
}
