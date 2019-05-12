package org.serverct.sir.mood.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.mood.command.subcommands.*;

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
    }

    public void registerSubcommand(String command, Subcommand executor) {
        if(!subCommandMap.containsKey(command)) {
            subCommandMap.put(command, executor);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(subCommandMap.containsKey(args[0])) {
            subCommandMap.get(args[0]).execute(sender, args);
        } else {
            subCommandMap.get("version").execute(sender, args);
        }
        return false;
    }
}
