package org.serverct.sir.tianfu.config;

import lombok.Getter;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.data.Talent;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.util.LocaleUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class TalentManager {

    private static TalentManager instance;
    public static TalentManager getInstance() {
        if(instance == null) {
            instance = new TalentManager();
        }
        return instance;
    }
    private LocaleUtil locale = Tianfu.getInstance().getLocale();

    @Getter private Map<TalentType, Talent> talentMap = new HashMap<>();

    private File dataFile = new File(Tianfu.getInstance().getDataFolder() + File.separator + "config.yml");
    FileConfiguration data;

    public void load() {
        if(!dataFile.exists()) {
            Tianfu.getInstance().saveDefaultConfig();
            locale.buildMessage(Tianfu.getInstance().getLocaleKey(), MessageType.WARN, "&7未找到配置文件, 已自动生成.");
        }
        data = Tianfu.getInstance().getConfig();
        for(String key : data.getConfigurationSection("Talent").getKeys(false)) {
            ConfigurationSection talentSection = data.getConfigurationSection("Talent." + key);
            ConfigurationSection levelSection = talentSection.getConfigurationSection("Levels");
            TalentType talentType = TalentType.valueOf(key.toUpperCase());
            Map<Integer, Integer> levelData = new HashMap<>();

            for(String level : levelSection.getKeys(false)) {
                levelData.put(Integer.valueOf(level), levelSection.getInt(level));
            }

            if(!levelData.containsKey(0)) {
                levelData.put(0, 0);
            }

            talentMap.put(
                    talentType,
                    new Talent(
                            talentType,
                            ChatColor.translateAlternateColorCodes('&', talentSection.getString("Display")),
                            ChatColor.translateAlternateColorCodes('&', talentSection.getString("Description")),
                            ChatColor.translateAlternateColorCodes('&', talentSection.getString("Symbol")),
                            levelData,
                            TalentExecutorManager.getInstance().getTalentExecutor(talentType),
                            talentSection.getDouble("Cost.Money"),
                            talentSection.getInt("Cost.Point")
                    )
            );
        }
        locale.buildMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "&7共加载 &c" + talentMap.size() + " &7个天赋.");
    }

    public Talent getTalent(TalentType type) {
        return talentMap.get(type);
    }

    public boolean setLevel(TalentType type, int level, int value) {
        Talent target = getTalent(type);
        Map<Integer, Integer> levels = target.getLevels();

        if(level <= levels.size() + 1) {
            if(level >= levels.size()) {
                if(value != 0) {
                    levels.put(level, value);
                } else {
                    if(level == levels.size()) {
                        levels.remove(level);
                    }
                }
                return true;
            } else {
                if(value != 0) {
                    levels.put(level, value);
                } else {
                    return false;
                }
            }
            target.setLevels(levels);
            talentMap.put(type, target);
            save(target);
        }
        return false;
    }

    public boolean addLevel(TalentType type, int value) {
        return setLevel(type, getTalent(type).getLevels().size() + 1, value);
    }

    public void save(Talent talent) {
        ConfigurationSection talentSection = data.getConfigurationSection("Talent." + talent.getType().toString());

        talentSection.set("Display", talent.getDisplayName());
        talentSection.set("Description", talent.getDescription());
        talentSection.set("Symbol", talent.getSymbol());

        ConfigurationSection levelSection = talentSection.getConfigurationSection("Levels");
        Map<Integer, Integer> levels = talent.getLevels();
        for(int level : levels.keySet()) {
            levelSection.set(String.valueOf(level), levels.get(level));
        }

        talentSection.set("Cost.Money", talent.getMoney());
        talentSection.set("Cost.Point", talent.getPoint());

        Tianfu.getInstance().saveConfig();
    }

    public boolean isTalentType(String text) {
        for(TalentType type : TalentType.values()) {
            if(text.toUpperCase().equals(type.toString())) {
                return true;
            }
        }
        return false;
    }
}
