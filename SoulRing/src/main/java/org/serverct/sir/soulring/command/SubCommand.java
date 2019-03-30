package org.serverct.sir.soulring.command;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    abstract boolean execute(CommandSender sender, String[] args);
}
