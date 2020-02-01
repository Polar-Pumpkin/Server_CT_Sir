package org.serverct.sir.hunhuan.command.subcommands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.hunhuan.HunHuan;
import org.serverct.sir.hunhuan.command.Subcommand;
import org.serverct.sir.hunhuan.configuration.HunhuanManager;
import org.serverct.sir.hunhuan.data.HunhuanData;
import org.serverct.sir.hunhuan.enums.MessageType;
import org.serverct.sir.hunhuan.utils.HunhuanUtil;
import org.serverct.sir.hunhuan.utils.LocaleUtil;

import java.util.List;

public class Unload implements Subcommand {

    private LocaleUtil locale = HunHuan.getInstance().getLocale();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        locale.debug("准备拆卸魂骨");
        if(sender instanceof Player) {
            locale.debug("命令发送者为玩家对象.");
            HunhuanManager hunhuanManager = HunHuan.getInstance().getHunhuanManager();
            locale.debug("获取魂骨数据管理器成功.");
            Player user = (Player) sender;
            locale.debug("命令发送者: (玩家)" + user.getName());
            Inventory playerInv = user.getInventory();
            locale.debug("获取玩家背包成功");

            ItemStack itemInHand = user.getItemInHand();
            locale.debug("获取目标装备成功.");

            if(user.hasPermission("Hungu.chai")) {
                locale.debug("玩家拥有所需权限.");
                if(itemInHand != null && itemInHand.getType() != Material.AIR) {
                    locale.debug("目标装备有效.");
                    String handItemDisplay = itemInHand.getItemMeta().getDisplayName();
                    String handItemType = itemInHand.getType().toString();
                    locale.debug("获取目标装备数据有效.");

                    List<HunhuanData> hunhuans = HunhuanUtil.getInstance().hasHungu(itemInHand.getItemMeta().getLore());
                    if(!hunhuans.isEmpty()) {
                        locale.debug("目标装备上镶嵌有魂骨.");
                        HunhuanData target = hunhuans.get(0);
                        user.setItemInHand(HunhuanUtil.getInstance().unload(itemInHand, target));
                        locale.debug("已发送拆卸后物品.");
                        ItemStack targetItem = target.getItem();
                        targetItem.setAmount(1);
                        playerInv.addItem(targetItem);
                        locale.debug("已返还目标魂骨.");

                        user.sendMessage(
                                locale.getMessage(MessageType.INFO, "Command", "Success.Unload.Message")
                                        .replace("%hunhuan%", target.getItem().getItemMeta().getDisplayName())
                        );
                        for(Player player : HunhuanUtil.getInstance().getOnlinePlayers()) {
                            player.sendMessage(
                                    locale.getMessage(MessageType.INFO, "Command", "Success.Unload.Broadcast")
                                            .replace("%player%", user.getName())
                                            .replace("%item%", handItemDisplay == null ? handItemType : handItemDisplay)
                                            .replace("%hunhuan%", target.getItem().getItemMeta().getDisplayName())
                            );
                        }
                    } else {
                        locale.debug("目标装备上未镶嵌目标魂骨, 拆卸请求驳回.");
                        user.sendMessage(locale.getMessage(MessageType.WARN, "Command", "Invalid.Item.Empty"));
                    }
                } else {
                    locale.debug("目标装备无效, 拆卸请求驳回.");
                    user.sendMessage(locale.getMessage(MessageType.WARN, "Command", "Invalid.Item.NothingInHand"));
                }
            } else {
                locale.debug("玩家未拥有所需权限, 拆卸请求驳回.");
                user.sendMessage(locale.getMessage(MessageType.ERROR, "Plugin", "NoPermission"));
            }
        } else {
            locale.debug("命令发送者为非玩家对象, 拆卸请求驳回.");
            sender.sendMessage(locale.getMessage(MessageType.ERROR, "Plugin", "NotPlayer"));
        }
        return true;
    }
}
