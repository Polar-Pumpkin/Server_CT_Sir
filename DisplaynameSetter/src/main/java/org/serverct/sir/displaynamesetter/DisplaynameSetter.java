package org.serverct.sir.displaynamesetter;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public final class DisplaynameSetter extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginCommand("displaynamesetter").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        // /ds set player name
        // /ds reset player
        if(args.length == 3) {
            if("set".equalsIgnoreCase(args[0])) {
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null && target.isOnline()) {
                    target.setDisplayName(ChatColor.translateAlternateColorCodes('&', args[2]));
                    sender.sendMessage("设置成功.");
                } else {
                    sender.sendMessage("目标玩家不在线或不存在.");
                }
            } else {
                sender.sendMessage("未知命令, 使用 /ds set <玩家名> <名字> 设置显示名, 使用 /ds reset <玩家名> 重置显示名.");
            }
        } else if(args.length == 2) {
            if("reset".equalsIgnoreCase(args[0])) {
                Player target = Bukkit.getPlayer(args[1]);
                if(target != null && target.isOnline()) {
                    target.setDisplayName(target.getName());
                    sender.sendMessage("还原成功.");
                } else {
                    sender.sendMessage("目标玩家不在线或不存在.");
                }
            } else {
                sender.sendMessage("未知命令, 使用 /ds set <玩家名> <名字> 设置显示名, 使用 /ds reset <玩家名> 重置显示名.");
            }
        } else {
            sender.sendMessage("未知命令, 使用 /ds set <玩家名> <名字> 设置显示名, 使用 /ds reset <玩家名> 重置显示名.");
        }
        return true;
    }
}
