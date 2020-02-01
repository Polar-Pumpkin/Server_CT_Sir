package org.serverct.sir.guajichi.command.Subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.guajichi.command.Subcommand;
import org.serverct.sir.guajichi.config.ConfigManager;

public class Remove implements Subcommand {
    @Override
    public boolean executor(CommandSender sender, String[] args) {
        // /gjc remove id
        if(args.length == 2) {
            if(ConfigManager.getInstance().remove(args[1])) {
                sender.sendMessage("好了");
            } else {
                sender.sendMessage("不知道的挂机池区域");
            }
        } else {
            sender.sendMessage("你写错指令了");
        }
        return true;
    }
}
