package org.serverct.sir.citylifemood.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.citylifemood.command.subcommands.*;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor {

    private Map<String, Subcommand> subCommandMap = new HashMap<>();

    public CommandHandler() {
        registerSubcommand("help", new Help());
        registerSubcommand("version", new Version());
        registerSubcommand("reload", new Reload());
        registerSubcommand("do", new Do());
        registerSubcommand("item", new Item());
        registerSubcommand("list", new List());
        registerSubcommand("admin", new Admin());
        registerSubcommand("select", new Select());
    }

    public void registerSubcommand(String command, Subcommand executor) {
        if(!subCommandMap.containsKey(command)) {
            subCommandMap.put(command, executor);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length != 0) {
            if(subCommandMap.containsKey(args[0])) {
                return subCommandMap.get(args[0]).execute(sender, args);
            } else {
                return subCommandMap.get("version").execute(sender, args);
            }
        } else {
            return subCommandMap.get("version").execute(sender, args);
        }
    }
}
