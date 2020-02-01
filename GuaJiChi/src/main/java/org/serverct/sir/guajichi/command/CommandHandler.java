package org.serverct.sir.guajichi.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.guajichi.command.Subcommands.Create;
import org.serverct.sir.guajichi.command.Subcommands.Remove;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor {

    public CommandHandler() {
        registerSubCommand("create", new Create());
        registerSubCommand("remove", new Remove());
    }

    private Map<String, Subcommand> subcommands = new HashMap<>();

    private void registerSubCommand(String cmd, Subcommand executor) {
        if(!subcommands.containsKey(cmd)) {
            subcommands.put(cmd, executor);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(subcommands.containsKey(args[0])) {
            return subcommands.get(args[0]).executor(sender, args);
        }
        return false;
    }
}
