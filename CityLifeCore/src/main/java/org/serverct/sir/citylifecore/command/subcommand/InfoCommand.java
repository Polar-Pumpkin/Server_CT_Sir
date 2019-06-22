package org.serverct.sir.citylifecore.command.subcommand;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.command.Subcommand;
import org.serverct.sir.citylifecore.data.Area;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.manager.AreaManager;
import org.serverct.sir.citylifecore.manager.InventoryManager;
import org.serverct.sir.citylifecore.utils.LocaleUtil;

import java.util.Map;

public class InfoCommand implements Subcommand {

    private LocaleUtil locale = CityLifeCore.getInstance().getLocale();
    private InventoryManager inventoryManager = CityLifeCore.getAPI().getInventoryAPI();
    private AreaManager areaManager = CityLifeCore.getAPI().getAreaAPI();

    private Player user;
    private Map<String, InventoryGui> loadedInventory;
    private Map<String, Area> loadedArea;
    private InventoryGui targetGui;
    private InventoryItem targetItem;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            user = (Player) sender;
            loadedInventory = inventoryManager.getLoadedInventory();
            loadedArea = areaManager.getLoadedArea();

            if(args.length > 2) {
                switch (args[1]) {
                    case "gui":
                        if(args.length == 3) {
                            if(loadedInventory.containsKey(args[2])) {
                                targetGui = loadedInventory.get(args[2]);

                                for(String info : targetGui.getInfo()) {
                                    user.sendMessage(info);
                                }
                            }
                        }
                        break;
                    case "Area":
                        if(args.length == 3) {
                            if(loadedArea.containsKey(args[2])) {
                                for(String info : loadedArea.get(args[2]).getInfo()) {
                                    user.sendMessage(info);
                                }
                            }
                        }
                        break;
                    case "inventoryitem":
                        // /clc info inventoryitem InventoryID list/item InventoryItemID
                        if(args.length == 4 || args.length == 5) {
                           if(loadedInventory.containsKey(args[2])) {
                               targetGui = loadedInventory.get(args[2]);

                               switch (args[3]) {
                                   case "list":
                                       if(args.length == 4) {
                                           for(String info : targetGui.getItemsInfo()) {
                                               user.sendMessage(info);
                                           }
                                       }
                                       break;
                                   case "item":
                                       if(args.length == 5) {
                                           if(targetGui.containItem(args[4])) {
                                               targetItem = targetGui.getItem(args[4]);

                                               if(targetItem != null) {
                                                   for(String info : targetItem.getInfo(true)) {
                                                       user.sendMessage(info);
                                                   }
                                               }
                                           }
                                       }
                                       break;
                                   default:
                                       break;
                               }
                           }
                        }
                        break;
                    default:
                        break;
                }
            }
        }
        return true;
    }
}
