package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.anohanamarry.command.SubCommand;

public class Debug implements SubCommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(args.length == 3 && args[1].equalsIgnoreCase("data")) {
            if(args[2].equalsIgnoreCase("loggedPlayers")) {

            }
        }
        return true;
    }
}
