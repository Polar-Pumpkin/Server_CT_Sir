package org.serverct.sir.duobao.util;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.configuration.file.FileConfiguration;
import org.serverct.sir.duobao.Duobao;
import org.serverct.sir.duobao.manager.GameManager;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class AreaUtil {

    public static List<Location> generateTreasureLocation(Map<Integer, Location> area) {
        List<Location> result = new ArrayList<>();
        Location point1 = area.get(1);
        Location point2 = area.get(2);
        if(point1.getWorld().getName().equals(point2.getWorld().getName())) {
            World world = point1.getWorld();
            double minX = Math.min(point1.getX(), point2.getX());
            double minY = Math.min(point1.getY(), point2.getY());
            double minZ = Math.min(point1.getZ(), point2.getZ());

            double maxX = Math.max(point1.getX(), point2.getX());
            double maxY = Math.max(point1.getY(), point2.getY());
            double maxZ = Math.max(point1.getZ(), point2.getZ());

            int rangeX = (int) Math.floor(maxX - minX);
            int rangeY = (int) Math.floor(maxY - minY);
            int rangeZ = (int) Math.floor(maxZ - minZ);

            Location loc;
            Location test;
            for(int x = 0; x < rangeX; x++) {
                for(int y = 0; y < rangeY; y++) {
                    for(int z = 0; z < rangeZ; z++) {
                        loc = new Location(world, minX + x, minY + y, minZ + z);
                        if(loc.getBlock().getType() == Material.AIR) {
                            test = loc.clone().add(0, -1, 0);
                            if(test.getBlock().getType() != Material.AIR) {
                                result.add(loc);
                            }
                        }
                    }
                }
            }

        }
        return result;
    }

    public static void setPoint(Location loc, boolean isPoint1) {
        FileConfiguration config = Duobao.getInstance().getConfig();
        if(isPoint1) {
            config.set("Area.1.World", loc.getWorld().getName());
            config.set("Area.1.X", loc.getX());
            config.set("Area.1.Y", loc.getY());
            config.set("Area.1.Z", loc.getZ());
            GameManager.getInstance().loadArea();
        } else {
            config.set("Area.2.World", loc.getWorld().getName());
            config.set("Area.2.X", loc.getX());
            config.set("Area.2.Y", loc.getY());
            config.set("Area.2.Z", loc.getZ());
            GameManager.getInstance().loadArea();
        }
        Duobao.getInstance().saveConfig();
    }

    public static boolean checkAround(Location loc) {
        boolean n = loc.clone().add(0, 0, 1).getBlock().getType() != Material.CHEST;
        boolean s = loc.clone().add(0, 0, -1).getBlock().getType() != Material.CHEST;
        boolean w = loc.clone().add(-1, 0, 0).getBlock().getType() != Material.CHEST;
        boolean e = loc.clone().add(1, 0, 0).getBlock().getType() != Material.CHEST;
        return n && s && w && e;
    }
}
