package org.serverct.sir.citylifecore.manager;

import lombok.Getter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.data.CLID;
import org.serverct.sir.citylifecore.data.ChatRequest;
import org.serverct.sir.citylifecore.enums.ChatRequestDataType;
import org.serverct.sir.citylifecore.enums.MessageType;
import org.serverct.sir.citylifecore.utils.LocaleUtil;

import java.util.HashMap;
import java.util.Map;

public class ChatRequestManager {

    private LocaleUtil locale = CityLifeCore.getInstance().getLocale();

    @Getter private Map<Player, ChatRequest> chatRequestMap = new HashMap<>();
    @Getter private Map<Player, ChatRequest> loggedChatRequest = new HashMap<>();

    private ChatRequest chatRequest;

    public boolean registerChatRequest(CLID father, String id, Plugin plugin, ChatRequestDataType dataType, Player player) {
        if(!chatRequestMap.containsKey(player)) {
            chatRequestMap.put(
                    player,
                    new ChatRequest(
                            new CLID(id, father.getFather()),
                            plugin,
                            dataType,
                            player,
                            null)
                    );
            player.sendMessage(
                    locale.getMessage(MessageType.INFO, "ChatRequest", "Waiting")
                            .replace("%plugin%", plugin.getName())
                            .replace("%datatype%", dataType.getType())
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
