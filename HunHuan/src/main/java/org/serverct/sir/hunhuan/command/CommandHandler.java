package org.serverct.sir.hunhuan.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.hunhuan.HunHuan;
import org.serverct.sir.hunhuan.command.subcommands.*;
import org.serverct.sir.hunhuan.enums.MessageType;
import org.serverct.sir.hunhuan.utils.LocaleUtil;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor {

    private LocaleUtil locale = HunHuan.getInstance().getLocale();

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
        if(args.length > 0) {
            if(subcommandMap.containsKey(args[0])) {
                return subcommandMap.get(args[0]).execute(sender, args);
            } else {
                sender.sendMessage(locale.getMessage(MessageType.ERROR, "Command", "Unknown.Command"));
            }
        } else {
            sender.sendMessage(locale.getMessage(MessageType.ERROR, "Command", "Unknown.Command"));
        }
        return true;
    }
}
