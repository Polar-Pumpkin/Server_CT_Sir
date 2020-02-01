package org.serverct.sir.guajichi.command.Subcommands;

import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.guajichi.command.Subcommand;
import org.serverct.sir.guajichi.config.ConfigManager;

public class Create implements Subcommand {
    @Override
    public boolean executor(CommandSender sender, String[] args) {
        // /gjc create id money exp
        if(sender instanceof Player) {
            Player user = (Player) sender;
            if(args.length == 4) {
                if(ConfigManager.getInstance().hasWaitArea(user)) {
                    ConfigManager.getInstance().create(user, args[1], Integer.parseInt(args[2]), Integer.parseInt(args[3]));
                } else {
                    Bukkit.broadcastMessage("你没有选区");
                }
            } else {
                Bukkit.broadcastMessage("你写错命令了");
            }
        }
        return true;
    }
}
