package org.serverct.sir.mood.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.sir.mood.Mood;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Area {

    private static Area instance;

    public static Area getInstance() {
        if(instance == null) {
            instance = new Area();
        }
        return instance;
    }

    private File dataFile = new File(Mood.getInstance().getDataFolder() + File.separator + "areas.yml");
    @Getter private FileConfiguration data = YamlConfiguration.loadConfiguration(dataFile);

    @Getter private Map<String, org.serverct.sir.mood.Area> areasMap = new HashMap<>();

    private ConfigurationSection targetSection;
    private double x1;
    private double y1;
    private double z1;
    private Location location1;
    private double x2;
    private double y2;
    private double z2;
    private Location location2;
    private World cacheWorld;
    private int cacheStep;
    private int cachePeriod;
    private org.serverct.sir.mood.Area cacheArea;
    private int counter;

    public void loadArea() {
        if(!dataFile.exists()) {
            try {
                dataFile.createNewFile();
                Bukkit.getLogger().info("  > 未找到区域文件, 已自动生成.");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            counter = 0;
            for(String section : data.getKeys(false)) {
                targetSection = data.getConfigurationSection(section);
                cacheStep = targetSection.getInt("Step");
                cachePeriod = targetSection.getInt("Period");
                cacheWorld = Bukkit.getWorld(targetSection.getString("World"));

                x1 = targetSection.getDouble("Point1.X");
                y1 = targetSection.getDouble("Point1.Y");
                z1 = targetSection.getDouble("Point1.Z");
                location1 = new Location(cacheWorld, x1, y1, z1);
                x2 = targetSection.getDouble("Point2.X");
                y2 = targetSection.getDouble("Point2.Y");
                z2 = targetSection.getDouble("Point2.Z");
                location2 = new Location(cacheWorld, x1, y1, z1);

                cacheArea = new org.serverct.sir.mood.Area(section, location1, location2, cacheStep, cachePeriod);
                areasMap.put(section, cacheArea);
                counter++;
            }
            Bukkit.getLogger().info("  > 共加载 " + counter + " 个区域.");
        }
    }

}
