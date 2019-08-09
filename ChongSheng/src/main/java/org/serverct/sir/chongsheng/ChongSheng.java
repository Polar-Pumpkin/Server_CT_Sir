package org.serverct.sir.chongsheng;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public final class ChongSheng extends JavaPlugin implements CommandExecutor, Listener {

    private Location respawn;
    private Location first;
    private FileConfiguration config;

    private Map<String, BukkitRunnable> stringBukkitRunnableMap = new HashMap<>();
    private File players = new File(getDataFolder() + File.separator + "Players.yml");
    private FileConfiguration playerData = YamlConfiguration.loadConfiguration(players);

    @Override
    public void onEnable() {
        // Plugin startup logic
        if(!new File(getDataFolder() + File.separator + "config.yml").exists()) {
            saveDefaultConfig();
        }
        if(!players.exists()) {
            try {
                players.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        playerData = YamlConfiguration.loadConfiguration(players);
        config = getConfig();

        loadLocation();
        if(config.isConfigurationSection("Heal")) {
            ConfigurationSection heal = config.getConfigurationSection("Heal");
            Set<String> settings = heal.getKeys(false);
            if(!settings.isEmpty()) {
                for(String worldName : settings) {
                    ConfigurationSection targetWorld = heal.getConfigurationSection(worldName);
                    startRunnable(worldName, targetWorld.getInt("Amount"), targetWorld.getInt("Period"));
                }
            }
        }

        Bukkit.getPluginCommand("chongsheng").setExecutor(this);
        Bukkit.getPluginCommand("huixue").setExecutor(this);
        Bukkit.getPluginManager().registerEvents(this, this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender instanceof Player) {
            Player user = (Player) sender;
            if(command.getName().equalsIgnoreCase("chongsheng")) {
                if(args.length == 1) {
                    Location target = user.getLocation();
                    if(args[0].equalsIgnoreCase("set")) {
                        config.set("Respawn.World", target.getWorld().getName());
                        config.set("Respawn.X", target.getX());
                        config.set("Respawn.Y", target.getY());
                        config.set("Respawn.Z", target.getZ());
                        saveConfig();
                        respawn = target;
                        user.sendMessage("设置成功.");
                    } else if(args[0].equalsIgnoreCase("first")) {
                        config.set("First.World", target.getWorld().getName());
                        config.set("First.X", target.getX());
                        config.set("First.Y", target.getY());
                        config.set("First.Z", target.getZ());
                        saveConfig();
                        first = target;
                        user.sendMessage("设置成功.");
                    }
                }
            } else if(command.getName().equalsIgnoreCase("huixue")) {
                if(args.length == 4) {
                    // /huixue set worldName amount period
                    String worldName = args[1];
                    int amount = Integer.valueOf(args[2]);
                    int period = Integer.valueOf(args[3]);
                    startRunnable(worldName, amount, period);
                    config.set("Heal." + worldName + ".Amount", amount);
                    config.set("Heal." + worldName + ".Period", period);
                    saveConfig();
                    user.sendMessage("设置成功.");
                }
            }
        }
        return true;
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onJoin(PlayerJoinEvent event) {
        loadLocation();
        Player joiner = event.getPlayer();
        Bukkit.getLogger().info("玩家加入事件触发: " + joiner.getName());
        List<String> loggedPlayers = playerData.getStringList("Log");

        if(loggedPlayers.contains(joiner.getName())) {
            Bukkit.getLogger().info("玩家先前登陆过服务器, 被记录在案.");
        } else {
            Bukkit.getLogger().info("玩家未在记录中, 视为首次登陆.");
            if(first != null) {
                joiner.teleport(first);
                Bukkit.getLogger().info("坐标记录已加载, 已传送.");
                loggedPlayers.add(joiner.getName());
                playerData.set("Log", loggedPlayers);
                try {
                    playerData.save(players);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Bukkit.getLogger().info("已将玩家记录.");
            }
        }
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onRespawn(PlayerRespawnEvent event) {
        loadLocation();
        Player respawner = event.getPlayer();
        Bukkit.getLogger().info("玩家重生事件触发: " + respawner.getName());
        if(respawn != null) {
            respawner.teleport(respawn);
            Bukkit.getLogger().info("坐标记录已加载, 已传送.");
        }
    }

    private void startRunnable(String worldName, int amount, int period) {
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for(Player player : Bukkit.getWorld(worldName).getPlayers()) {
                    double resultHealth = player.getHealth() + amount;
                    if(resultHealth >= player.getMaxHealth()) {
                        player.setHealth(player.getMaxHealth());
                    } else {
                        player.setHealth(resultHealth);
                    }
                }
            }
        };
        stringBukkitRunnableMap.put(worldName, task);
        task.runTaskTimer(this, 1, period * 20);
    }

    public void loadLocation() {
        Bukkit.getLogger().info("开始加载坐标信息");
        reloadConfig();
        Bukkit.getLogger().info("重载配置文件.");
        FileConfiguration data = getConfig();
        if(config.isConfigurationSection("Respawn")) {
            Bukkit.getLogger().info("侦测到重生点记录信息.");
            String respawnWorld = config.getString("Respawn.World");
            double respawnX = config.getDouble("Respawn.X");
            double respawnY = config.getDouble("Respawn.Y");
            double respawnZ = config.getDouble("Respawn.Z");
            respawn = new Location(Bukkit.getWorld(respawnWorld), respawnX, respawnY, respawnZ);
            Bukkit.getLogger().info("已加载重生点记录: " + respawnX + ", " + respawnY + ", " + respawnZ + " (" + respawnWorld + ")");
        }
        if(config.isConfigurationSection("First")) {
            String firstWorld = config.getString("Respawn.World");
            double firstX = config.getDouble("Respawn.X");
            double firstY = config.getDouble("Respawn.Y");
            double firstZ = config.getDouble("Respawn.Z");
            first = new Location(Bukkit.getWorld(firstWorld), firstX, firstY, firstZ);
            Bukkit.getLogger().info("已加载重生点记录: " + firstX + ", " + firstY + ", " + firstZ + " (" + firstWorld + ")");
        }
    }
}
