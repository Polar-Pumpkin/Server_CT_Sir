package org.serverct.sir.anohanamarry.util;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class ItemStackUtil {

    /**
     * 附魔序列化
     * @param set 附魔集合
     * @return 获得附魔序列化
     */
    @SuppressWarnings("deprecation")
    public static String getEnch(Set<Entry<Enchantment, Integer>> set) {
        StringBuilder enchs = new StringBuilder();
        for (Map.Entry<Enchantment, Integer> ench : set) {
            enchs.append(String.format("{id:%s,lvl:%s},", ench.getKey().getId(), ench.getValue()));
        }
        enchs.deleteCharAt(enchs.length() - 1);
        return enchs.toString();
    }

    /**
     * 取一个列表中某个lore存在的行数
     * @param lores 列表集合
     * @param lore 要判断的数据
     * @return 该Lore所在的行数
     */
    public static int getLoreIndex(List<String> lores, String lore) {
        int i = 0;
        for (String a : lores) {
            if (a.contains(lore)) {
                return i;
            }
            i++;
        }
        return -1;
    }

    /**
     * 设置某物品指定行数的Lore
     * @param is 物品(ItemStack类型)
     * @param Line 行数
     * @param lore 要设置的Lore
     */

    public static ItemStack setLore(ItemStack is,int Line,String lore) {
        List <String> lores = new ArrayList<String>();
        if(is == null || is.getType() == Material.AIR) {
            throw new NullPointerException();
        }
        if(is.getItemMeta().hasLore()) {
            ItemMeta itemMeta = is.getItemMeta();
            lores.addAll(is.getItemMeta().getLore());
            lores.add(Line, lore.replaceAll("&", "§"));
            itemMeta.setLore(lores);
            is.setItemMeta(itemMeta);
        }
        return is;
    }

    /**
     * 添加Lore
     * @param is 需要设置的物品
     * @param lore 待添加的String
     * @return 该物品的ItemStack对象
     */
    public static ItemStack addLore(ItemStack is, String lore) {
        if (is != null) {
            lore = ChatColor.translateAlternateColorCodes('&', lore);
            ItemMeta im = is.getItemMeta();
            if (im.hasLore()) {
                List<String> l = im.getLore();
                l.add(lore);
                im.setLore(l);
                is.setItemMeta(im);
                return is;
            }
            List<String> l = new ArrayList<>();
            l.add(lore);
            im.setLore(l);
            is.setItemMeta(im);
            return is;
        }
        throw new NullPointerException();
    }

    /**
     * 替换指定的Lore
     * @param is 需要替换的物品
     * @param old 原Lore
     * @param newString 新Lore
     * @return 该物品的ItemStack
     */
    public static ItemStack replaceLore(ItemStack is, String old, String newString) {
        if (is == null) {
            throw new NullPointerException();
        }
        ItemMeta im = is.getItemMeta();
        List<String> lore = im.getLore();
        if (!lore.contains(old)) {
            return is;
        }
        while (true) {
            if (!lore.contains(old)) {
                break;
            }
            lore.set(lore.indexOf(old), newString);
            break;
        }
        im.setLore(lore);
        is.setItemMeta(im);
        return is;
    }

    public static boolean takeItemInMainHand(Player player, int amount){
        ItemStack item = player.getInventory().getItemInHand();
        int number = item.getAmount() - amount;
        if(number == 0) return false;
        if(number > 0) item.setAmount(amount);
        return true;
    }

    /**
     *
     * @param player 替换物品的玩家
     * @param item 需要被替换的物品
     * @return p都不是
     */
    public static boolean TakePlayerItem(Player player, ItemStack item) {

        int targetItemAmount = 0;
        ItemStack targetitem = item;
        for (ItemStack s : player.getInventory().getContents()) {
            if ((s != null) && (s.getItemMeta().hasDisplayName())
                    && (s.getItemMeta().getDisplayName().equals(item.getItemMeta().getDisplayName()))) {
                targetItemAmount += s.getAmount();
                player.getInventory().removeItem(new org.bukkit.inventory.ItemStack[] { s });
            }
        }
        int cachnum = targetItemAmount;
        targetItemAmount -= item.getAmount();
        if (targetItemAmount > 0) {
            targetitem.setAmount(targetItemAmount);
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack[] { targetitem });
        } else if (targetItemAmount < 0) {
            targetitem.setAmount(cachnum);
            player.getInventory().addItem(new org.bukkit.inventory.ItemStack[] { targetitem });
        }
        return true;
    }

    /**
     * 检查玩家是否拥有某物品
     * @param player 玩家
     * @param is 物品(ItemStack类型)
     * @return Boolean
     */
    public static boolean hasItem(Player player, ItemStack is) {
        Inventory inventory = player.getInventory();
        ItemStack[] invItem = inventory.getContents();
        int i = 0;
        if (i < invItem.length) {
            invItem[i].equals(is);
            return true;
        }
        return false;
    }

    /**
     * 安全的给予物品
     * @param player 玩家
     * @param is 物品
     */
    public static void safeAddItem(Player player, ItemStack is) {
        if(is == null || is.getType() == Material.AIR) {
            return;
        }
        player.getWorld().dropItem(player.getLocation(), is);
    }

    /**
     * 给玩家物品并设置数量
     * @param player 玩家
     * @param is 物品
     * @param amount 数量
     */
    public static void giveItemAndAmount(Player player,ItemStack is,int amount) {
        if(is == null || is.getType() == Material.AIR) {
            return;
        }
        is.setAmount(amount);
        safeAddItem(player, is);
    }

    /**
     * 设置玩家主手中物品数量
     * @param player 玩家
     * @param amount 数量
     */
    public static ItemStack setItemInHandAmount(Player player,int amount) {
        if(player.getInventory().getItemInHand() == null || player.getInventory().getItemInHand().getType() == Material.AIR) {
            return null;
        }
        ItemStack is = player.getInventory().getItemInHand();
        is.setAmount(amount);
        return is;
    }

    /**
     * 清除物品颜色
     *
     * @param lore String
     * @return String
     */
    public static String clearColor(String lore) {
        for (int i = 0; i < 10; i++) {
            lore = ChatColor.stripColor(lore);
        }
        return lore;
    }

    public static String getLore(ItemStack item, int index) {
        for(int i = 0; i < item.getItemMeta().getLore().size(); i++) {
            if(i == index) {
                return item.getItemMeta().getLore().get(i);
            }
        }
        return null;
    }

    public static ItemStack removeLore(ItemStack item, String lore) {
        List<String> list = item.getItemMeta().getLore();
        for(String  lores : list) {
            if(lores.equals(lore)) {
                list.remove(getLoreIndex(list, lores));
                ItemMeta itemMeta = item.getItemMeta();
                itemMeta.setLore(list);
                item.setItemMeta(itemMeta);
            }
        }
        return item;
    }

    public static ItemStack removeLore(ItemStack is,int Line,String lore) {
        List <String> lores = new ArrayList<String>();
        if(is == null || is.getType() == Material.AIR) {
            throw new NullPointerException();
        }
        if(is.getItemMeta().hasLore()) {
            lores.addAll(is.getItemMeta().getLore());
            for(String key : lores) {
                if(lore.equals(key)) {
                    lores.remove(key);
                }
            }
            ItemMeta itemMeta = is.getItemMeta();
            itemMeta.setLore(lores);
            is.setItemMeta(itemMeta);
        }
        return is;
    }

    public static ItemStack removeLore(ItemStack is,int Line) {
        List <String> lores = new ArrayList<String>();
        if(is == null || is.getType() == Material.AIR) {
            throw new NullPointerException();
        }
        if(is.getItemMeta().hasLore()) {
            lores.addAll(is.getItemMeta().getLore());
            lores.remove(Line);
            ItemMeta itemMeta = is.getItemMeta();
            itemMeta.setLore(lores);
            is.setItemMeta(itemMeta);
        }
        return is;
    }
}
