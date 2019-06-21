package org.serverct.sir.citylifemood.command.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.citylifemood.command.Subcommand;
import org.serverct.sir.citylifemood.configuration.ItemManager;
import org.serverct.sir.citylifemood.configuration.LocaleManager;
import org.serverct.sir.citylifemood.data.Consumable;
import org.serverct.sir.citylifemood.enums.ConsumableType;
import org.serverct.sir.citylifemood.enums.MessageType;

public class Item implements Subcommand {

    private Player playerSender;

    private Player targetPlayer;
    private Consumable targetConsumable;
    private ItemStack targetItem;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof  Player) {
            playerSender = (Player) sender;

            if(playerSender.hasPermission("CityLifeMood.item.admin")) {
                switch(args[1]) {
                    case "give":
                        if(args.length == 5) {
                            targetPlayer = Bukkit.getPlayer(args[2]);
                            if(targetPlayer.isOnline()) {
                                if(ItemManager.getInstance().getItemMap().containsKey(args[3])) {
                                    targetItem = ItemManager.getInstance().getItemMap().get(args[3]).getItem();
                                    if(Integer.valueOf(args[4]) <= targetItem.getMaxStackSize()) {
                                        targetItem.setAmount(Integer.valueOf(args[4]));
                                        targetPlayer.getInventory().addItem(targetItem);
                                        playerSender.sendMessage(
                                                LocaleManager.getInstance().getMessage(MessageType.INFO, "Commands", "Item.GivenSuccess")
                                                        .replace("%player%", targetPlayer.getName())
                                                        .replace("%amount%", args[4])
                                                        .replace("%item%", targetItem.getItemMeta().getDisplayName())
                                        );
                                    } else {
                                        playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Item.OutOfMaxStackSize"));
                                    }
                                } else {
                                    playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Item"));
                                }
                            } else {
                                playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Player"));
                            }
                        } else {
                            playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
                        }
                        break;
                    case "get":
                        if(args.length == 4) {
                            if(ItemManager.getInstance().getItemMap().containsKey(args[2])) {
                                targetItem = ItemManager.getInstance().getItemMap().get(args[2]).getItem();
                                if(Integer.valueOf(args[3]) <= targetItem.getMaxStackSize()) {
                                    targetItem.setAmount(Integer.valueOf(args[3]));
                                    playerSender.getInventory().addItem(targetItem);
                                    playerSender.sendMessage(
                                            LocaleManager.getInstance().getMessage(MessageType.INFO, "Commands", "Item.GivenSuccess")
                                                    .replace("%player%", playerSender.getName())
                                                    .replace("%amount%", args[3])
                                                    .replace("%item%", targetItem.getItemMeta().getDisplayName())
                                    );
                                } else {
                                    playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Item.OutOfMaxStackSize"));
                                }
                            } else {
                                playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Item"));
                            }
                        } else {
                            playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
                        }
                        break;
                    case "save":
                        if(args.length == 5) {
                            targetItem = playerSender.getItemInHand();
                            if(targetItem != null && targetItem.getType() != Material.AIR) {
                                ItemManager.getInstance().saveItem(args[2], targetItem, ConsumableType.valueOf(args[3].toUpperCase()), Integer.valueOf(args[4]));
                            } else {
                                playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Item.NothingInHand"));
                            }
                        } else {
                            playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
                        }
                        break;
                    default:
                        break;
                }
            }
        } else {
            sender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Plugin", "NotPlayer"));
        }
        return true;
    }
}
