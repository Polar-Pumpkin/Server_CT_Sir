package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.configuration.ConfigData;
import org.serverct.sir.citylifecore.data.Area;
import org.serverct.sir.citylifecore.enums.MessageType;
import org.serverct.sir.citylifecore.utils.LocaleUtil;

import java.util.HashMap;
import java.util.Map;

public class SelectionManager {

    @Getter private Map<Player, Area> selectionMap = new HashMap<>();

    private Map<Player, Location> point1Map = new HashMap<>();
    private Map<Player, Location> point2Map = new HashMap<>();

    private Area selection;

    private LocaleUtil locale = CityLifeCore.getInstance().getLocale();

    public boolean addSelection(Player player, Area area) {
        if(!selectionMap.containsKey(player)) {
            selectionMap.put(player, area);
            return true;
        }
        return false;
    }

    public void clearSelection(Player player) {
        selectionMap.remove(player);
        point1Map.remove(player);
        point2Map.remove(player);
    }

    public void setPoint1(Player player, Location location) {
        point1Map.put(player, location);
        player.sendMessage(locale.buildMessage(MessageType.INFO, "&7已设置点1: " + location.getX() + ", " + location.getY() + ", " + location.getZ()));
    }

    public void setPoint2(Player player, Location location) {
        point2Map.put(player, location);
        player.sendMessage(locale.buildMessage(MessageType.INFO, "&7已设置点2: " + location.getX() + ", " + location.getY() + ", " + location.getZ()));
    }

    public boolean hasPoint1(Player player) {
        return point1Map.containsKey(player);
    }

    public boolean hasPoint2(Player player) {
        return point2Map.containsKey(player);
    }

    public Area createSelection(Player player) {
        if(selectionMap.containsKey(player)) {
            return selectionMap.get(player);
        } else {
            if(hasPoint1(player) && hasPoint2(player)) {
                if(point1Map.get(player).getWorld().equals(point2Map.get(player).getWorld())) {
                    selection = new Area("SELECTION_" + player.getName(), point1Map.get(player), point2Map.get(player));
                    addSelection(player, selection);
                    player.sendMessage(locale.buildMessage(MessageType.INFO, "&7成功创建 SELECTION(Area) 选区对象."));
                    return selection;
                }
            } else {
                player.sendMessage(locale.buildMessage(MessageType.ERROR, "&7您还没有用选择工具选定过区域."));
            }
            return null;
        }
    }

    public ItemStack getSelector() {
        return ConfigData.getInstance().getSelector();
    }
}
