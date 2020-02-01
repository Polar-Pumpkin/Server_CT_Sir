package org.serverct.sir.guajichi.command;

import org.bukkit.command.CommandSender;

public interface Subcommand {
    boolean executor(CommandSender sender, String[] args);
}
