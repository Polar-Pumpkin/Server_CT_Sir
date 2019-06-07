package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;
import org.bukkit.configuration.file.FileConfiguration;

public @Data @AllArgsConstructor class Area {

    private String id;
    private Location point1;
    private Location point2;

    public String[] info() {
        String[] infoMsg = {
                "==========[ Area 区域详细信息 ]==========",
                "  > ID: " + id,
                "  > 坐标点1: " + point1.getX() + ", " + point1.getY() + ", " + point1.getZ(),
                "  > 坐标点2: " + point2.getX() + ", " + point2.getY() + ", " + point2.getZ()
        };
        return infoMsg;
    }

    public boolean hasEntered(Location loc) {
        double sumP1 = point1.getX() + point1.getY() + point1.getZ();
        double sumP2 = point2.getX() + point2.getY() + point2.getZ();
        double sumLoc = loc.getX() + loc.getY() + loc.getZ();
        return sumLoc >= Math.min(sumP1, sumP2) && sumLoc <= Math.max(sumP1, sumP2);
    }

    public void save(FileConfiguration data) {
        data.set("World", point1.getWorld().getName());
        data.set("Point1.X", point1.getX());
        data.set("Point1.Y", point1.getY());
        data.set("Point1.Z", point1.getZ());
        data.set("Point2.X", point2.getX());
        data.set("Point2.Y", point2.getY());
        data.set("Point2.Z", point2.getZ());
    }
}
