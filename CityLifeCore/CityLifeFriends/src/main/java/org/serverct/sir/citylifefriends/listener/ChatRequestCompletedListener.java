package org.serverct.sir.citylifefriends.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.serverct.sir.citylifecore.data.*;
import org.serverct.sir.citylifecore.event.ChatRequestCompletedEvent;
import org.serverct.sir.citylifecore.manager.InventoryManager;
import org.serverct.sir.citylifecore.utils.LocaleUtil;
import org.serverct.sir.citylifefriends.CityLifeFriends;
import org.serverct.sir.citylifefriends.configuration.InventoryConfigManager;
import org.serverct.sir.citylifefriends.configuration.PlayerDataManager;

import java.util.HashMap;
import java.util.Map;

public class ChatRequestCompletedListener implements Listener {

    private LocaleUtil locale = CityLifeFriends.getInstance().getLocale();

    private InventoryManager inventoryManager = CityLifeFriends.getInstance().getCoreApi().getInventoryAPI();

    private Player user;
    private ChatRequest chatRequest;
    private String[] id;
    private String guiID;
    private String itemID;
    private String clickID;

    private InventoryGui gui;
    private InventoryItem item;
    private InventoryClick click;

    private Map<String, String> placeholder;

    @EventHandler
    public void onChatRequestCompleted(ChatRequestCompletedEvent event) {
        if(event.getPlugin().getName().equalsIgnoreCase(CityLifeFriends.getInstance().getName())) {
            user = event.getPlayer();
            chatRequest = event.getChatRequest();

            placeholder = new HashMap<>();
            placeholder.put("value", chatRequest.getValue());

            id = chatRequest.getId().split("_");
            // Plugin_InventoryGui_InventoryItem.FriendName_InventoryClick
            if(id[0].equals(CityLifeFriends.getInstance().getName())) {
                locale.debug("> 侦测到主管数据请求被完成, 操作玩家: " + user.getName() + ", ChatRequest ID: " + chatRequest.getId() + ".");
                locale.debug("  > 已更新变量列表: value -> " + chatRequest.getValue() + ".");

                guiID = id[0] + "_" + id[1];
                itemID = guiID + "_" + id[2];
                clickID = itemID + "_" + id[3];

                if(inventoryManager.getLoadedInventory().containsKey(guiID)) {
                    locale.debug("  > 查询到 ChatRequest 目标 InventoryGui, ID: " + guiID + ".");
                    switch(id[1]) {
                        case "Friends":
                            gui = InventoryConfigManager.getInstance().applyFriendsToGui(PlayerDataManager.getInstance().getList(user.getName(), true));

                            locale.debug("临时, 从 ChatRequest 中获取到的 ItemID: " + itemID);
                            if(gui.containItem(itemID)) {
                                item = gui.getItem(itemID);
                                locale.debug("  > 在目标 InventoryGui 查询到 ChatRequest 目标 InventoryItem, ID: " + itemID + ".");

                                if(item.containclick(clickID)) {
                                    click = item.getClick(clickID);
                                    locale.debug("  > 在目标 InventoryItem 查询到 ChatRequest 目标 InventoryClick, ID: " + clickID + ".");

                                    locale.debug("  > 开始循环执行后续 Action 动作操作.");
                                    for(Action action : click.getActionAfterCR()) {
                                        action.cast(
                                                user,
                                                new HashMap<String, String>() {
                                                    {
                                                        put("friend", item.getId().split("//.")[1].split("_")[0]);
                                                        put("chatrequest", chatRequest.getValue());
                                                    }
                                                }
                                        );
                                    }
                                }
                            }
                            break;
                        case "Applications":
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
}
