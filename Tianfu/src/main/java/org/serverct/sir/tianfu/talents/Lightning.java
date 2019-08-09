package org.serverct.sir.tianfu.talents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.serverct.sir.tianfu.config.TalentManager;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.enums.TalentType;

import java.util.Random;

public class Lightning implements TalentExecutor {

    private final TalentType talentKey = TalentType.LIGHTNING;
    private Player user;
    private int level;
    private int amount;

    private Random random = new Random();

    @Override
    public Object execute(PlayerData data) {
        user = Bukkit.getPlayer(data.getPlayerName());
        level = data.getLevel().get(talentKey);
        amount = TalentManager.getInstance().getTalent(talentKey).getLevels().get(level);
        return random.nextInt(100) <= amount;
    }
}
