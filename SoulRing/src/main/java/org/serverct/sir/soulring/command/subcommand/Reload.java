package org.serverct.sir.soulring.command.subcommand;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.serverct.sir.soulring.Attributes;
import org.serverct.sir.soulring.SoulRing;
import org.serverct.sir.soulring.command.SubCommand;
import org.serverct.sir.soulring.configuration.AttributeManager;
import org.serverct.sir.soulring.configuration.LocaleManager;
import org.serverct.sir.soulring.configuration.RingManager;
import org.serverct.sir.soulring.hook.VaultHook;

import java.io.File;

public class Reload implements SubCommand {

    private File configFile = new File(SoulRing.getInstance().getDataFolder() + File.separator + "config.yml");

    private String[] enableMsg = {
            "> ------------------------------",
            "",
            "  Soul Ring | 魂环 &7>>>",
            "",
            "  作者: &c&l&oEntityParrot_",
            "  版本: &c&l&o" + SoulRing.PLUGIN_VERSION,
            "",
            "  正在启动 >>>",
            "",
            "> ------------------------------"
    };

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender.hasPermission("SoulRing.Reload")) {
            for(String msg : enableMsg) {
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
            }

            if(!configFile.exists()){
                SoulRing.getInstance().saveDefaultConfig();
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 未找到配置文件, 已自动生成."));
            } else {
                SoulRing.getInstance().reloadConfig();
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 已加载配置文件."));
            }

            VaultHook.getInstance().loadVault();

            AttributeManager.getInstance().loadAttributes();
            RingManager.getRingManager().loadRings();
            LocaleManager.getLocaleManager().loadLanguage();

            String enableEndSuffix = "> ------------------------------";
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', enableEndSuffix));

            sender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Plugins", "ReloadSuccess"));
        } else {
            sender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Plugins", "NoPermission"));
        }
        return true;
    }
}
