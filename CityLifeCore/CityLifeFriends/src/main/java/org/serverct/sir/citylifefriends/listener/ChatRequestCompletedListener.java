package org.serverct.sir.citylifefriends.listener;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.serverct.sir.citylifecore.data.Action;
import org.serverct.sir.citylifecore.data.ChatRequest;
import org.serverct.sir.citylifecore.event.ChatRequestCompletedEvent;
import org.serverct.sir.citylifefriends.CityLifeFriends;

import java.util.Map;

public class ChatRequestCompletedListener implements Listener {

    private Player user;
    private ChatRequest chatRequest;

    private Map<String, String> placeholder;

    @EventHandler
    public void onChatRequestCompleted(ChatRequestCompletedEvent event) {
        if(event.getPluginName().equalsIgnoreCase(CityLifeFriends.getINSTANCE().getName())) {
            user = event.getPlayer();
            chatRequest = event.getChatRequest();

            placeholder.put("value", chatRequest.getValue());
            for(Action action : chatRequest.getTodos()) {

            }
        }
    }
}
