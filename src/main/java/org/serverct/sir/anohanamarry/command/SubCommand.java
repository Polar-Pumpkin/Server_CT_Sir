package org.serverct.sir.anohanamarry.command;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    abstract boolean execute(CommandSender sender, String[] args);
}
