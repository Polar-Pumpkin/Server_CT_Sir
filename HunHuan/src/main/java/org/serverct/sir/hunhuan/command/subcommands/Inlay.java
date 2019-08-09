package org.serverct.sir.hunhuan.command.subcommands;

import org.bukkit.Material;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.hunhuan.HunHuan;
import org.serverct.sir.hunhuan.command.Subcommand;
import org.serverct.sir.hunhuan.configuration.HunhuanManager;
import org.serverct.sir.hunhuan.data.HunhuanData;
import org.serverct.sir.hunhuan.enums.MessageType;
import org.serverct.sir.hunhuan.hooks.VaultHook;
import org.serverct.sir.hunhuan.utils.HunhuanUtil;
import org.serverct.sir.hunhuan.utils.LocaleUtil;

import java.util.List;

public class Inlay implements Subcommand {

    private Player user;
    private Inventory playerInv;
    private LocaleUtil locale = HunHuan.getInstance().getLocale();
    private HunhuanManager hunhuanManager;

    private HunhuanData target;
    private ItemStack targetHunguItem;
    private ItemStack itemInHand;
    private ItemMeta handItemMeta;
    private String handItemDisplay;
    private String handItemType;
    private List<String> handItemLore;
    private List<HunhuanData> handItemHungu;

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        locale.debug("准备镶嵌魂骨.");
        if(sender instanceof Player) {
            locale.debug("命令发送者为玩家对象.");
            hunhuanManager = HunHuan.getInstance().getHunhuanManager();
            locale.debug("获取魂骨数据管理器成功.");
            user = (Player) sender;
            locale.debug("命令发送者: (玩家)" + user.getName());
            playerInv = user.getInventory();
            locale.debug("获取玩家背包成功");

            itemInHand = user.getItemInHand();
            locale.debug("获取目标装备成功.");
            targetHunguItem = playerInv.getItem(8);
            locale.debug("获取魂骨位物品成功.");

            if((targetHunguItem != null && targetHunguItem.getType() != Material.AIR) && hunhuanManager.isHungu(targetHunguItem)) {
                locale.debug("魂骨位物品有效.");
                target = hunhuanManager.getHungu(targetHunguItem);
                locale.debug("获取目标魂骨数据成功.");

                if(itemInHand != null && itemInHand.getType() != Material.AIR) {
                    locale.debug("目标装备有效.");
                    handItemMeta = itemInHand.getItemMeta();
                    handItemDisplay = handItemMeta.getDisplayName();
                    handItemType = itemInHand.getType().toString();
                    locale.debug("获取目标装备数据有效.");

                    if(itemInHand.getAmount() == 1) {
                        locale.debug("目标装备数量为 1.");
                        if(handItemMeta.hasLore()) {
                            locale.debug("目标装备包含 Lore.");
                            handItemLore = handItemMeta.getLore();
                            locale.debug("获取目标装备 Lore 数据成功.");

                            if(handItemLore != null && !handItemLore.isEmpty()) {
                                locale.debug("目标装备 Lore 数据有效.");
                                handItemHungu = HunhuanUtil.getInstance().hasHungu(handItemLore);
                                locale.debug("获取目标装备 Lore 中的已镶嵌魂骨成功.");
                                locale.debug("已镶嵌魂骨: " + handItemHungu.toString());

                                if(handItemHungu.size() >= HunHuan.getInstance().getAbsorbLimit()) {
                                    locale.debug("目标装备已镶嵌魂骨数量达到限制, 镶嵌请求驳回.");
                                    user.sendMessage(
                                            locale.getMessage(MessageType.WARN, "Command", "Invalid.Item.ReachLimit")
                                                    .replace("%limit%", String.valueOf(HunHuan.getInstance().getAbsorbLimit()))
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

                        if(VaultHook.getInstance().getBalances(user) >= HunHuan.getInstance().getAbsorbCost()) {
                            locale.debug("玩家余额足以支付一次镶嵌.");
                            user.setItemInHand(HunhuanUtil.getInstance().inlay(itemInHand, target));
                            locale.debug("已发送镶嵌后物品.");
                            VaultHook.getInstance().take(user, HunHuan.getInstance().getAbsorbCost());
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
                                            .replace("%cost%", String.valueOf(HunHuan.getInstance().getAbsorbCost()))
                            );
                            user.sendMessage(
                                    locale.getMessage(MessageType.INFO, "Command", "Success.Inlay.Message")
                                            .replace("%hunhuan%", targetHunguItem.getItemMeta().getDisplayName())
                            );
                            for(Player player : HunhuanUtil.getInstance().getOnlinePlayers()) {
                                player.sendMessage(
                                        locale.getMessage(MessageType.INFO, "Command", "Success.Inlay.Broadcast")
                                                .replace("%player%", user.getName())
                                                .replace("%item%", handItemDisplay == null ? handItemType : handItemDisplay)
                                                .replace("%hunhuan%", targetHunguItem.getItemMeta().getDisplayName())
                                );
                            }
                        } else {
                            locale.debug("玩家余额不足以支付一次镶嵌, 镶嵌请求驳回.");
                            user.sendMessage(
                                    locale.getMessage(MessageType.INFO, "Command", "Cost.NotEnough")
                                            .replace("%money%", String.valueOf(VaultHook.getInstance().getBalances(user)))
                                            .replace("%cost%", String.valueOf(HunHuan.getInstance().getAbsorbCost()))
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
