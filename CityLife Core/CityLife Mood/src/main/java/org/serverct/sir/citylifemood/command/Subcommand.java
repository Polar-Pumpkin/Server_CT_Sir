package org.serverct.sir.citylifemood.command;

import org.bukkit.command.CommandSender;

public interface Subcommand {
    abstract boolean execute(CommandSender sender, String[] args);
}
