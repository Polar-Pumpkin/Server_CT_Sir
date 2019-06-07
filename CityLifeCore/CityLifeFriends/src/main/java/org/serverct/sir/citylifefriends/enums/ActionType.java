package org.serverct.sir.citylifefriends.enums;

public enum ActionType {
    COMMAND("执行命令"),
    SUGGEST("建议命令");

    private final String type;
    ActionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
