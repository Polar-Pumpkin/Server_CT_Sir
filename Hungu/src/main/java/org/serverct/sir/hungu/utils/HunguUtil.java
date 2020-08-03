package org.serverct.sir.hungu.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.hungu.Hungu;
import org.serverct.sir.hungu.configuration.HunguManager;
import org.serverct.sir.hungu.data.HunguData;

import java.util.ArrayList;
import java.util.List;

public class HunguUtil {

    private static HunguUtil instance;
    public static HunguUtil getInstance() {
        if(instance == null) {
            instance = new HunguUtil();
        }
        return instance;
    }

    private HunguManager hunguManager = Hungu.getInstance().getHunguManager();
    private final LocaleUtil locale = Hungu.getInstance().getLocale();

    public void reloadHunguManager() {
        hunguManager = Hungu.getInstance().getHunguManager();
    }

    public boolean checkItem(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            ItemMeta meta = item.getItemMeta();

            if (item.getAmount() == 1) {
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();

                    if (lore != null && !lore.isEmpty()) {
                        return hasHungu(lore).size() < Hungu.getInstance().getAbsorbLimit();
                    }
                } else {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean hasHeader(List<String> lore) {
        if(lore != null && !lore.isEmpty()) {
            return lore.contains(hunguManager.getHeader()) && lore.contains(hunguManager.getFooter());
        }
        return false;
    }

    public List<HunguData> hasHungu(List<String> lore) {
        List<HunguData> result = new ArrayList<>();
        String display = hunguManager.getDisplay();

        if(lore != null && !lore.isEmpty()) {
            for(String temp : lore) {
                if(temp.contains(display)) {
                    HunguData target = hunguManager.getHungu(temp.replace(display, ""));

                    if(target != null) {
                        result.add(target);
                    }
                }
            }
        }
        return result;
    }

    public int getHeaderIndex(List<String> lore) {
        return lore.indexOf(hunguManager.getHeader());
    }

    public int getFooterIndex(List<String> lore) {
        return lore.lastIndexOf(hunguManager.getFooter());
    }

    public ItemStack inlay(ItemStack item, HunguData hungu) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        String inlayTag = hunguManager.getDisplay() + hungu.getItem().getItemMeta().getDisplayName();

        if(lore == null) {
            lore = new ArrayList<>();
        }

        if(hasHeader(lore)) {
            lore.add(getFooterIndex(lore), inlayTag);
            lore.addAll(getFooterIndex(lore), hungu.getFjlore());
        } else {
            lore.add(hunguManager.getHeader());
            lore.add(inlayTag);
            lore.addAll(hungu.getFjlore());
            lore.add(hunguManager.getFooter());
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack unload(ItemStack item, HunguData hungu) {
        locale.debug("执行 unload 方法.");
        locale.debug("参数 ItemStack 数据: " + item.toString());
        ItemStack result = item.clone();
        ItemMeta meta = result.getItemMeta();
        List<String> lore = meta.getLore();
        List<HunguData> hunguList = hasHungu(lore);
        String inlayTag = hunguManager.getDisplay() + hungu.getItem().getItemMeta().getDisplayName();
        locale.debug("已获取目标物品数据.");
        locale.debug("已镶嵌魂骨列表: " + hunguList.toString());
        locale.debug("生成镶嵌标签: " + inlayTag);

        if (hunguList.contains(hungu)) {
            locale.debug("目标装备镶嵌有目标魂骨.");
            int index = lore.lastIndexOf(inlayTag);
            locale.debug("获取末镶嵌标签索引值: " + index);
            for(int i = 0; i <= hungu.getFjlore().size(); i++) {
                locale.debug("准备删除目标 Lore 内容: " + lore.get(index));
                lore.remove(index);
            }

            if(hunguList.size() == 1) {
                locale.debug("目标仅镶嵌有一个魂骨.");
                while(lore.contains(hunguManager.getHeader())) {
                    locale.debug("已找到 Header.");
                    lore.remove(hunguManager.getHeader());
                    locale.debug("删除 Header 成功.");
                }
                while(lore.contains(hunguManager.getFooter())) {
                    locale.debug("已找到 Footer.");
                    lore.remove(hunguManager.getFooter());
                    locale.debug("删除 Footer 成功.");
                }
            }
        }

        locale.debug("拆卸动作完成.");
        locale.debug("最终 Lore 数据: " + lore.toString());
        meta.setLore(lore);
        locale.debug("最终 Meta 数据: " + meta.toString());
        if(result.setItemMeta(meta)) {
            locale.debug("ItemMeta 设置成功.");
        }
        locale.debug("最终 ItemStack 数据: " + result.toString());
        return result;
    }

    public List<Player> getOnlinePlayers() {
        // 实例化两个List用于存放Player和World
        List<Player> players = Lists.newArrayList();
        List<World> worlds = Lists.newArrayList();
        worlds.addAll(Bukkit.getWorlds());
        // 遍历所有的世界
        for (int i = 0; i < worlds.size(); i++) {
            // 如果第i个世界的玩家是空的则进行下一次循环
            if (worlds.get(i).getPlayers().isEmpty()) {
                continue;
            } else {
                // 不是空的则添加到players集合中
                players.addAll(worlds.get(i).getPlayers());
            }
        }
        return players;
    }
}
