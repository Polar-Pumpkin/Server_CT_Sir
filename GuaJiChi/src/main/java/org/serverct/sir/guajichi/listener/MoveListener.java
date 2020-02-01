package org.serverct.sir.guajichi.listener;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.guajichi.GuaJiChi;
import org.serverct.sir.guajichi.config.ConfigManager;
import org.serverct.sir.guajichi.data.Area;
import org.serverct.sir.guajichi.hooks.VaultHook;
import org.serverct.sir.guajichi.utils.LocaleUtil;

import java.util.HashMap;
import java.util.Map;

public class MoveListener implements Listener {

    private Map<String, Player> coolDownPlayers = new HashMap<>();
    private Map<Player, Boolean> checkList = new HashMap<>();

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        Player user = event.getPlayer();
        if(!coolDownPlayers.containsKey(user.getName())) {
            Location loc = event.getTo();
            Area target = ConfigManager.getInstance().check(loc);
            if(target != null) {
                if(checkList.getOrDefault(user, false)) {
                    int money = target.getMoney();
                    int exp = target.getExp();

                    user.giveExp(exp);
                    VaultHook.getInstance().give(user, money);

                    user.sendMessage(GuaJiChi.getInstance().getLocale().getMessage(
                            "Chinese",
                            LocaleUtil.INFO,
                            "Reward",
                            "Success"
                    ).replace("%money%", String.valueOf(money)).replace("%exp%", String.valueOf(exp))
                    );
                } else {
                    checkList.put(user, true);
                }
            } else {
                if(checkList.getOrDefault(target, false)) {
                    checkList.put(user, false);
                }
            }

            coolDownPlayers.put(user.getName(), user);
            new BukkitRunnable() {
                @Override
                public void run() {
                    coolDownPlayers.remove(user.getName());
                }
            }.runTaskLater(GuaJiChi.getInstance(), ConfigManager.interval);
        }
    }
}
