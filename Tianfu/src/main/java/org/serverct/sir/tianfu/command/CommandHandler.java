package org.serverct.sir.tianfu.command;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.command.subcommands.*;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.util.LocaleUtil;

import java.util.HashMap;
import java.util.Map;

public class CommandHandler implements CommandExecutor {

    private Map<String, Subcommand> subcommandMap = new HashMap<>();
    private LocaleUtil locale = Tianfu.getInstance().getLocale();

    public CommandHandler() {
        registerSubcommand("reload", new Reload());
        registerSubcommand("stats", new Stats());
        registerSubcommand("gui", new Gui());
        registerSubcommand("help", new Help());
        registerSubcommand("talent", new TalentC());
    }

    public void registerSubcommand(String cmd, Subcommand executor) {
        if(!subcommandMap.containsKey(cmd)) {
            subcommandMap.put(cmd, executor);
        } else {
            locale.buildMessage(locale.getDefaultLocaleKey(), MessageType.ERROR, "&7检测到重复子命令注册: &c" + cmd + "&7.");
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length >= 1) {
            if(subcommandMap.containsKey(args[0])) {
                return subcommandMap.get(args[0]).execute(sender, args);
            }
        } else {
            return subcommandMap.get("gui").execute(sender, args);
        }
        return false;
    }
}
