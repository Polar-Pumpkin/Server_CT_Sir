package org.serverct.sir.tianfu.hooks;

import org.black_ixx.playerpoints.PlayerPoints;
import org.black_ixx.playerpoints.PlayerPointsAPI;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.serverct.sir.tianfu.Tianfu;

public class PlayerPointsHook {

    private static PlayerPointsHook pointsHook;
    public static PlayerPointsHook getInstance() {
        if(pointsHook == null) {
            pointsHook = new PlayerPointsHook();
        }
        return pointsHook;
    }

    private PlayerPointsAPI playerPointsAPI;

    public void loadPlayerPoints() {
        if (!setupPoints()) {
            Bukkit.getLogger().info("  > 未找到 PlayerPoints, 扣费模块不可用.");
        } else {
            Bukkit.getLogger().info("  > 已连接 PlayerPoints.");
        }
    }

    private boolean setupPoints() {
        if(Tianfu.getInstance().getServer().getPluginManager().getPlugin("PlayerPoints") != null) {
            if(Bukkit.getPluginManager().isPluginEnabled("PlayerPoints")) {
                playerPointsAPI = ((PlayerPoints) Bukkit.getPluginManager().getPlugin("PlayerPoints")).getAPI();
                return true;
            }
        }
        return false;
    }

    public int getBalances(Player player) {
        if (!Bukkit.getPluginManager().getPlugin("PlayerPoints").isEnabled()) {
            return 0;
        }
        return playerPointsAPI.look(player.getUniqueId());
    }

    public boolean give(Player player, int amount)
    {
        if (Bukkit.getPluginManager().getPlugin("PlayerPoints").isEnabled()) {
            return playerPointsAPI.give(player.getUniqueId(), amount);
        }
        return false;
    }

    public boolean take(Player player, int amount)
    {
        if (Bukkit.getPluginManager().getPlugin("PlayerPoints").isEnabled()) {
            return playerPointsAPI.take(player.getUniqueId(), amount);
        }
        return false;
    }

}
