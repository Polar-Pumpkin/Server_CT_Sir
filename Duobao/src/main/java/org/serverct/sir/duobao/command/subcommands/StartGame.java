package org.serverct.sir.duobao.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.duobao.Duobao;
import org.serverct.sir.duobao.command.Subcommand;
import org.serverct.sir.duobao.enums.MessageType;
import org.serverct.sir.duobao.manager.GameManager;
import org.serverct.sir.duobao.util.CommonUtil;
import org.serverct.sir.duobao.util.LocaleUtil;

public class StartGame implements Subcommand {

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        LocaleUtil locale = Duobao.getInstance().getLocale();
        if(sender.hasPermission("Duobao.start")) {
            if(args.length == 2) {
                GameManager.getInstance().startNewGame("gameone", Integer.valueOf(args[1]));
                CommonUtil.broadcast(
                        locale.getMessage(Duobao.getInstance().getLocaleKey(), MessageType.INFO, "Game", "Start")
                                .replace("%amount%", args[1])
                                .replace("%loc%", locale.getLocation(GameManager.getInstance().getGameMap().get("gameone").get(0)))
                );
            } else {
                sender.sendMessage(locale.getMessage(Duobao.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.Command"));
            }
        } else {
            sender.sendMessage(locale.getMessage(Duobao.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.Permission"));
        }
        return true;
    }
}
