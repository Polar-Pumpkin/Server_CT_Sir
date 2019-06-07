package org.serverct.sir.citylifecore.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.utils.ItemStackUtil;

import java.io.File;

public class ConfigData {

    private static ConfigData instance;

    public static ConfigData getInstance() {
        if(instance == null) {
            instance = new ConfigData();
        }
        return instance;
    }

    private File dataFile = new File(CityLifeCore.getInstance().getDataFolder() + File.separator + "config.yml");
    @Getter private FileConfiguration data;

    public void loadConfig() {
        if(!dataFile.exists()) {
            CityLifeCore.getInstance().saveDefaultConfig();
            Bukkit.getLogger().info("> 未找到配置文件, 已自动生成.");
        } else {
            Bukkit.getLogger().info("> 已加载配置文件.");
        }
        data = CityLifeCore.getInstance().getConfig();
    }

    public ItemStack getSelector() {
        return ItemStackUtil.buildBasicItem(data.getConfigurationSection("Selector"));
    }

}
