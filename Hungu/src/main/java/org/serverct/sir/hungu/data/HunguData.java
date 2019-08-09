package org.serverct.sir.hungu.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public @Data @AllArgsConstructor class HunguData {
    String id;
    ItemStack item;
    List<String> fjlore;
}
