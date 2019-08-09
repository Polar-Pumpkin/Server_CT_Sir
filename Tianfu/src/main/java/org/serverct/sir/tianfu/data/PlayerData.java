package org.serverct.sir.tianfu.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.util.CommonUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public @Data @AllArgsConstructor class PlayerData {

    private String playerName;
    private Map<TalentType, Integer> level;

    public PlayerData(File dataFile) {
        this.playerName = CommonUtil.getNoExFileName(dataFile.getName());
        FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);
        ConfigurationSection section = data.getConfigurationSection("Talents");
        this.level = new HashMap<>();
        for(String talentType : section.getKeys(false)) {
            level.put(TalentType.valueOf(talentType.toUpperCase()), Integer.valueOf(section.getString(talentType)));
        }
    }
}
