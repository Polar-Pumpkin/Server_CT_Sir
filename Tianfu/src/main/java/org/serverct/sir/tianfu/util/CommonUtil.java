package org.serverct.sir.tianfu.util;

import com.google.common.collect.Lists;
import org.bukkit.*;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.data.Talent;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.hooks.PlayerPointsHook;
import org.serverct.sir.tianfu.hooks.VaultHook;

import java.util.ArrayList;
import java.util.List;

public class CommonUtil {

    private static LocaleUtil locale;

    /**
     * 取服务器在线玩家
     *
     * @return 玩家集合
     */
    public static List<Player> getOnlinePlayers() {
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

    public static ItemStack buildItem(ConfigurationSection section) {
        ItemStack result = new ItemStack(Material.valueOf(section.getString("Material").toUpperCase()));
        ItemMeta meta = result.getItemMeta();

        meta.setDisplayName(ChatColor.translateAlternateColorCodes('&', section.getString("Display")));

        List<String> lore = new ArrayList<>();
        for(String text : section.getStringList("Lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', text));
        }
        meta.setLore(lore);

        result.setItemMeta(meta);
        return result;
    }

    public static ItemStack applyPlaceholder(ItemStack item, TalentType type, PlayerData data) {
        ItemStack result = item.clone();
        ItemMeta meta = result.getItemMeta();
        meta.setLore(PlaceholderUtil.checkAll(meta.getLore(), type, data));
        meta.setDisplayName(PlaceholderUtil.check(meta.getDisplayName(), type, data));
        result.setItemMeta(meta);
        return result;
    }

    public static String getNoExFileName(String filename) {
        if ((filename != null) && (filename.length() > 0)) {
            int dot = filename.lastIndexOf('.');
            if ((dot >-1) && (dot < (filename.length()))) {
                return filename.substring(0, dot);
            }
        }
        return filename;
    }

    public static void closeInventory(HumanEntity humanEntity, Sound closeSound) {
        BukkitRunnable invTask = new BukkitRunnable() {
            @Override
            public void run() {
                humanEntity.closeInventory();
            }
        };
        invTask.runTask(Tianfu.getInstance());
        if(closeSound != null) {
            Player user = (Player) humanEntity;
            user.playSound(user.getLocation(), closeSound, 1, 1);
        }
    }

    public static void openInventory(HumanEntity humanEntity, Inventory inventory, Sound openSound){
        BukkitRunnable invTask = new BukkitRunnable() {
            @Override
            public void run() {
                humanEntity.openInventory(inventory);
            }
        };
        invTask.runTask(Tianfu.getInstance());
        if(openSound != null) {
            Player user = (Player) humanEntity;
            user.playSound(user.getLocation(), openSound, 1, 1);
        }
    }

    public static boolean checkMoney(Player player, Talent talent) {
        locale = Tianfu.getInstance().getLocale();
        double money = talent.getMoney();

        return VaultHook.getInstance().getBalances(player) >= money;
    }

    public static boolean checkPoint(Player player, Talent talent) {
        locale = Tianfu.getInstance().getLocale();
        int point = talent.getPoint();

        return PlayerPointsHook.getInstance().getBalances(player) >= point;
    }
}
