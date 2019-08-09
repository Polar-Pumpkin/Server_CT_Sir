package org.serverct.sir.tianfu.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.config.PlayerDataManager;
import org.serverct.sir.tianfu.config.TalentManager;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.data.Talent;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.task.RegenTalentTask;
import org.serverct.sir.tianfu.util.LocaleUtil;
import org.serverct.sir.tianfu.util.PlaceholderUtil;

public class PlayerJoinListener implements Listener {

    private Player user;
    private PlayerData userData;
    private int amount;

    private Talent regen = TalentManager.getInstance().getTalent(TalentType.REGEN);
    private LocaleUtil locale = Tianfu.getInstance().getLocale();

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        user = event.getPlayer();
        userData = PlayerDataManager.getInstance().getPlayerData(user.getName());
        amount = (int) regen.getExecutor().execute(userData);

        if(userData.getLevel().get(TalentType.REGEN) >= 1) {
            new RegenTalentTask(user).runTaskTimer(Tianfu.getInstance(), 1, 20);
            user.sendMessage(
                    PlaceholderUtil.check(
                            locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Talent", "Regen.Welcome"),
                            TalentType.REGEN,
                            userData
                    )
            );
        }
    }

}
