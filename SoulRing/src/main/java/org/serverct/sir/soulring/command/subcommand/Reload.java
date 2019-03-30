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
            "&a&l> &d&m------------------------------",
            "",
            "  &6&lSoul Ring &7| &d&l魂环 &7>>>",
            "",
            "  &7作者: &c&l&oEntityParrot_",
            "  &7版本: &c&l&o" + SoulRing.PLUGIN_VERSION,
            "",
            "  &a&l正在启动 &7>>>",
            "",
            "&a&l> &d&m------------------------------"
    };

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        for(String msg : enableMsg) {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
        }

        if(!configFile.exists()){
            SoulRing.getInstance().saveDefaultConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7未找到配置文件, 已自动生成."));
        } else {
            SoulRing.getInstance().reloadConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7已加载配置文件."));
        }

        VaultHook.getInstance().loadVault();

        AttributeManager.getInstance().loadAttributes();
        RingManager.getRingManager().loadRings();
        LocaleManager.getLocaleManager().loadLanguage();

        String enableEndSuffix = "&a&l> &d&m------------------------------";
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', enableEndSuffix));

        sender.sendMessage(LocaleManager.getLocaleManager().getMessage("INFO", "Plugins", "ReloadSuccess"));
        return true;
    }
}
