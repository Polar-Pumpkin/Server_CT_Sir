package org.serverct.sir.anohanamarry.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.anohanamarry.command.subcommands.*;
import org.serverct.sir.anohanamarry.configuration.Language;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor{

    private Map<String, SubCommand> subCommandMap = new HashMap<>();

    public CommandHandler() {
        registerSubCommand("version", new Version());
        registerSubCommand("help", new Help());
        registerSubCommand("gender", new Gender());
        registerSubCommand("propose", new Propose());
        registerSubCommand("divorce", new Divorce());
        registerSubCommand("reload", new Reload());
    }

    public void registerSubCommand(String commandName, SubCommand subCommandClass) {
        if(subCommandMap.containsKey(commandName)) {

        }
        subCommandMap.put(commandName, subCommandClass);
    }

    public void unregisterSubCommand(String commandName) {
        if(!subCommandMap.containsKey(commandName)) {

        } else {
            subCommandMap.remove(commandName);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length == 0 || args[0].equalsIgnoreCase("version")) {
            return subCommandMap.get("version").execute(sender, args);
        } else if(args[0].equalsIgnoreCase("help")){
            return subCommandMap.get("help").execute(sender, args);
        } else if(args[0].equalsIgnoreCase("gender")) {
            return subCommandMap.get("gender").execute(sender, args);
        } else if(args[0].equalsIgnoreCase("propose")) {
            return subCommandMap.get("propose").execute(sender, args);
        } else if(args[0].equalsIgnoreCase("divorce")) {
            return subCommandMap.get("divorce").execute(sender, args);
        } else if(args[0].equalsIgnoreCase("reload")) {
            return subCommandMap.get("reload").execute(sender, args);
        } else {
            sender.sendMessage(Language.getLanguageClass().getMessage("error", "Commands.Unknown.command"));
            return true;
        }
    }
}
