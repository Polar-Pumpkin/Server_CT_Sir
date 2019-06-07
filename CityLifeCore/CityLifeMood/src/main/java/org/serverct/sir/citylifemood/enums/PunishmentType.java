package org.serverct.sir.citylifemood.enums;

public enum PunishmentType {
    POTION("药水效果"),
    COMMAND("执行指令"),
    MONEY("扣除金钱"),
    POINT("扣除点卷"),
    HEALTH("扣除生命"),
    EXHAUSTION("扣除饥饿"),
    MESSAGE("发送消息");

    private final String type;
    private PunishmentType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}