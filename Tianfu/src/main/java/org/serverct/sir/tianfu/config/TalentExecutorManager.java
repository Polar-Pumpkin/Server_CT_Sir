package org.serverct.sir.tianfu.config;

import lombok.Getter;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.talents.*;

import java.util.HashMap;
import java.util.Map;

public class TalentExecutorManager {

    private static TalentExecutorManager instance;
    public static TalentExecutorManager getInstance() {
        if(instance == null) {
            instance = new TalentExecutorManager();
        }
        return instance;
    }

    @Getter private Map<TalentType, TalentExecutor> talentExecutorMap = new HashMap<>();

    public TalentExecutorManager() {
        talentExecutorMap.put(TalentType.DAMAGE, new Damage());
        talentExecutorMap.put(TalentType.HEALTHREFILL, new HealthRefill());
        talentExecutorMap.put(TalentType.REGEN, new Regen());
        talentExecutorMap.put(TalentType.IMPRISONMENT, new Imprisonment());
        talentExecutorMap.put(TalentType.LIGHTNING, new Lightning());
    }

    public TalentExecutor getTalentExecutor(TalentType type) {
        return talentExecutorMap.get(type);
    }
}
