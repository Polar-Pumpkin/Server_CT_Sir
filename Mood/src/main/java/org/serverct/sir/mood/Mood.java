package org.serverct.sir.mood;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.mood.command.CommandHandler;
import org.serverct.sir.mood.configuration.Area;
import org.serverct.sir.mood.configuration.Config;
import org.serverct.sir.mood.configuration.Item;
import org.serverct.sir.mood.configuration.Language;
import org.serverct.sir.mood.configuration.PlayerData;
import org.serverct.sir.mood.hooks.PlaceholderAPIHook;
import org.serverct.sir.mood.hooks.VaultHook;
import org.serverct.sir.mood.listener.*;
import org.serverct.sir.mood.runnable.tasks.CheckTask;
import org.serverct.sir.mood.runnable.tasks.DataSaveTask;
import org.serverct.sir.mood.runnable.tasks.MoodUpdateTask;

public final class Mood extends JavaPlugin {

    private static Mood instance;
    public static final String PLUGIN_VERSION = "1.0-SNAPSHOT";
    public static boolean vaultHook;

    public static Mood getInstance() {
        return instance;
    }

    private String[] enableMsg = {
            "------------------------------",
            "",
            "  Mood | 心情 >>>",
            "",
            "  作者: EntityParrot_",
            "  版本: " + Mood.PLUGIN_VERSION,
            "",
            "  正在启动 >>>",
            "",
            "------------------------------"
    };

    private BukkitRunnable dataSaveTask;
    private BukkitRunnable playerCheckTask;
    private BukkitRunnable commonMoodDecreaseTask;
    private BukkitRunnable sunnyMoodDecreaseTask;
    private BukkitRunnable rainyMoodDecreaseTask;
    private BukkitRunnable snowyMoodDecreaseTask;

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;

        init();

        dataSaveTask = new DataSaveTask();
        dataSaveTask.runTaskTimer(this, 600, Config.getInstance().getData().getInt("DataSaveDelay") * 20 * 60);
        Bukkit.getLogger().info("  > 已启动定时保存任务.");

        playerCheckTask = new CheckTask();
        playerCheckTask.runTaskTimer(this, 600, 200);
        Bukkit.getLogger().info("  > 已启动定时玩家数据监测任务.");

        commonMoodDecreaseTask = new MoodUpdateTask(MoodUpdateTaskType.COMMON);
        sunnyMoodDecreaseTask = new MoodUpdateTask(MoodUpdateTaskType.SUNNY);
        rainyMoodDecreaseTask = new MoodUpdateTask(MoodUpdateTaskType.RAINY);
        snowyMoodDecreaseTask = new MoodUpdateTask(MoodUpdateTaskType.SNOWY);
        commonMoodDecreaseTask.runTaskTimer(this, 600, Config.getInstance().getMoodDecreasePeriod(MoodUpdateTaskType.COMMON));
        sunnyMoodDecreaseTask.runTaskTimer(this, 600, Config.getInstance().getMoodDecreasePeriod(MoodUpdateTaskType.SUNNY));
        rainyMoodDecreaseTask.runTaskTimer(this, 600, Config.getInstance().getMoodDecreasePeriod(MoodUpdateTaskType.RAINY));
        snowyMoodDecreaseTask.runTaskTimer(this, 600, Config.getInstance().getMoodDecreasePeriod(MoodUpdateTaskType.SNOWY));
        Bukkit.getLogger().info("  > 已启动定时心情自减任务.");

        Bukkit.getPluginManager().registerEvents(new PlayerJoinListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDamagedListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerDeathListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerInteractListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerMoveListener(), this);
        Bukkit.getLogger().info("  > 已注册监听器.");
        Bukkit.getPluginCommand("mood").setExecutor(new CommandHandler());
        Bukkit.getLogger().info("  > 已注册命令.");

        String enableEndSuffix = "------------------------------";
        Bukkit.getLogger().info(enableEndSuffix);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
        uninit();
    }

    public void init() {
        for(String msg : enableMsg) {
            Bukkit.getLogger().info(msg);
        }

        Language.getInstance().loadLanguage();
        Config.getInstance().loadConfig();
        PlayerData.getInstance().loadPlayerData();
        Item.getInstance().loadItem();
        Area.getInstance().loadArea();
        VaultHook.getInstance().loadVault();
        loadPlaceholderAPIExpansion();

        String enableEndSuffix = "------------------------------";
        Bukkit.getLogger().info(enableEndSuffix);
    }

    private void uninit() {
        dataSaveTask.cancel();
        playerCheckTask.cancel();
    }

    private void loadPlaceholderAPIExpansion() {
        if(Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null){
            new PlaceholderAPIHook().register();
            Bukkit.getLogger().info("  > 已连接 PlaceholderAPI.");
        } else {
            Bukkit.getLogger().info("  > 未找到 PlaceholderAPI, 变量模块不可用.");
        }
    }

    public void setVaultHook(boolean bool) {
        vaultHook = bool;
    }
}
