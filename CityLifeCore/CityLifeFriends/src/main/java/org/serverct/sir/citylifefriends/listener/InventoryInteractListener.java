package org.serverct.sir.citylifefriends.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.serverct.sir.citylifecore.data.InventoryClick;
import org.serverct.sir.citylifecore.data.InventoryGui;
import org.serverct.sir.citylifecore.data.InventoryItem;
import org.serverct.sir.citylifecore.manager.InventoryManager;
import org.serverct.sir.citylifecore.utils.InventoryUtil;
import org.serverct.sir.citylifecore.utils.LocaleUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;
import org.serverct.sir.citylifefriends.configuration.InventoryConfigManager;
import org.serverct.sir.citylifefriends.configuration.PlayerDataManager;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class InventoryInteractListener implements Listener {

    private LocaleUtil locale = CityLifeFriends.getInstance().getLocale();

    private Inventory clickedInventory;
    private InventoryItem clickedItem;
    private Map<String, String> placeholder;
    private List<InventoryClick> clickList;

    private Player user;

    private InventoryManager inventoryManager = CityLifeFriends.getInstance().getCoreApi().getInventoryAPI();
    private InventoryUtil inventoryUtil = CityLifeFriends.getInstance().getCoreApi().getInventoryUtil();

    private InventoryGui friends;

    @EventHandler
    public void onPlayerClick(InventoryClickEvent event) {
        locale.debug("> 触发 Gui 点击事件.");
        clickedInventory = event.getInventory();
        locale.debug("  > 被点击 Gui 标题: " + clickedInventory.getName());
        user = (Player) event.getWhoClicked();
        locale.debug("  > 点击玩家: " + user.getName());
        friends = InventoryConfigManager.getInstance().applyFriendsToGui(PlayerDataManager.getInstance().getList(user.getName(), true));

        if(clickedInventory.getName().equals(friends.getInventory().getName())) {
            event.setCancelled(true);
            locale.debug("  > 被点击 Gui 为好友主界面.");
            clickedItem = inventoryUtil.getInventoryItem(event.getCurrentItem(), friends.getItems());

            if(clickedItem != null) {
                locale.debug("  > 已获取到有效的被点击物品的 InventoryItem 对象. ID: " + clickedItem.getId());
                clickList = clickedItem.getClicks();
                placeholder = new HashMap<>();
                placeholder.put("friend", clickedItem.getId());
                locale.debug("  > 已更新变量列表: friend -> " + clickedItem.getId());

                if(!clickedItem.isKeepOpen()) {
                    inventoryUtil.closeInventory(event.getWhoClicked(), friends);
                    locale.debug("  > 被点击物品不包含附加选项: 保持打开, 已关闭Gui.");
                }

                locale.debug("  > 开始循环执行 InventoryClick 操作.");
                for(InventoryClick click : clickList) {
                    locale.debug("    > InventoryClick ID: " + click.getId() + ", 触发类型:" + click.getClickType().getType() + ", Action 动作数量: " + click.getActionList().size() + ".");
                    if(click.getClickType().getClickType() == event.getClick()) {
                        locale.debug("    > 触发类型匹配, 开始执行.");
                        click.start(
                                user,
                                new HashMap<String, String>() {
                                    {
                                        put("friend",clickedItem.getId());
                                    }
                                }
                        );
                        break;
                    } else {
                        locale.debug("    > 触发类型不匹配, 跳过执行.");
                    }
                }
            }
        }
    }
}
