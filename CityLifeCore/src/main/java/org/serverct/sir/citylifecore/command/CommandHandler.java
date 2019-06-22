package org.serverct.sir.citylifecore.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.citylifecore.command.subcommand.GetCommand;
import org.serverct.sir.citylifecore.command.subcommand.InfoCommand;
import org.serverct.sir.citylifecore.command.subcommand.VersionCommand;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor {

    private Map<String, Subcommand> subCommandMap = new HashMap<>();

    public CommandHandler() {
        registerSubcommand("version", new VersionCommand());
        registerSubcommand("get", new GetCommand());
        registerSubcommand("info", new InfoCommand());
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
