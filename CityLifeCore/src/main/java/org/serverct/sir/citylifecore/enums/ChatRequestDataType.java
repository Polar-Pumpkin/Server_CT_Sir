package org.serverct.sir.citylifecore.enums;

public enum ChatRequestDataType {
    INT("数字"),
    STRING("字符串"),
    CHINESE("中文字符串");

    private final String type;
    ChatRequestDataType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
