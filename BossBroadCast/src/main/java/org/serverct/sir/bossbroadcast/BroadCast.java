package org.serverct.sir.bossbroadcast;

import me.confuser.barapi.BarAPI;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.List;

public class BroadCast extends BukkitRunnable {

    private int index;

    public BroadCast(int index) {
        this.index = index;
    }

    private List<Player> players;
    private FileConfiguration config;
    private int stayTime;

    @Override
    public void run() {
        config = BossBroadCast.getInstance().getConfig();
        ConfigurationSection stayTimeSection = config.getConfigurationSection("BroadCast.StayTime");
        players = new ArrayList<>(BossBroadCast.getInstance().getServer().getOnlinePlayers());
        stayTime = stayTimeSection.getInt(String.valueOf(index)) * 20;

        BossBroadCast.getInstance().clear();

        for(Player player : players) {
            BarAPI.setMessage(player,
                    config.getString("BroadCast.Message." + index),
                    stayTime
            );
        }

        if(index + 1 <= stayTimeSection.getKeys(false).size()) {
            new BroadCast(index + 1).runTaskLater(BossBroadCast.getInstance(), stayTime);
        } else {
            new BroadCast(1).runTaskLater(BossBroadCast.getInstance(), stayTime);
        }
    }
}
