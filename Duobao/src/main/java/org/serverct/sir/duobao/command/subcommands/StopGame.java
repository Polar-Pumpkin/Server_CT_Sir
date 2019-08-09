package org.serverct.sir.duobao.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.duobao.Duobao;
import org.serverct.sir.duobao.command.Subcommand;
import org.serverct.sir.duobao.enums.MessageType;
import org.serverct.sir.duobao.manager.GameManager;
import org.serverct.sir.duobao.util.CommonUtil;
import org.serverct.sir.duobao.util.LocaleUtil;

public class StopGame implements Subcommand {

    private LocaleUtil locale;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        locale = Duobao.getInstance().getLocale();
        if(sender.hasPermission("Duobao.stop")) {
            if(args.length == 2) {
                GameManager.getInstance().stopGame(args[1]);
                CommonUtil.broadcast(locale.getMessage(Duobao.getInstance().getLocaleKey(), MessageType.INFO, "Game", "Stop"));
            } else {
                sender.sendMessage(locale.getMessage(Duobao.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.Command"));
            }
        } else {
            sender.sendMessage(locale.getMessage(Duobao.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.Permission"));
        }
        return true;
    }
}
