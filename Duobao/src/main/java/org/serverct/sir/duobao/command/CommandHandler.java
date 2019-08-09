package org.serverct.sir.duobao.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.duobao.command.subcommands.StartGame;
import org.serverct.sir.duobao.command.subcommands.StopGame;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor {

    private Map<String, Subcommand> subcommandMap = new HashMap<>();

    public CommandHandler() {
        registerSubcommand("start", new StartGame());
        registerSubcommand("stop", new StopGame());
    }

    private void registerSubcommand(String cmd, Subcommand executor) {
        subcommandMap.put(cmd, executor);
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0) {
            if(subcommandMap.containsKey(args[0])) {
                return subcommandMap.get(args[0]).execute(sender, args);
            } else {
                return subcommandMap.get("help").execute(sender, args);
            }
        } else {
            return subcommandMap.get("help").execute(sender, args);
        }
    }
}
