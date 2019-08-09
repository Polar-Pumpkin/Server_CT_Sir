package org.serverct.sir.hungu.command.subcommands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.hungu.Hungu;
import org.serverct.sir.hungu.command.Subcommand;
import org.serverct.sir.hungu.configuration.HunguManager;
import org.serverct.sir.hungu.data.HunguData;
import org.serverct.sir.hungu.enums.MessageType;
import org.serverct.sir.hungu.utils.HunguUtil;
import org.serverct.sir.hungu.utils.LocaleUtil;

public class Unload implements Subcommand {

    private Player user;
    private Inventory playerInv;
    private LocaleUtil locale = Hungu.getInstance().getLocale();
    private HunguManager hunguManager;

    private HunguData target;
    private ItemStack targetItem;
    private ItemStack itemInHand;
    private String handItemDisplay;
    private String handItemType;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        locale.debug("准备拆卸魂骨");
        if(sender instanceof Player) {
            locale.debug("命令发送者为玩家对象.");
            hunguManager = Hungu.getInstance().getHunguManager();
            locale.debug("获取魂骨数据管理器成功.");
            user = (Player) sender;
            locale.debug("命令发送者: (玩家)" + user.getName());
            playerInv = user.getInventory();
            locale.debug("获取玩家背包成功");

            itemInHand = user.getItemInHand();
            locale.debug("获取目标装备成功.");

            if(user.hasPermission("Hungu.chai")) {
                locale.debug("玩家拥有所需权限.");
                if(itemInHand != null && itemInHand.getType() != Material.AIR) {
                    locale.debug("目标装备有效.");
                    handItemDisplay = itemInHand.getItemMeta().getDisplayName();
                    handItemType = itemInHand.getType().toString();
                    locale.debug("获取目标装备数据有效.");

                    if(hunguManager.getHunguList().containsKey(args[1])) {
                        locale.debug("目标魂骨已加载.");
                        target = hunguManager.getHunguList().get(args[1]);
                        locale.debug("获取目标魂骨数据成功.");

                        if(HunguUtil.getInstance().hasHungu(itemInHand.getItemMeta().getLore()).contains(target)) {
                            locale.debug("目标装备上镶嵌有目标魂骨.");
                            user.setItemInHand(HunguUtil.getInstance().unload(itemInHand, target));
                            locale.debug("已发送拆卸后物品.");
                            targetItem = target.getItem();
                            targetItem.setAmount(1);
                            playerInv.addItem(targetItem);
                            locale.debug("已返还目标魂骨.");

                            user.sendMessage(
                                    locale.getMessage(MessageType.INFO, "Command", "Success.Unload.Message")
                                            .replace("%hungu%", target.getItem().getItemMeta().getDisplayName())
                            );
                            for(Player player : HunguUtil.getInstance().getOnlinePlayers()) {
                                player.sendMessage(
                                        locale.getMessage(MessageType.INFO, "Command", "Success.Unload.Broadcast")
                                                .replace("%player%", user.getName())
                                                .replace("%item%", handItemDisplay == null ? handItemType : handItemDisplay)
                                                .replace("%hungu%", target.getItem().getItemMeta().getDisplayName())
                                );
                            }
                        } else {
                            locale.debug("目标装备上未镶嵌目标魂骨, 拆卸请求驳回.");
                            user.sendMessage(locale.getMessage(MessageType.WARN, "Command", "Invalid.Item.Empty"));
                        }
                    } else {
                        locale.debug("目标魂骨未加载或无效, 拆卸请求驳回.");
                        user.sendMessage(locale.getMessage(MessageType.WARN, "Command", "Invalid.Hungu.InvalidID"));
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
