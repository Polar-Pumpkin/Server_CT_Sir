package org.serverct.sir.citylifemood.data;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.citylifemood.enums.ConsumableType;

public class Consumable {

    @Getter @Setter private ItemStack item;
    @Getter private ConsumableType type;
    @Getter @Setter int value;

    public Consumable(ItemStack itemStack, ConsumableType consumableType, int value) {
        this.item = itemStack;
        this.type = consumableType;
        this.value = value;
    }

}
