package org.serverct.sir.hungu.command.subcommands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.hungu.Hungu;
import org.serverct.sir.hungu.command.Subcommand;
import org.serverct.sir.hungu.configuration.HunguManager;
import org.serverct.sir.hungu.data.HunguData;
import org.serverct.sir.hungu.enums.MessageType;
import org.serverct.sir.hungu.hooks.VaultHook;
import org.serverct.sir.hungu.utils.HunguUtil;
import org.serverct.sir.hungu.utils.LocaleUtil;

import java.util.List;

public class Inlay implements Subcommand {

    private LocaleUtil locale = Hungu.getInstance().getLocale();

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        locale.debug("准备镶嵌魂骨.");
        if(sender instanceof Player) {
            locale.debug("命令发送者为玩家对象.");
            HunguManager hunguManager = Hungu.getInstance().getHunguManager();
            locale.debug("获取魂骨数据管理器成功.");
            Player user = (Player) sender;
            locale.debug("命令发送者: (玩家)" + user.getName());
            Inventory playerInv = user.getInventory();
            locale.debug("获取玩家背包成功");

            ItemStack itemInHand = user.getItemInHand();
            locale.debug("获取目标装备成功.");
            ItemStack targetHunguItem = playerInv.getItem(8);
            locale.debug("获取魂骨位物品成功.");

            if((targetHunguItem != null && targetHunguItem.getType() != Material.AIR) && hunguManager.isHungu(targetHunguItem)) {
                locale.debug("魂骨位物品有效.");
                HunguData target = hunguManager.getHungu(targetHunguItem);
                locale.debug("获取目标魂骨数据成功.");

                if(itemInHand != null && itemInHand.getType() != Material.AIR) {

                    if(hunguManager.isHungu(itemInHand)) {
                        user.sendMessage(locale.getMessage(MessageType.WARN, "Command", "Invalid.Item.NoHungu"));
                        return true;
                    }

                    locale.debug("目标装备有效.");
                    ItemMeta handItemMeta = itemInHand.getItemMeta();
                    String handItemDisplay = handItemMeta.getDisplayName();
                    String handItemType = itemInHand.getType().toString();
                    locale.debug("获取目标装备数据有效.");

                    if(itemInHand.getAmount() == 1) {
                        locale.debug("目标装备数量为 1.");
                        if(handItemMeta.hasLore()) {
                            locale.debug("目标装备包含 Lore.");
                            List<String> handItemLore = handItemMeta.getLore();
                            locale.debug("获取目标装备 Lore 数据成功.");

                            if(handItemLore != null && !handItemLore.isEmpty()) {
                                locale.debug("目标装备 Lore 数据有效.");
                                List<HunguData> handItemHungu = HunguUtil.getInstance().hasHungu(handItemLore);
                                locale.debug("获取目标装备 Lore 中的已镶嵌魂骨成功.");
                                locale.debug("已镶嵌魂骨: " + handItemHungu.toString());

                                if(handItemHungu.size() >= Hungu.getInstance().getAbsorbLimit()) {
                                    locale.debug("目标装备已镶嵌魂骨数量达到限制, 镶嵌请求驳回.");
                                    user.sendMessage(
                                            locale.getMessage(MessageType.WARN, "Command", "Invalid.Item.ReachLimit")
                                                    .replace("%limit%", String.valueOf(Hungu.getInstance().getAbsorbLimit()))
                                    );
                                    return true;
                                } else {
                                    locale.debug("目标装备已镶嵌魂骨数量未达到限制.");
                                }
                            } else {
                                locale.debug("目标装备 Lore 数据无效.");
                            }
                        } else {
                            locale.debug("目标装备不包含 Lore.");
                        }

                        if(VaultHook.getInstance().getBalances(user) >= Hungu.getInstance().getAbsorbCost()) {
                            locale.debug("玩家余额足以支付一次镶嵌.");
                            user.setItemInHand(HunguUtil.getInstance().inlay(itemInHand, target));
                            locale.debug("已发送镶嵌后物品.");
                            VaultHook.getInstance().take(user, Hungu.getInstance().getAbsorbCost());
                            locale.debug("扣费成功.");

                            if(targetHunguItem.getAmount() > 1) {
                                locale.debug("魂骨位物品数量大于 1.");
                                targetHunguItem.setAmount(targetHunguItem.getAmount() - 1);
                                playerInv.setItem(8, targetHunguItem);
                                locale.debug("已扣除魂骨位物品, 并返还多余物品.");
                            } else {
                                playerInv.setItem(8, new ItemStack(Material.AIR));
                                locale.debug("已扣除魂骨位物品");
                            }

                            user.sendMessage(
                                    locale.getMessage(MessageType.INFO, "Command", "Cost.Success")
                                            .replace("%money%", String.valueOf(VaultHook.getInstance().getBalances(user)))
                                            .replace("%cost%", String.valueOf(Hungu.getInstance().getAbsorbCost()))
                            );
                            user.sendMessage(
                                    locale.getMessage(MessageType.INFO, "Command", "Success.Inlay.Message")
                                            .replace("%hungu%", targetHunguItem.getItemMeta().getDisplayName())
                            );
                            for(Player player : HunguUtil.getInstance().getOnlinePlayers()) {
                                player.sendMessage(
                                        locale.getMessage(MessageType.INFO, "Command", "Success.Inlay.Broadcast")
                                                .replace("%player%", user.getName())
                                                .replace("%item%", handItemDisplay == null ? handItemType : handItemDisplay)
                                                .replace("%hungu%", targetHunguItem.getItemMeta().getDisplayName())
                                );
                            }
                        } else {
                            locale.debug("玩家余额不足以支付一次镶嵌, 镶嵌请求驳回.");
                            user.sendMessage(
                                    locale.getMessage(MessageType.INFO, "Command", "Cost.NotEnough")
                                            .replace("%money%", String.valueOf(VaultHook.getInstance().getBalances(user)))
                                            .replace("%cost%", String.valueOf(Hungu.getInstance().getAbsorbCost()))
                            );
                        }
                    } else {
                        locale.debug("目标装备数量大于 1, 镶嵌请求驳回.");
                        user.sendMessage(locale.getMessage(MessageType.WARN, "Command", "Invalid.Item.Amount"));
                    }
                } else {
                    locale.debug("目标装备无效, 镶嵌请求驳回.");
                    user.sendMessage(locale.getMessage(MessageType.WARN, "Command", "Invalid.Item.NothingInHand"));
                }
            } else {
                locale.debug("魂骨位物品无效, 镶嵌请求驳回.");
                user.sendMessage(locale.getMessage(MessageType.WARN, "Command", "Invalid.Hungu.Empty"));
            }
        } else {
            locale.debug("命令发送者为非玩家对象, 镶嵌请求驳回.");
            sender.sendMessage(locale.getMessage(MessageType.ERROR, "Plugin", "NotPlayer"));
        }
        return true;
    }
}
