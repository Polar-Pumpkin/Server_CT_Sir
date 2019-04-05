package org.serverct.sir.soulring.command;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.soulring.command.subcommand.*;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor {

    private Map<String, SubCommand> subCommandMap = new HashMap<>();

    public CommandHandler() {
        registerSubCommand("version", new Version());
        registerSubCommand("help", new Help());
        registerSubCommand("absorb", new Absorb());
        registerSubCommand("ring", new Ring());
        registerSubCommand("punch", new Punch());
        registerSubCommand("reload", new Reload());
        registerSubCommand("stat", new Stat());
        registerSubCommand("unload", new Unload());
    }

    public void registerSubCommand(String commandName, SubCommand subCommandClass) {
        if(subCommandMap.containsKey(commandName)) {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7侦测到重复注册命令 &c" + commandName + "&7."));
        }
        subCommandMap.put(commandName, subCommandClass);
    }

    public void unregisterSubCommand(String commandName) {
        if(!subCommandMap.containsKey(commandName)) {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7侦测到尝试删除未注册命令 &c" + commandName + "&7."));
        } else {
            subCommandMap.remove(commandName);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0) {
            return subCommandMap.get("version").execute(sender, args);
        }
        if(subCommandMap.keySet().contains(args[0])) {
            return subCommandMap.get(args[0]).execute(sender, args);
        } else if(args[0].equalsIgnoreCase("stats")) {
            return subCommandMap.get("stat").execute(sender, args);
        } else {
            return subCommandMap.get("version").execute(sender, args);
        }
    }
}
