package org.serverct.sir.citylifemood.configuration;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.sir.citylifecore.data.Area;
import org.serverct.sir.citylifemood.CityLifeMood;
import org.serverct.sir.citylifemood.data.MoodArea;
import org.serverct.sir.citylifemood.enums.MoodAreaType;
import org.serverct.sir.citylifemood.utils.CommonUtil;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class AreaManager {

    private static AreaManager instance;

    public static AreaManager getInstance() {
        if(instance == null) {
            instance = new AreaManager();
        }
        return instance;
    }

    private File dataFolder = new File(CityLifeMood.getInstance().getDataFolder() + File.separator + "Areas");
    @Getter private Map<String, MoodArea> areasMap = new HashMap<>();
    private org.serverct.sir.citylifecore.manager.AreaManager areaManager;

    private FileConfiguration targetData;

    private double x1;
    private double y1;
    private double z1;
    private Location location1;
    private double x2;
    private double y2;
    private double z2;
    private Location location2;
    private World targetWorld;

    private int targetStep;
    private int targetPeriod;
    private String targetReason;
    private MoodAreaType targetType;
    private Area targetArea;
    private MoodArea targetMoodArea;

    private int counter;

    private File[] areaDataFiles;

    private File newAreaDataFile;
    private FileConfiguration newAreaData;

    public void loadArea() {
        if(!dataFolder.exists()) {
            dataFolder.mkdirs();
            Bukkit.getLogger().info("  > 未找到区域配置文件夹, 已自动生成.");
        } else {
            areaDataFiles = dataFolder.listFiles(pathname -> {
                String fileName = pathname.getName();
                return fileName.endsWith(".yml");
            });

            if(areaDataFiles != null) {
                counter = 0;

                for(File dataFile : areaDataFiles) {
                    targetData = YamlConfiguration.loadConfiguration(dataFile);
                    targetStep = targetData.getInt("Step");
                    targetPeriod = targetData.getInt("Period") * 20;
                    targetType = MoodAreaType.valueOf(targetData.getString("Type").toUpperCase());
                    targetReason = ChatColor.translateAlternateColorCodes('&', targetData.getString("Reason"));
                    targetWorld = Bukkit.getWorld(targetData.getString("World"));

                    x1 = targetData.getDouble("Point1.X");
                    y1 = targetData.getDouble("Point1.Y");
                    z1 = targetData.getDouble("Point1.Z");
                    location1 = new Location(targetWorld, x1, y1, z1);
                    x2 = targetData.getDouble("Point2.X");
                    y2 = targetData.getDouble("Point2.Y");
                    z2 = targetData.getDouble("Point2.Z");
                    location2 = new Location(targetWorld, x2, y2, z2);

                    targetArea = new Area("MOOD_" + counter, location1, location2);
                    targetMoodArea = new MoodArea(CommonUtil.getFileNameNoEx(dataFile.getName()), targetType, targetArea, targetStep, targetPeriod, targetReason);
                    if(targetType == MoodAreaType.CORE) {
                        areasMap.put(CommonUtil.getFileNameNoEx(dataFile.getName()), targetMoodArea);
                    }

                    counter++;
                }
            }

            areaManager = CityLifeMood.getInstance().getCoreApi().getAreaAPI();
            if(!areasMap.values().isEmpty()) {
                for(MoodArea moodArea : areasMap.values()) {
                    areaManager.registerArea(moodArea.getArea());
                }
            }

            Bukkit.getLogger().info("  > 共加载 " + counter + " 个区域.");
        }
    }

    public void saveMoodArea(MoodArea moodArea) {
        newAreaDataFile = new File(dataFolder.getAbsolutePath() + File.separator + moodArea.getId() + ".yml");
        newAreaData = YamlConfiguration.loadConfiguration(newAreaDataFile);

        newAreaData.set("Type", moodArea.getType().toString());
        newAreaData.set("Step", moodArea.getStep());
        newAreaData.set("Period", moodArea.getPeriod());
        newAreaData.set("Reason", moodArea.getReason());
        moodArea.getArea().save(newAreaData);

        areasMap.put(moodArea.getId(), moodArea);
        if(moodArea.getType() == MoodAreaType.CORE) {
            areaManager.registerArea(moodArea.getArea());
        }
        try {
            newAreaData.save(newAreaDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void deleteMoodArea(String id) {
        if(areasMap.containsKey(id)) {
            if(areasMap.get(id).getType() == MoodAreaType.CORE) {
                areaManager.unregisterArea(areasMap.get(id).getArea());
            }
            areasMap.remove(id);
            newAreaDataFile = new File(dataFolder.getAbsolutePath() + File.separator + id + ".yml");
            newAreaDataFile.delete();
        }
    }

}
