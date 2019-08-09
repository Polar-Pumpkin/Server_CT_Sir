package org.serverct.sir.tianfu.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.talents.TalentExecutor;

import java.util.Map;

public @Data @AllArgsConstructor class Talent {

    private TalentType type;
    private String displayName;
    private String description;
    private String symbol;
    private Map<Integer, Integer> levels;
    private TalentExecutor executor;
    private double money;
    private int point;
}
