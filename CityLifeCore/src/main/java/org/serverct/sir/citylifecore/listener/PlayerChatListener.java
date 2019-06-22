package org.serverct.sir.citylifecore.listener;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.data.ChatRequest;
import org.serverct.sir.citylifecore.enums.MessageType;
import org.serverct.sir.citylifecore.event.ChatRequestCompletedEvent;
import org.serverct.sir.citylifecore.manager.ChatRequestManager;
import org.serverct.sir.citylifecore.utils.LocaleUtil;

import java.util.Map;

public class PlayerChatListener implements Listener {

    private Player chater;
    private String message;
    private Map<Player, ChatRequest> chatRequestMap;
    private ChatRequest chatRequest;

    private ChatRequestManager chatAPI = CityLifeCore.getAPI().getChatRequestAPI();
    private LocaleUtil locale = CityLifeCore.getInstance().getLocale();

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        chater = event.getPlayer();
        message = event.getMessage();
        chatRequestMap = chatAPI.getChatRequestMap();

        if(chatRequestMap.containsKey(chater)) {
            event.setCancelled(true);
            chatRequest = chatRequestMap.get(chater);

            if(message.equalsIgnoreCase("cancel")) {
                chater.sendMessage(locale.getMessage(MessageType.WARN, "ChatRequest", "Cancelled"));
            } else {
                chatRequest.setValue(message);

                if(chatRequest.validate()) {
                    ChatRequestCompletedEvent chatRequestCompletedEvent = new ChatRequestCompletedEvent(chater, chatRequest);

                    if(chatRequestCompletedEvent.checkConsistency()) {
                        Bukkit.getPluginManager().callEvent(chatRequestCompletedEvent);
                    }

                    chater.sendMessage(locale.getMessage(MessageType.INFO, "ChatRequest", "Success").replace("%value%", message));
                }
            }

            chatAPI.unregisterChatRequest(chater);
        }
    }

}
