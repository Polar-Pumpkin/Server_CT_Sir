package org.serverct.sir.guajichi.config;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.serverct.sir.guajichi.GuaJiChi;
import org.serverct.sir.guajichi.data.Area;
import org.serverct.sir.guajichi.utils.CommonUtil;
import org.serverct.sir.guajichi.utils.LocaleUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class ConfigManager {

    private static ConfigManager instance;
    public static ConfigManager getInstance() {
        if(instance == null) {
            instance = new ConfigManager();
        }
        return instance;
    }

    public static int interval;
    public static Material tool;
    private File file = new File(GuaJiChi.getInstance().getDataFolder() + File.separator + "config.yml");
    private FileConfiguration config = null;
    private Map<String, Area> areas = new HashMap<>();
    private Map<String, Area> waitCreate = new HashMap<>();

    public void load() {
        if(!file.exists()) {
            GuaJiChi.getInstance().saveDefaultConfig();
        }
        config = GuaJiChi.getInstance().getConfig();

        interval = config.getInt("Interval") * 20;
        tool = Material.valueOf(config.getString("Tool").toUpperCase());

        ConfigurationSection areaSection = config.getConfigurationSection("Areas");
        if(areaSection != null) {
            for(String id : areaSection.getKeys(false)) {
                ConfigurationSection target = areaSection.getConfigurationSection(id);
                String worldName = target.getString("World");

                ConfigurationSection loc1Section = target.getConfigurationSection("loc1");
                double X1 = loc1Section.getDouble("X");
                double Y1 = loc1Section.getDouble("Y");
                double Z1 = loc1Section.getDouble("Z");

                ConfigurationSection loc2Section = target.getConfigurationSection("loc2");
                double X2 = loc2Section.getDouble("X");
                double Y2 = loc2Section.getDouble("Y");
                double Z2 = loc2Section.getDouble("Z");

                int money = target.getInt("Money");
                int exp = target.getInt("Exp");

                Area result = new Area(worldName, X1, Y1, Z1, X2, Y2, Z2, money, exp);
                areas.put(id, result);
            }
        }
    }

    public Area check(Location loc) {
        for(Area target : areas.values()) {
            if(target.check(loc)) {
                return target;
            }
        }
        return null;
    }

    public boolean remove(String id) {
        if(areas.containsKey(id)) {
            areas.remove(id);
            config.getConfigurationSection("Areas").set(id, null);
            GuaJiChi.getInstance().saveConfig();
            return true;
        }
        return false;
    }

    private Map<Player, Location> loc1s = new HashMap<>();
    private Map<Player, Location> loc2s = new HashMap<>();

    public void setPoint(Player user, Location loc, boolean isLoc1) {
        Location loc1 = loc1s.getOrDefault(user, null);
        Location loc2 = loc1s.getOrDefault(user, null);
        if(isLoc1) {
            loc1 = loc.clone();
            loc1s.put(user, loc1);
        } else {
            loc2 = loc.clone();
            loc2s.put(user, loc2);
        }

        boolean end = loc1 != null && loc2 != null;
        if(end) {
            if(!loc1.getWorld().getName().equals(loc2.getWorld().getName())) {
                loc1s.remove(user);
                loc2s.remove(user);
                return;
            }
            waitCreate.put(user.getName(), new Area(loc1.getWorld().getName(),
                    loc1.getX(),
                    loc1.getY(),
                    loc1.getZ(),
                    loc2.getX(),
                    loc2.getY(),
                    loc2.getZ(),
                    0,
                    0));
            CommonUtil.sendMessageAsync(GuaJiChi.getInstance().getLocale().buildMessage("Chinese", LocaleUtil.INFO, "已更新您ID下的待创建选区."), user);
        }
    }

    public void create(Player user, String id, int money, int exp) {
        Area target = waitCreate.getOrDefault(user.getName(), null);
        if(target != null) {
            target.setMoney(money);
            target.setExp(exp);
            areas.put(id, target);
            waitCreate.remove(user.getName());

            ConfigurationSection areas = config.getConfigurationSection("Areas");
            if(areas == null) {
                areas = config.createSection("Areas");
            }
            ConfigurationSection area = areas.createSection(id);
            area.set("World", target.getWorldName());
            area.set("loc1.X", target.getX1());
            area.set("loc1.Y", target.getY1());
            area.set("loc1.Z", target.getZ1());
            area.set("loc2.X", target.getX2());
            area.set("loc2.Y", target.getY2());
            area.set("loc2.Z", target.getZ2());
            area.set("Money", money);
            area.set("Exp", exp);

            GuaJiChi.getInstance().saveConfig();
        }
    }

    public boolean hasWaitArea(Player user) {
        return waitCreate.containsKey(user.getName());
    }
}
