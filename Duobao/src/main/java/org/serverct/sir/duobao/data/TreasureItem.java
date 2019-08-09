package org.serverct.sir.duobao.data;

import lombok.Data;
import org.bukkit.Material;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public @Data class TreasureItem implements ConfigurationSerializable {

    private Material itemType;
    private int minAmount;
    private int amountRange;
    private int rate;

    @Override
    public Map<String, Object> serialize() {
        Map<String, Object> dataMap = new HashMap<>();

        dataMap.put("Material", itemType.toString());
        dataMap.put("MinAmount", minAmount);
        dataMap.put("Range", amountRange);
        dataMap.put("Rate", rate);

        return dataMap;
    }

    public TreasureItem(Map<String, Object> data) {
        this.itemType = Material.valueOf(((String) data.get("Material")).toUpperCase());
        this.minAmount = (int) data.get("MinAmount");
        this.amountRange = (int) data.get("Range");
        this.rate = (int) data.get("Rate");
    }

    public TreasureItem(ConfigurationSection section) {
        this.itemType = Material.valueOf((section.getString("Material").toUpperCase()));
        this.minAmount = section.getInt("MinAmount");
        this.amountRange = section.getInt("Range");
        this.rate = section.getInt("Rate");
    }

    public boolean check() {
        Random random = new Random();
        return random.nextInt(100) <= rate;
    }

    public ItemStack item() {
        Random random = new Random();
        ItemStack treasure = new ItemStack(itemType);
        treasure.setAmount(minAmount + random.nextInt(amountRange));
        return treasure;
    }
}
