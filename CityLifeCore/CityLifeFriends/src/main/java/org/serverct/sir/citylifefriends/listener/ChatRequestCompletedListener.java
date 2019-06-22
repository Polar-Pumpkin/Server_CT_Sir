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

    private InventoryGui gui;
    private InventoryItem item;
    private InventoryClick click;

    private Map<String, String> placeholder;

    @EventHandler
    public void onChatRequestCompleted(ChatRequestCompletedEvent event) {
        if(event.getPluginName().equalsIgnoreCase(CityLifeFriends.getInstance().getName())) {
            user = event.getPlayer();
            chatRequest = event.getChatRequest();

            placeholder.put("value", chatRequest.getValue());

            id = chatRequest.getId().split("_");
            // Plugin_InventoryGui_InventoryItem_InventoryClick
            guiID = id[1] + "_" + id[2];
            if(id[0].equals(CityLifeFriends.getInstance().getName())) {
                if(inventoryManager.getLoadedInventory().containsKey(guiID)) {
                    switch(guiID) {
                        case "FRIENDS_Friends":
                            gui = InventoryConfigManager.getInstance().applyFriendsToGui(PlayerDataManager.getInstance().getList(user.getName(), true));

                            if(gui.containItem(id[3])) {
                                item = gui.getItem(id[3]);

                                if(item.containclick(id[4])) {
                                    click = item.getClick(id[4]);

                                    for(Action action : click.getActionAfterCR()) {
                                        action.cast(
                                                user,
                                                new HashMap<String, String>() {
                                                    {
                                                        put("friend",item.getId());
                                                        put("chatrequest", chatRequest.getValue());
                                                    }
                                                }
                                        );
                                    }
                                }
                            }
                            break;
                        case "FRIENDS_Applications":
                            break;
                        default:
                            break;
                    }
                }
            }
        }
    }
}
