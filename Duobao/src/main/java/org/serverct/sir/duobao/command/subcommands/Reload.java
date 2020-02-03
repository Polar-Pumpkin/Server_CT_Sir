package org.serverct.sir.duobao.command.subcommands;

import org.bukkit.command.CommandSender;
import org.serverct.sir.duobao.Duobao;
import org.serverct.sir.duobao.command.Subcommand;
import org.serverct.sir.duobao.enums.MessageType;
import org.serverct.sir.duobao.manager.GameManager;

public class Reload implements Subcommand {
    @Override
    public boolean execute(CommandSender sender, String[] args) {
        for(String id : GameManager.getInstance().getGameMap().keySet()) {
            GameManager.getInstance().stopGame(id);
        }
        Duobao.getInstance().reloadConfig();
        Duobao.getInstance().init();
        sender.sendMessage(Duobao.getInstance().getLocale().buildMessage(Duobao.getInstance().getLocaleKey(), MessageType.INFO, "&7所有进行中游戏已停止, 配置文件重载完成."));
        return true;
    }
}
