package org.serverct.sir.hunhuan.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.hunhuan.enums.HunHuanEffect;

import java.util.List;

public @Data @AllArgsConstructor class HunhuanData {
    String id;
    ItemStack item;
    List<String> fjlore;
    List<HunHuanEffect> effects;
}
