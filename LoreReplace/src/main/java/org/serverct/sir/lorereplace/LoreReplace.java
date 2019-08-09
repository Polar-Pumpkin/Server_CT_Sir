package org.serverct.sir.lorereplace;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;

public final class LoreReplace extends JavaPlugin implements CommandExecutor {

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getPluginCommand("lorereplace").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if(sender.hasPermission("LoreReplace.use")) {
            // /lorerep target replace
            if(args.length == 2) {
                int playerAmount = 0;
                int itemAmount = 0;
                int modifyAmount = 0;
                for(Player player : getServer().getOnlinePlayers()) {
                    Inventory userInv = player.getInventory();
                    for(int index = 0; index <= 35; index++) {
                        ItemStack item = userInv.getItem(index);
                        if(item != null && item.getType() != Material.AIR) {
                            if(item.hasItemMeta()) {
                                ItemMeta meta = item.getItemMeta();
                                if(meta.hasLore()) {
                                    List<String> lore = new ArrayList<>(meta.getLore());
                                    lore.replaceAll(s -> s.replaceAll(args[0], args[1]));
                                    meta.setLore(lore);
                                }
                                if(meta.hasDisplayName()) {
                                    String display = meta.getDisplayName().replaceAll(args[0], args[1]);
                                    meta.setDisplayName(display);
                                }
                                item.setItemMeta(meta);
                                userInv.setItem(index, item);
                                modifyAmount++;
                            }
                        }
                        itemAmount++;
                    }
                    playerAmount++;
                }
                sender.sendMessage("扫描了 " + playerAmount + " 个玩家, " + itemAmount + " 个物品, 共修改 " + modifyAmount + " 个物品.");
            } else {
                sender.sendMessage("cnm你个憨批写的什么屎我不认识帮你替换个屁的Lore?");
            }
        } else {
            sender.sendMessage("你个憨批没有权限这么做.");
        }
        return true;
    }
}
