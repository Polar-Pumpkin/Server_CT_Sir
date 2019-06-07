package org.serverct.sir.citylifecore.enums.inventoryitem;

public enum OptionType {
    KEEPOPEN("保持开启"),
    PRICE("金钱要求"),
    POINT("点卷要求");

    private final String type;
    OptionType(String type) {
        this.type = type;
    }

    public String getType() {
        return type;
    }
}
