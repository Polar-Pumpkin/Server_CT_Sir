package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifecore.data.Action;
import org.serverct.sir.citylifecore.data.ChatRequest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChatRequestManager {

    @Getter private Map<Player, ChatRequest> chatRequestMap = new HashMap<>();
    @Getter private Map<Player, ChatRequest> loggedChatRequest = new HashMap<>();

    private ChatRequest chatRequest;

    public boolean registerChatRequest(String pluginName, Player player, List<Action> todos) {
        if(!chatRequestMap.containsKey(player)) {
            chatRequestMap.put(player, new ChatRequest(pluginName, player, null, todos));
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
}
