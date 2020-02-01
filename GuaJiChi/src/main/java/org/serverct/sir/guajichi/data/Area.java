package org.serverct.sir.guajichi.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.Location;

public @Data @AllArgsConstructor class Area {

    private String worldName;
    private double X1;
    private double Y1;
    private double Z1;
    private double X2;
    private double Y2;
    private double Z2;
    private int money;
    private int exp;

    public boolean check(Location loc) {

        double locX = loc.getX();
        double locY = loc.getY();
        double locZ = loc.getZ();

        double maxX = Math.max(X1, X2);
        double maxY = Math.max(Y1, Y2);
        double maxZ = Math.max(Z1, Z2);

        double minX = Math.min(X1, X2);
        double minY = Math.min(Y1, Y2);
        double minZ = Math.min(Z1, Z2);

        boolean x = locX <= maxX && locX >= minX;
        boolean y = locY <= maxY && locY >= minY;
        boolean z = locZ <= maxZ && locZ >= minZ;

        return x && y && z;
    }
}
