package org.serverct.sir.tianfu.task;

import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.config.PlayerDataManager;
import org.serverct.sir.tianfu.config.TalentManager;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.data.Talent;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.util.LocaleUtil;
import org.serverct.sir.tianfu.util.PlaceholderUtil;

public class RegenTalentTask extends BukkitRunnable {

    private LocaleUtil locale = Tianfu.getInstance().getLocale();

    private Player user;
    private PlayerData userData;
    private Talent regen = TalentManager.getInstance().getTalent(TalentType.REGEN);
    private int amount;
    private int level;

    public RegenTalentTask(Player player) {
        this.user = player;
        this.userData = PlayerDataManager.getInstance().getPlayerData(user.getName());
    }

    private double health;
    private double maxHealth;
    private double resultHealth;

    @Override
    public void run() {
        if(user.isOnline()) {
            if(!user.isDead()) {
                if(level > 0) {
                    if(level != userData.getLevel().get(TalentType.REGEN)) {
                        user.sendMessage(
                                PlaceholderUtil.check(
                                        locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Talent", "Regen.Update"),
                                        TalentType.REGEN,
                                        userData
                                )
                        );
                    }
                }
                level = userData.getLevel().get(TalentType.REGEN);

                if(userData.getLevel().get(TalentType.REGEN) > 0) {
                    health = user.getHealth();
                    maxHealth = user.getMaxHealth();
                    amount = (int) regen.getExecutor().execute(userData);
                    resultHealth = user.getHealth() + amount;

                    if(health < maxHealth) {
                        if(resultHealth < maxHealth) {
                            user.setHealth(resultHealth);
                        } else {
                            user.setHealth(maxHealth);
                        }
                    }
                }
            }
        } else {
            cancel();
        }
    }
}
