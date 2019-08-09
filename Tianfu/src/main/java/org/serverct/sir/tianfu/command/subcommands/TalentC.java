package org.serverct.sir.tianfu.command.subcommands;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.command.Subcommand;
import org.serverct.sir.tianfu.config.TalentManager;
import org.serverct.sir.tianfu.data.Talent;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.util.LocaleUtil;

public class TalentC implements Subcommand {

    private Player user;
    private String text;

    private TalentType type;
    private Talent talent;
    private int level;
    private int value;

    private LocaleUtil locale;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        locale = Tianfu.getInstance().getLocale();

        if(sender instanceof Player) {
            user = (Player) sender;
        }

        if(sender.hasPermission("Tianfu.admin")) {
            if(args.length > 3) {
                if(TalentManager.getInstance().isTalentType(args[2])) {
                    type = TalentType.valueOf(args[2].toUpperCase());

                    switch (args[1]) {
                        case "add":
                            if(args.length == 4) {
                                value = Integer.valueOf(args[3]);

                                if(TalentManager.getInstance().addLevel(type, value)) {
                                    talent = TalentManager.getInstance().getTalent(type);
                                    text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Command", "Admin.Success.Add")
                                            .replace("%name%", talent.getDisplayName())
                                            .replace("%level%", String.valueOf(talent.getLevels().size() - 1))
                                            .replace("%value%", ChatColor.GRAY + talent.getDescription() + ": " + value)
                                    ;
                                    sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
                                } else {
                                    text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Admin.Failure.Add");
                                    sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
                                }
                            } else {
                                text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.Command");
                                sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
                            }
                        case "set":
                            if(args.length == 5) {
                                level = Integer.valueOf(args[3]);
                                value = Integer.valueOf(args[4]);

                                if(TalentManager.getInstance().setLevel(type, level, value)) {
                                    talent = TalentManager.getInstance().getTalent(type);
                                    text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Command", "Admin.Success.Add")
                                            .replace("%name%", talent.getDisplayName())
                                            .replace("%level%", String.valueOf(level))
                                            .replace("%value%", String.valueOf(value))
                                    ;
                                    sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
                                } else {
                                    text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Admin.Failure.Set");
                                    sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
                                }
                            } else {
                                text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.Command");
                                sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
                            }
                        /*case "info":
                            if(args.length == 3) {

                            } else {
                                text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.Command");
                                sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
                            }*/
                        default:
                            break;
                    }
                } else {
                    text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.TalentType");
                    sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
                }
            } else {
                text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Unknown.Command");
                sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
            }
        } else {
            text = locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "NoPermission");
            sender.sendMessage(user != null ? text : ChatColor.stripColor(text));
        }
        return true;
    }
}
