package org.serverct.sir.mood;

import lombok.Data;
import org.bukkit.Location;

public @Data class Area {

    private String id;
    private Location point1;
    private Location point2;
    private int step;
    private int period;

    public Area(String id, Location loc1, Location loc2, int step, int period) {
        this.id = id;
        this.point1 = loc1;
        this.point2 = loc2;
        this.step = step;
        this.period = period;
    }
}
