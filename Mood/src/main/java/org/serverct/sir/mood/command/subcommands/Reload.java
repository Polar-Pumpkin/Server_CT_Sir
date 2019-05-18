package org.serverct.sir.mood.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.mood.MessageType;
import org.serverct.sir.mood.Mood;
import org.serverct.sir.mood.command.Subcommand;
import org.serverct.sir.mood.configuration.Language;

public class Reload implements Subcommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("Mood.reload")) {
            Mood.getInstance().uninit();
            Mood.getInstance().init();
            sender.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Plugin", "ReloadSuccess"));
        } else {
            sender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Plugin", "NoPermission"));
        }
        return true;
    }
}
