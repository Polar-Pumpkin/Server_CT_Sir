package org.serverct.sir.citylifemood.enums;

public enum MoodAreaType {
    CORE("CL Core 核心内建"),
    RESIDENCE("Residence 领地链接");

    private final String type;
    MoodAreaType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
