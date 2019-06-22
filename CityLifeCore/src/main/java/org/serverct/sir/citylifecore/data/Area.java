package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public @Data @AllArgsConstructor class Area {

    private String id;
    private Location point1;
    private Location point2;

    public String[] getInfo() {
        String[] infoMsg = {
                "==========[ Area 区域详细信息 ]==========",
                "  > ID: " + id,
                "  > 坐标点1: " + point1.getX() + ", " + point1.getY() + ", " + point1.getZ(),
                "  > 坐标点2: " + point2.getX() + ", " + point2.getY() + ", " + point2.getZ()
        };
        return infoMsg;
    }

    public boolean hasEntered(Location location) {
        String worldName = getWorldName();
        if(worldName != null && !worldName.equals("")) {
            int x1 = (int) point1.getX();
            int x2 = (int) point2.getX();
            int y1 = (int) point1.getY();
            int y2 = (int) point2.getY();
            int z1 = (int) point1.getZ();
            int z2 = (int) point2.getZ();
            int minY = Math.min(y1, y2) - 1;
            int maxY = Math.max(y1, y2) + 1;
            int minZ = Math.min(z1, z2) - 1;
            int maxZ = Math.max(z1, z2) + 1;
            int minX = Math.min(x1, x2) - 1;
            int maxX = Math.max(x1, x2) + 1;
            if (location.getWorld().getName().equals(worldName)) {
                if (location.getX() > minX && location.getX() < maxX) {
                    if (location.getY() > minY && location.getY() < maxY) {
                        return location.getZ() > minZ && location.getZ() < maxZ;
                    }
                }
            }
        }
        return false;
    }

    public void save(FileConfiguration data) {
        if(point1.getWorld().getName().equals(point2.getWorld().getName())) {
            data.set("World", point1.getWorld().getName());
            data.set("Point1.X", point1.getX());
            data.set("Point1.Y", point1.getY());
            data.set("Point1.Z", point1.getZ());
            data.set("Point2.X", point2.getX());
            data.set("Point2.Y", point2.getY());
            data.set("Point2.Z", point2.getZ());
        }
    }

    public String getWorldName() {
        if(point1.getWorld().getName().equals(point2.getWorld().getName())) {
            return point1.getWorld().getName();
        }
        return "";
    }
}
