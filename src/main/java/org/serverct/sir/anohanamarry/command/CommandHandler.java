package org.serverct.sir.anohanamarry.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.anohanamarry.command.subcommands.*;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;

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
        registerSubCommand("home", new Home());
        registerSubCommand("info", new Info());
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
        } else if(subCommandMap.containsKey(args[0])) {
            return subCommandMap.get(args[0]).execute(sender, args);
        } else {
            sender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.command"));
            return true;
        }
    }
}
