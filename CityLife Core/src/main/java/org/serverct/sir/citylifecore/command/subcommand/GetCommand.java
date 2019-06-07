package org.serverct.sir.citylifecore.command.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifecore.command.Subcommand;
import org.serverct.sir.citylifecore.configuration.ConfigData;

public class GetCommand implements Subcommand {

    private Player user;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            user = (Player) sender;

            if(args.length > 1) {
                switch (args[1]) {
                    case "selector":
                        user.getInventory().addItem(ConfigData.getInstance().getSelector());
                        break;
                    case "item":
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }
}
