package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifecore.data.Area;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AreaManager {

    @Getter private Map<String, Area> loadedArea = new HashMap<>();
    @Getter private Map<Area, List<Player>> enterdPlayer = new HashMap<>();

    private List<Player> enterdPlayerList;

    public boolean registerArea(String id, Location loc1, Location loc2) {
        if(!loadedArea.containsKey(id)) {
            loadedArea.put(id, new Area(id, loc1, loc2));
            return true;
        }
        return false;
    }

    public boolean registerArea(Area area) {
        if(!loadedArea.containsKey(area.getId())) {
            loadedArea.put(area.getId(), area);
            return true;
        }
        return false;
    }

    public boolean unregisterArea(Area area) {
        loadedArea.remove(area.getId());
        enterdPlayer.remove(area);
        return true;
    }

    public Area checkPlayer(Player player) {
        for(Area area : enterdPlayer.keySet()) {
            if(enterdPlayer.get(area).contains(player)) {
                return area;
            }
        }
        return null;
    }

    public boolean logPlayer(Area area, Player player) {
        if(enterdPlayer.containsKey(area)) {
            enterdPlayerList = enterdPlayer.get(area);

            if(!enterdPlayerList.contains(player)) {
                enterdPlayerList.add(player);
            }
        } else {
            enterdPlayerList = new ArrayList<>();
            enterdPlayerList.add(player);
        }
        enterdPlayer.put(area, enterdPlayerList);
        return true;
    }

    public boolean unlogPlayer(Area area, Player player) {
        if(enterdPlayer.containsKey(area)) {
            enterdPlayerList = enterdPlayer.get(area);

            if(enterdPlayerList.contains(player)) {
                enterdPlayerList.remove(player);
            }

            enterdPlayer.put(area, enterdPlayerList);
        }
        return true;
    }

}
