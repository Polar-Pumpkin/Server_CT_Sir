package org.serverct.sir.mood.command;

import org.bukkit.command.CommandSender;

public interface Subcommand {
    abstract boolean execute(CommandSender sender, String[] args);
}
