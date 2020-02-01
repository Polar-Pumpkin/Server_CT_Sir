package org.serverct.sir.guajichi.listener;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.guajichi.GuaJiChi;
import org.serverct.sir.guajichi.config.ConfigManager;
import org.serverct.sir.guajichi.utils.LocaleUtil;

public class InteractListener implements Listener {

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        LocaleUtil locale = GuaJiChi.getInstance().getLocale();
        Player user = event.getPlayer();
        if(user.hasPermission("GuaJiChi.setup")) {
            if(!event.isBlockInHand()) {
                if(event.hasBlock()) {
                    Block block = event.getClickedBlock();
                    Location loc = block.getLocation();
                    Action action = event.getAction();
                    if(event.hasItem()) {
                        ItemStack tool = event.getItem();
                        if(tool != null && tool.getType() == ConfigManager.tool) {
                            event.setCancelled(true);
                            switch (action) {
                                case LEFT_CLICK_BLOCK:
                                    ConfigManager.getInstance().setPoint(user, loc, true);
                                    user.sendMessage(locale.buildMessage("Chinese", LocaleUtil.INFO, "&7成功设置点 1(&c" + locale.getLocation(loc) +"&7."));
                                    break;
                                case RIGHT_CLICK_BLOCK:
                                    ConfigManager.getInstance().setPoint(user, loc, false);
                                    user.sendMessage(locale.buildMessage("Chinese", LocaleUtil.INFO, "&7成功设置点 2(&c" + locale.getLocation(loc) + "&7."));
                                    break;
                            }
                        }
                    }
                }
            }
        }
    }
}
