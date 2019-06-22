package org.serverct.sir.citylifemood.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.serverct.sir.citylifecore.data.Area;
import org.serverct.sir.citylifemood.enums.MoodAreaType;

public @Data @AllArgsConstructor class MoodArea {

    private String id;
    private MoodAreaType type;
    private Area area;
    private int step;
    private int period;
    private String reason;

    public String[] getInfo() {
        String[] infoMsg = {
                "&7==========&c[ &6MoodArea &9区域详细信息 &c]&7==========",
                "  &e&l> &7MoodArea ID: &c" + id,
                "  &e&l> &7Area ID: &c" + area.getId(),
                "  &e&l> &7区域类型: &c" + type.getType(),
                "  &e&l> &7心情步进: &c" + step,
                "  &e&l> &7时间间隔: &c" + period,
                "  &e&l> &7变化原因: &c" + reason,
                "  &e&l> &7坐标点1: &c" + area.getPoint1().getX() + "&7, &c" + area.getPoint1().getY() + "&7, &c" + area.getPoint1().getZ(),
                "  &e&l> &7坐标点2: &c" + area.getPoint2().getX() + "&7, &c" + area.getPoint2().getY() + "&7, &c" + area.getPoint2().getZ()
        };
        return infoMsg;
    }

    public String getDescription() {
        return id + "(步进: " + step + ", 间隔: " + period + "s)";
    }

}
