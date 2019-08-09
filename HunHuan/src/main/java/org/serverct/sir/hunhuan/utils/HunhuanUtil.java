package org.serverct.sir.hunhuan.utils;

import com.google.common.collect.Lists;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.hunhuan.HunHuan;
import org.serverct.sir.hunhuan.configuration.HunhuanManager;
import org.serverct.sir.hunhuan.data.HunhuanData;

import java.util.ArrayList;
import java.util.List;

public class HunhuanUtil {

    private static HunhuanUtil instance;
    public static HunhuanUtil getInstance() {
        if(instance == null) {
            instance = new HunhuanUtil();
        }
        return instance;
    }

    private HunhuanManager hunhuanManager = HunHuan.getInstance().getHunhuanManager();
    private LocaleUtil locale = HunHuan.getInstance().getLocale();

    public void reloadHunguManager() {
        hunhuanManager = HunHuan.getInstance().getHunhuanManager();
    }

    public boolean checkItem(ItemStack item) {
        if (item != null && item.getType() != Material.AIR) {
            ItemMeta meta = item.getItemMeta();

            if (item.getAmount() == 1) {
                if (meta.hasLore()) {
                    List<String> lore = meta.getLore();

                    if (lore != null && !lore.isEmpty()) {
                        return hasHungu(lore).size() < HunHuan.getInstance().getAbsorbLimit();
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
            return lore.contains(hunhuanManager.getHeader()) && lore.contains(hunhuanManager.getFooter());
        }
        return false;
    }

    public List<HunhuanData> hasHungu(List<String> lore) {
        List<HunhuanData> result = new ArrayList<>();
        String display = hunhuanManager.getDisplay();

        if(lore != null && !lore.isEmpty()) {
            for(String temp : lore) {
                if(temp.contains(display)) {
                    HunhuanData target = hunhuanManager.getHungu(temp.replace(display, ""));

                    if(target != null) {
                        result.add(target);
                    }
                }
            }
        }
        return result;
    }

    public int getHeaderIndex(List<String> lore) {
        return lore.indexOf(hunhuanManager.getHeader());
    }

    public int getFooterIndex(List<String> lore) {
        return lore.indexOf(hunhuanManager.getFooter());
    }

    public ItemStack inlay(ItemStack item, HunhuanData hungu) {
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        String inlayTag = hunhuanManager.getDisplay() + hungu.getItem().getItemMeta().getDisplayName();

        if(lore == null) {
            lore = new ArrayList<>();
        }

        if(hasHeader(lore)) {
            lore.add(getFooterIndex(lore), inlayTag);
            lore.addAll(getFooterIndex(lore), hungu.getFjlore());
        } else {
            lore.add(hunhuanManager.getHeader());
            lore.add(inlayTag);
            lore.addAll(hungu.getFjlore());
            lore.add(hunhuanManager.getFooter());
        }

        meta.setLore(lore);
        item.setItemMeta(meta);
        return item;
    }

    public ItemStack unload(ItemStack item, HunhuanData hungu) {
        locale.debug("执行 unload 方法.");
        locale.debug("参数 ItemStack 数据: " + item.toString());
        ItemMeta meta = item.getItemMeta();
        List<String> lore = meta.getLore();
        List<HunhuanData> hunguList = hasHungu(lore);
        String inlayTag = hunhuanManager.getDisplay() + hungu.getItem().getItemMeta().getDisplayName();
        locale.debug("已获取目标物品数据.");
        locale.debug("已镶嵌魂骨列表: " + hunguList.toString());
        locale.debug("生成镶嵌标签: " + inlayTag);

        if(hunguList.contains(hungu)) {
            locale.debug("目标装备镶嵌有目标魂骨.");
            int index = lore.lastIndexOf(inlayTag);
            locale.debug("获取末镶嵌标签索引值: " + index);
            for(int i = 0; i <= hungu.getFjlore().size(); i++) {
                locale.debug("准备删除目标 Lore 内容: " + lore.get(index));
                lore.remove(index);
            }

            if(hunguList.size() == 1) {
                locale.debug("目标仅镶嵌有一个魂骨.");
                while(lore.contains(hunhuanManager.getHeader())) {
                    locale.debug("已找到 Header.");
                    lore.remove(hunhuanManager.getHeader());
                    locale.debug("删除 Header 成功.");
                }
                while(lore.contains(hunhuanManager.getFooter())) {
                    locale.debug("已找到 Footer.");
                    lore.remove(hunhuanManager.getFooter());
                    locale.debug("删除 Footer 成功.");
                }
            }
        }

        locale.debug("拆卸动作完成.");
        locale.debug("最终 Lore 数据: " + lore.toString());
        meta.setLore(lore);
        locale.debug("最终 Meta 数据: " + meta.toString());
        ItemStack result = item.clone();
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
