package org.serverct.sir.soulring;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Effect;
import org.bukkit.Sound;
import org.bukkit.plugin.java.JavaPlugin;
import org.serverct.sir.soulring.command.CommandHandler;
import org.serverct.sir.soulring.configuration.AttributeManager;
import org.serverct.sir.soulring.configuration.LocaleManager;
import org.serverct.sir.soulring.configuration.RingManager;
import org.serverct.sir.soulring.hook.VaultHook;
import org.serverct.sir.soulring.listener.OnPlayerAttack;
import org.serverct.sir.soulring.listener.OnPlayerHeld;

import java.io.File;

public final class SoulRing extends JavaPlugin {

    private static SoulRing INSTANCE;
    public final static String PLUGIN_VERSION = "1.2-RELEASE";

    public static SoulRing getInstance() {
        return INSTANCE;
    }

    private File configFile = new File(getDataFolder() + File.separator + "config.yml");

    private String[] enableMsg = {
            "> ------------------------------",
            "",
            "  Soul Ring | 魂环 >>>",
            "",
            "  作者: EntityParrot_",
            "  版本: " + SoulRing.PLUGIN_VERSION,
            "",
            "  正在启动 >>>",
            "",
            "> ------------------------------"
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
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 未找到配置文件, 已自动生成."));
        } else {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 已加载配置文件."));
        }

        VaultHook.getInstance().loadVault();

        AttributeManager.getInstance().loadAttributes();
        RingManager.getRingManager().loadRings();
        LocaleManager.getLocaleManager().loadLanguage();

        getServer().getPluginCommand("soulring").setExecutor(new CommandHandler());
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 成功注册命令."));

        getServer().getPluginManager().registerEvents(new OnPlayerAttack(), this);
        getServer().getPluginManager().registerEvents(new OnPlayerHeld(), this);
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 成功注册监听器."));

        String enableEndSuffix = "> ------------------------------";
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', enableEndSuffix));
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public boolean hasSoundEnabled() {
        return getConfig().getBoolean("Sound.Enable");
    }

    public Sound getSound(boolean isSuccess) {
        if(isSuccess) {
            return Sound.valueOf(getConfig().getString("Sound.Success").toUpperCase());
        } else {
            return Sound.valueOf(getConfig().getString("Sound.Failure").toUpperCase());
        }
    }

    public boolean hasEffectEnabled() {
        return getConfig().getBoolean("Effect.Enable");
    }

    public Effect getEffect(boolean isSuccess) {
        if(isSuccess) {
            return Effect.valueOf(getConfig().getString("Effect.Success").toUpperCase());
        } else {
            return Effect.valueOf(getConfig().getString("Effect.Failure").toUpperCase());
        }
    }
}
