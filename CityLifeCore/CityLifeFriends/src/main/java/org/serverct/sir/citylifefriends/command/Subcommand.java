package org.serverct.sir.citylifefriends.command;

import org.bukkit.command.CommandSender;

public interface Subcommand {
    boolean execute(CommandSender sender, String[] args);
}
