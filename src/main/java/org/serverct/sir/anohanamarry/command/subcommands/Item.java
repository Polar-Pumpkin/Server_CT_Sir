package org.serverct.sir.anohanamarry.command.subcommands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.anohanamarry.command.SubCommand;
import org.serverct.sir.anohanamarry.configuration.ItemData;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Item implements SubCommand {

    private Player playerSender;
    private Player targetPlayer;
    private ItemStack targetItem;
    private int targetAmount;

    private String regEx = "\\d{1,4}";
    private Pattern pattern = Pattern.compile(regEx);
    private Matcher matcher;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            playerSender = (Player) sender;

            if(args.length == 4) {
                switch (args[1]) {
                    case "get":
                        ItemData.getInstance().giveItem(args[2], playerSender, args[3]);
                        break;
                    case "savegift":
                        targetItem = playerSender.getInventory().getItemInMainHand();
                        if(targetItem != null || targetItem.getType() != Material.AIR) {
                            matcher = pattern.matcher(args[3]);

                            if(matcher.find()) {
                                targetAmount = Integer.valueOf(args[3]);

                                ItemData.getInstance().saveGift(targetItem, args[2], targetAmount);
                                playerSender.sendMessage(
                                        Language.getInstance().getMessage(MessageType.INFO, "Commands.Items.Success.Save")
                                                .replace("%item%", targetItem.getItemMeta().getDisplayName())
                                                .replace("%amount%", String.valueOf(targetAmount))
                                                .replace("%giftKey%", args[2])
                                );
                            } else {
                                playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Items.NotANumber").replace("%param%", "亲密点数"));
                            }
                        } else {
                            playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Items.NullItemInHand"));
                        }
                        break;
                    default:
                        playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Param"));
                        break;
                }
            } else if(args.length == 3) {
                switch (args[1]) {
                    case "removegift":
                        if(ItemData.getInstance().getLoadedGiftMap().containsKey(args[2])) {
                            ItemData.getInstance().removeGift(args[2]);
                        } else {
                            playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Item"));
                        }
                        break;
                    default:
                        playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Param"));
                        break;
                }
            } else if(args.length == 5) {
                switch (args[1]) {
                    case "give":
                        targetPlayer = Bukkit.getPlayer(args[2]);
                        if(targetPlayer.isOnline()) {
                            ItemData.getInstance().giveItem(args[3], targetPlayer, args[4]);
                        } else {
                            playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Player"));
                        }
                        break;
                    default:
                        playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Param"));
                        break;
                }
            } else {
                playerSender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Param"));
            }
        } else {
            sender.sendMessage(Language.getInstance().getMessage(MessageType.ERROR, "Plugins.NotPlayer"));
        }
        return true;
    }
}
