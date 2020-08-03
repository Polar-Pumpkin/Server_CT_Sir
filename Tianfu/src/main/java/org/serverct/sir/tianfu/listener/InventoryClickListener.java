package org.serverct.sir.tianfu.listener;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.config.GuiManager;
import org.serverct.sir.tianfu.config.PlayerDataManager;
import org.serverct.sir.tianfu.config.TalentManager;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.data.Talent;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.hooks.PlayerPointsHook;
import org.serverct.sir.tianfu.hooks.VaultHook;
import org.serverct.sir.tianfu.util.CommonUtil;
import org.serverct.sir.tianfu.util.LocaleUtil;
import org.serverct.sir.tianfu.util.PlaceholderUtil;

import java.util.ArrayList;
import java.util.List;

public class InventoryClickListener implements Listener {

    private final LocaleUtil locale = Tianfu.getInstance().getLocale();

    private Player user;
    private Inventory inventory;
    private Talent target;
    private PlayerData data;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        user = ((Player) event.getWhoClicked()).getPlayer();
        data = PlayerDataManager.getInstance().getPlayerData(user.getName());
        inventory = event.getInventory();

        if(inventory.getName().equalsIgnoreCase(ChatColor.translateAlternateColorCodes('&', "&6&l天赋系统"))) {
            event.setCancelled(true);

            if(event.getCurrentItem() != null && event.getCurrentItem().getType() != Material.AIR) {
                TalentType talentType = GuiManager.getInstance().getTalentByDisplay(event.getCurrentItem().getItemMeta().getDisplayName(), data);

                if(talentType != null) {
                    target = TalentManager.getInstance().getTalent(talentType);

                    if (data.getLevel().get(talentType) < target.getLevels().size() - 1) {
                        double money = target.getMoney();
                        int point = target.getPoint();

                        if(CommonUtil.checkMoney(user, target) && CommonUtil.checkPoint(user, target)) {

                            if(money > 0) {
                                VaultHook.getInstance().take(user, money);
                            }

                            if(point > 0) {
                                PlayerPointsHook.getInstance().take(user, point);
                            }

                            user.sendMessage(
                                    PlaceholderUtil.check(
                                            locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Command", "Cost.Success"),
                                            target.getType(),
                                            PlayerDataManager.getInstance().getPlayerData(user.getName())
                                    )
                            );

                            PlayerDataManager.getInstance().addPlayerData(user.getName(), talentType, 1);
                            data = PlayerDataManager.getInstance().getPlayerData(user.getName());

                            CommonUtil.closeInventory(user, null);
                            CommonUtil.openInventory(user, GuiManager.getInstance().init(data), null);

                            Sound upgradeSuccess = Sound.valueOf(Tianfu.getInstance().getConfig().getString("Setting.Sound.Upgrade.Success").toUpperCase());
                            user.playSound(user.getLocation(), upgradeSuccess, 1, 1);

                            List<Player> onlines = new ArrayList<>(Tianfu.getInstance().getServer().getOnlinePlayers());
                            onlines.remove(user);
                            for(Player player : onlines) {
                                player.sendMessage(
                                        PlaceholderUtil.check(
                                                locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.INFO, "Command", "Upgrade.Broadcast"),
                                                talentType,
                                                data
                                        )
                                );
                            }
                            return;
                        } else {
                            user.sendMessage(
                                    PlaceholderUtil.check(
                                            locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.WARN, "Command", "Cost.NotEnough"),
                                            talentType,
                                            PlayerDataManager.getInstance().getPlayerData(user.getName())
                                    )
                            );
                        }
                    } else {
                        user.sendMessage(
                                PlaceholderUtil.check(
                                        locale.getMessage(Tianfu.getInstance().getLocaleKey(), MessageType.ERROR, "Command", "Upgrade.ReachedLimit"),
                                        talentType,
                                        data
                                )
                        );
                    }
                }
            }
            Sound upgradeFailure = Sound.valueOf(Tianfu.getInstance().getConfig().getString("Setting.Sound.Upgrade.Failure").toUpperCase());
            user.playSound(user.getLocation(), upgradeFailure, 1, 1);
        }
    }

}
