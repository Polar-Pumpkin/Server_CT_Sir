package org.serverct.sir.bossbroadcast;

import lombok.Getter;
import me.confuser.barapi.BarAPI;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;

public final class BossBroadCast extends JavaPlugin implements CommandExecutor {

    @Getter private static BossBroadCast instance;
    private BukkitRunnable task;

    private File configFile = new File(getDataFolder() + File.separator + "config.yml");

    @Override
    public void onEnable() {
        // Plugin startup logic
        instance = this;
        init();
        task = new BroadCast(1);
        task.runTaskLater(this, 3 * 60 * 20);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public void init() {
        if(!configFile.exists()) {
            saveDefaultConfig();
        }
        reloadConfig();
        clear();
    }

    public void clear() {
        for(Player player : getServer().getOnlinePlayers()) {
            BarAPI.removeBar(player);
        }
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(args.length > 0) {
            switch (args[0]) {
                case "reload":
                    init();
                    task.cancel();
                    task = new BroadCast(1);
                    task.runTaskLater(this, 1 * 20);
                default:
                    break;
            }
        } else {
            return true;
        }
        return false;
    }
}
