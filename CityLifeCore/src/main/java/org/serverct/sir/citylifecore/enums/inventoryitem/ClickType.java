package org.serverct.sir.citylifecore.enums.inventoryitem;

import org.bukkit.event.inventory.InventoryAction;

public enum ClickType {
    LEFT_CLICK("左键", org.bukkit.event.inventory.ClickType.LEFT),
    RIGHT_CLICK("右键", org.bukkit.event.inventory.ClickType.RIGHT),
    SHIFT_LEFT_CLICK("Shift + 左键", org.bukkit.event.inventory.ClickType.SHIFT_LEFT),
    SHIFT_RIGHT_CLICK("Shift + 右键", org.bukkit.event.inventory.ClickType.SHIFT_RIGHT),
    Q_CLICK("丢弃键", org.bukkit.event.inventory.ClickType.DROP),
    SHIFT_Q_CLICK("Shift + 丢弃键", org.bukkit.event.inventory.ClickType.CONTROL_DROP),
    DOUBLE_LEFT_CLICK("双击左键", org.bukkit.event.inventory.ClickType.DOUBLE_CLICK),
    MIDDLE_CLICK("中键", org.bukkit.event.inventory.ClickType.MIDDLE);

    private final String type;
    private final org.bukkit.event.inventory.ClickType clickType;
    ClickType(String type, org.bukkit.event.inventory.ClickType clickType) {
        this.type = type;
        this.clickType = clickType;
    }

    public String getType() {
        return type;
    }
    public org.bukkit.event.inventory.ClickType getClickType() {
        return clickType;
    }
}
