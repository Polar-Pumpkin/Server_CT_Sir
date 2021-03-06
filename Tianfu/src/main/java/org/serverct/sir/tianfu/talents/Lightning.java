package org.serverct.sir.tianfu.talents;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.serverct.sir.tianfu.config.TalentManager;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.enums.TalentType;

import java.util.Random;

public class Lightning implements TalentExecutor {

    private final TalentType talentKey = TalentType.LIGHTNING;

    private Random random = new Random();

    @Override
    public Object execute(PlayerData data) {
        Player user = Bukkit.getPlayer(data.getPlayerName());
        int level = data.getLevel().get(talentKey);
        int amount = TalentManager.getInstance().getTalent(talentKey).getLevels().get(level);
        return (amount != 0 && random.nextInt(100) <= amount);
    }
}
