package org.serverct.sir.citylifemood.command.subcommands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifecore.data.Area;
import org.serverct.sir.citylifecore.manager.SelectionManager;
import org.serverct.sir.citylifecore.utils.CommonUtil;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.command.Subcommand;
import org.serverct.sir.citylifemood.configuration.AreaManager;
import org.serverct.sir.citylifemood.configuration.LocaleManager;
import org.serverct.sir.citylifemood.data.MoodArea;
import org.serverct.sir.citylifemood.enums.MessageType;
import org.serverct.sir.citylifemood.enums.MoodAreaType;

public class Select implements Subcommand {

    private Player playerSender;
    private SelectionManager selectionManager;

    private Area selection;
    private String id;
    private int step;
    private int period;
    private String reason;

    private MoodArea moodArea;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof  Player) {
            playerSender = (Player) sender;
            selectionManager = CityLifeMood.getInstance().getCoreApi().getSelectionAPI();

            switch (args[1]) {
                case "set":
                    if(args.length == 3) {
                        if(args[2].equalsIgnoreCase("point1")
                                || args[2].equalsIgnoreCase("p1")
                                || args[2].equalsIgnoreCase("1")
                                || args[2].equalsIgnoreCase("location1")
                                || args[2].equalsIgnoreCase("loc1")
                                || args[2].equalsIgnoreCase("l1")) {
                            selectionManager.setPoint1(playerSender, playerSender.getLocation());
                        } else if(args[2].equalsIgnoreCase("point2")
                                || args[2].equalsIgnoreCase("p2")
                                || args[2].equalsIgnoreCase("2")
                                || args[2].equalsIgnoreCase("location2")
                                || args[2].equalsIgnoreCase("loc2")
                                || args[2].equalsIgnoreCase("l2")) {
                            selectionManager.setPoint2(playerSender, playerSender.getLocation());
                        }
                    } else {
                        playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
                    }
                    break;
                case "clear":
                case "sel":
                case "clr":
                    if(args.length == 2) {
                        selectionManager.clearSelection(playerSender);
                    } else {
                        playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Param"));
                    }
                    break;
                case "create":
                case "new":
                    // /clmood select create id step period reason
                    selection = selectionManager.createSelection(playerSender);
                    if(selection != null) {
                        if(args.length >= 6) {
                            id = args[2];
                            if(CommonUtil.isInteger(args[3]) && CommonUtil.isInteger(args[4])) {
                                step = Integer.valueOf(args[3]);
                                period = Integer.valueOf(args[4]);
                            } else {
                                playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Unknown.Number"));
                                return true;
                            }
                            reason = args[5];
                            if(args.length > 6) {
                                for(int index = 6; index <= args.length; index++) {
                                    reason = reason + " " + args[index];
                                }
                            }

                            moodArea = new MoodArea(id, MoodAreaType.CORE, selection, step, period, reason);
                            AreaManager.getInstance().saveMoodArea(moodArea);
                            playerSender.sendMessage(
                                    LocaleManager.getInstance().getMessage(MessageType.INFO, "Commands", "Area.Created")
                                            .replace("%id%", args[2])
                                            .replace("%step%", args[3])
                                            .replace("%period%", args[4])
                            );
                        }
                    }
                    break;
                case "gettool":
                    if(args.length == 2) {
                        playerSender.getInventory().addItem(selectionManager.getSelector());
                    }
                    break;
                case "delete":
                case "remove":
                case "del":
                case "rem":
                    if(args.length == 3) {
                        if(AreaManager.getInstance().getAreasMap().containsKey(args[2])) {
                            AreaManager.getInstance().deleteMoodArea(args[2]);
                            playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.INFO, "Commands", "Area.Deleted").replace("%id%", args[2]));
                        } else {
                            playerSender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Commands", "Area.NotExist"));
                        }
                    }
                    break;
                default:
                    break;
            }
        } else {
            sender.sendMessage(LocaleManager.getInstance().getMessage(MessageType.WARN, "Plugin", "NotPlayer"));
        }
        return true;
    }
}
