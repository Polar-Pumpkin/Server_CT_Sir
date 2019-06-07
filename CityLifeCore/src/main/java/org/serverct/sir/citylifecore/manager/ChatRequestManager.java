package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifecore.data.ChatRequest;

import java.util.HashMap;
import java.util.Map;

public class ChatRequestManager {

    @Getter private Map<Player, ChatRequest> chatRequestMap = new HashMap<>();
    @Getter private Map<Player, ChatRequest> loggedChatRequest = new HashMap<>();

    private ChatRequest chatRequest;

    public boolean registerChatRequest(Player player, String pluginName) {
        if(!chatRequestMap.containsKey(player)) {
            chatRequestMap.put(player, new ChatRequest(pluginName, player, null));
            return true;
        }
        return false;
    }

    public boolean unregisterChatRequest(Player player) {
        if(chatRequestMap.containsKey(player)) {
            chatRequestMap.remove(player);
            return true;
        }
        return false;
    }

    /*public void log(Player player, String message) {
        if(chatRequestMap.containsKey(player)) {
            chatRequest = chatRequestMap.get(player);
            chatRequest.setMessage(message);
            loggedChatRequest.put(player, chatRequest);
        }
    }

    public void removeLog(Player player) {
        if(loggedChatRequest.containsKey(player)) {
            loggedChatRequest.remove(player);
        }
    }*/
}
