package org.serverct.sir.hungu.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.hungu.Hungu;
import org.serverct.sir.hungu.command.subcommands.*;
import org.serverct.sir.hungu.enums.MessageType;
import org.serverct.sir.hungu.utils.LocaleUtil;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor {

    private LocaleUtil locale = Hungu.getInstance().getLocale();

    private Map<String, Subcommand> subcommandMap = new HashMap<>();

    public CommandHandler() {
        registerSubcommand("list", new Help());
        registerSubcommand("xq", new Inlay());
        registerSubcommand("chai", new Unload());
        registerSubcommand("get", new Get());
        registerSubcommand("reload", new Reload());
    }

    public void registerSubcommand(String command, Subcommand executor) {
        if(!subcommandMap.containsKey(command)) {
            subcommandMap.put(command, executor);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(subcommandMap.containsKey(args[0])) {
            return subcommandMap.get(args[0]).execute(sender, args);
        } else {
            sender.sendMessage(locale.getMessage(MessageType.ERROR, "Command", "Unknown.Command"));
        }
        return true;
    }
}
