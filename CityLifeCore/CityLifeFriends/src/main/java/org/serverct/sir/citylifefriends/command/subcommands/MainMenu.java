package org.serverct.sir.citylifefriends.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.enums.MessageType;
import org.serverct.sir.citylifecore.utils.InventoryUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;
import org.serverct.sir.citylifefriends.command.Subcommand;
import org.serverct.sir.citylifefriends.configuration.InventoryConfigManager;
import org.serverct.sir.citylifefriends.configuration.LocaleManager;
import org.serverct.sir.citylifefriends.configuration.PlayerDataManager;

public class MainMenu implements Subcommand {

    private Player user;
    private InventoryGui friendsGui;
    private InventoryUtil inventoryUtil = CityLifeFriends.getINSTANCE().getCoreApi().getInventoryUtil();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            user = (Player) sender;

            if(args.length == 1) {
                friendsGui = InventoryConfigManager.getInstance().appleFriendsToGui(PlayerDataManager.getInstance().getList(user.getName(), true));

                inventoryUtil.openInventory(user, friendsGui);
            } else {
                user.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
            }
        } else {
            sender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.ERROR, "Plugin", "NotPlayer"));
        }
        return true;
    }
}
