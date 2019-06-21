package org.serverct.sir.citylifefriends.configuration;

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

    private File configFile = new File(CityLifeFriends.getINSTANCE().getDataFolder() + File.separator + "config.yml");

    public void loadConfig() {
        if(!configFile.exists()) {
            CityLifeFriends.getINSTANCE().saveDefaultConfig();
        }
    }

}
