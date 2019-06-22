package org.serverct.sir.citylifefriends.configuration;

import lombok.Getter;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.sir.citylifefriends.CityLifeFriends;

import java.io.File;

public class ConfigDataManager {

    private static ConfigDataManager instance;

    public static ConfigDataManager getInstance() {
        if(instance == null) {
            instance = new ConfigDataManager();
        }
        return instance;
    }

    private File configFile = new File(CityLifeFriends.getInstance().getDataFolder() + File.separator + "config.yml");
    @Getter private FileConfiguration data = YamlConfiguration.loadConfiguration(configFile);

    public void loadConfig() {
        if(!configFile.exists()) {
            CityLifeFriends.getInstance().saveDefaultConfig();
        }
    }

}
