package org.serverct.sir.soulring;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.soulring.command.CommandHandler;
import org.serverct.sir.soulring.configuration.AttributeManager;
import org.serverct.sir.soulring.configuration.LocaleManager;
import org.serverct.sir.soulring.configuration.RingManager;
import org.serverct.sir.soulring.hook.VaultHook;
import org.serverct.sir.soulring.listener.OnPlayerAttack;

import java.io.File;

public final class SoulRing extends JavaPlugin {

    private static SoulRing INSTANCE;
    public final static String PLUGIN_VERSION = "1.0-SNAPSHOT";

    public static SoulRing getInstance() {
        return INSTANCE;
    }

    private File configFile = new File(getDataFolder() + File.separator + "config.yml");

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
    public void onEnable() {
        // Plugin startup logic
        INSTANCE = this;

        for(String msg : enableMsg) {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', msg));
        }

        if(!configFile.exists()){
            saveDefaultConfig();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7未找到配置文件, 已自动生成."));
        } else {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7已加载配置文件."));
        }

        VaultHook.getInstance().loadVault();

        AttributeManager.getInstance().loadAttributes();
        RingManager.getRingManager().loadRings();
        LocaleManager.getLocaleManager().loadLanguage();

        getServer().getPluginCommand("soulring").setExecutor(new CommandHandler());
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7成功注册命令."));

        getServer().getPluginManager().registerEvents(new OnPlayerAttack(), this);
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7成功注册监听器."));

        String enableEndSuffix = "&a&l> &d&m------------------------------";
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', enableEndSuffix));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
