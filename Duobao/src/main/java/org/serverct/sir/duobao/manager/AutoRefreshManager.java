package org.serverct.sir.duobao.manager;

import org.bukkit.configuration.file.FileConfiguration;
import org.serverct.sir.duobao.Duobao;
import org.serverct.sir.duobao.enums.AutoRefreshType;

import java.util.Random;

public class AutoRefreshManager {

    private static AutoRefreshManager instance;
    public static AutoRefreshManager getInstance() {
        if(instance == null) {
            instance = new AutoRefreshManager();
        }
        return instance;
    }

    public int get(AutoRefreshType type) {
        FileConfiguration config = Duobao.getInstance().getConfig();
        Random random = new Random();
        String data = "0";
        switch (type) {
            case LOWER:
                data = config.getString("Auto-Refresh.Lower.Amount");
                break;
            case FOUND:
                data = config.getString("Auto-Refresh.Found");
                break;
            default:
                break;
        }
        if(data.contains("-")) {
            String[] dataSet = data.split("-");
            int min = Math.min(Integer.parseInt(dataSet[0]), Integer.parseInt(dataSet[1]));
            int range = Math.max(Integer.parseInt(dataSet[0]), Integer.parseInt(dataSet[1])) - min;
            return min + random.nextInt(range);
        } else {
            return Integer.parseInt(data);
        }
    }

    public boolean trigger(int amount) {
        FileConfiguration config = Duobao.getInstance().getConfig();
        return amount <= config.getInt("Auto-Refresh.Lower.Trigger");
    }

}
