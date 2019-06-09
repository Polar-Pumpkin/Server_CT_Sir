package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.configuration.LanguageData;
import org.serverct.sir.citylifecore.enums.MessageType;
import org.serverct.sir.citylifecore.enums.inventoryitem.ActionType;
import org.serverct.sir.citylifecore.enums.inventoryitem.ClickType;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public @Data @AllArgsConstructor class InventoryItemAction {

    private String id;
    private ClickType triggerMode;
    private ActionType actionType;
    private String value;

    public List<String> getInfo() {
        List<String> info = new ArrayList<>();
        info.add(actionType.getType() + ": " + ChatColor.translateAlternateColorCodes('&', value));
        return info;
    }

    public void cast(Player player, Map<String, String> placeholder) {
        for(String key : placeholder.keySet()) {
            value.replace("%" + key + "%", placeholder.get(key));
        }

        switch (actionType) {
            case COMMAND:
                player.performCommand(value);
                break;
            case MESSAGE:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', value));
                break;
            case FORMATEDMSG:
                player.sendMessage(LanguageData.getInstance().buildMessage(MessageType.valueOf(value.split("//.")[0].toUpperCase()), value.split("//.")[1]));
                break;
            case CHATREQUEST:
                CityLifeCore.getAPI().getChatRequestAPI().registerChatRequest(player, CityLifeCore.getInstance().getName());
                break;
            default:
                break;
        }
    }

    public boolean check(org.bukkit.event.inventory.ClickType clickType, Player player) {
        return clickType == triggerMode.getClickType();
    }

}
