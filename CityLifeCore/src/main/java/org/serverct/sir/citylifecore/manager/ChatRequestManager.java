package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.data.ChatRequest;
import org.serverct.sir.citylifecore.enums.ChatRequestDataType;

import java.util.HashMap;
import java.util.Map;

public class ChatRequestManager {

    @Getter private Map<Player, ChatRequest> chatRequestMap = new HashMap<>();
    @Getter private Map<Player, ChatRequest> loggedChatRequest = new HashMap<>();

    private ChatRequest chatRequest;

    public boolean registerChatRequest(String id, Plugin plugin, ChatRequestDataType dataType, Player player) {
        if(!chatRequestMap.containsKey(player)) {
            chatRequestMap.put(
                    player,
                    new ChatRequest(
                            id,
                            plugin,
                            dataType,
                            player,
                            null)
                    );
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
