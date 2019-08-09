package org.serverct.sir.duobao.listener;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.duobao.Duobao;
import org.serverct.sir.duobao.enums.MessageType;
import org.serverct.sir.duobao.manager.GameManager;
import org.serverct.sir.duobao.util.AreaUtil;
import org.serverct.sir.duobao.util.CommonUtil;
import org.serverct.sir.duobao.util.LocaleUtil;

public class PlayerInteractListener implements Listener {

    private LocaleUtil locale;
    private Player user;

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        locale = Duobao.getInstance().getLocale();
        user = event.getPlayer();

        if(!event.isBlockInHand()) {
            if(event.hasBlock()) {
                Block block = event.getClickedBlock();
                Location loc = block.getLocation();
                Action action = event.getAction();

                if(action == Action.LEFT_CLICK_BLOCK || action == Action.RIGHT_CLICK_BLOCK) {
                    if(block.getType() == Material.CHEST) {
                        if(GameManager.getInstance().chestFound(loc)) {
                            event.setCancelled(true);
                            CommonUtil.broadcast(locale.getMessage(Duobao.getInstance().getLocaleKey(), MessageType.INFO, "Game", "Found").replace("%player%", user.getName()));
                        }
                    }
                }

                if(event.hasItem()) {
                    if(user.hasPermission("Duobao.select")) {
                        ItemStack tool = event.getItem();
                        if(tool != null && tool.getType() == Material.NETHER_STAR) {
                            event.setCancelled(true);
                            switch (action) {
                                case LEFT_CLICK_BLOCK:
                                    AreaUtil.setPoint(loc, true);
                                    user.sendMessage(locale.buildMessage(Duobao.getInstance().getLocaleKey(), MessageType.INFO, "&7成功设置点 1(&c" + locale.getLocation(loc) +"&7."));
                                    break;
                                case RIGHT_CLICK_BLOCK:
                                    AreaUtil.setPoint(loc, false);
                                    user.sendMessage(locale.buildMessage(Duobao.getInstance().getLocaleKey(), MessageType.INFO, "&7成功设置点 2(&c" + locale.getLocation(loc) + "&7."));
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }

}
