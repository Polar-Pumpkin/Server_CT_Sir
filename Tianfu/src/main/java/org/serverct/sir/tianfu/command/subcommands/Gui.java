package org.serverct.sir.tianfu.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.command.Subcommand;
import org.serverct.sir.tianfu.config.GuiManager;
import org.serverct.sir.tianfu.config.PlayerDataManager;
import org.serverct.sir.tianfu.util.CommonUtil;
import org.serverct.sir.tianfu.util.LocaleUtil;

public class Gui implements Subcommand {

    private Player user;

    private LocaleUtil locale = Tianfu.getInstance().getLocale();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            user = (Player) sender;
            CommonUtil.openInventory(user, GuiManager.getInstance().init(PlayerDataManager.getInstance().getPlayerData(user.getName())), GuiManager.getInstance().getSound(true));
        }
        return true;
    }
}
