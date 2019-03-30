package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.command.SubCommand;

public class Reload implements SubCommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        ANOHANAMarry.getINSTANCE().reloadPlugin();

        return true;
    }
}
