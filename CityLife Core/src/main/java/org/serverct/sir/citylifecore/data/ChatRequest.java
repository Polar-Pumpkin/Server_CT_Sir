package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;

public @Data @AllArgsConstructor class ChatRequest {

    private String pluginName;
    private Player player;
    private String message;

}
