package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.enums.ChatRequestDataType;
import org.serverct.sir.citylifecore.enums.MessageType;
import org.serverct.sir.citylifecore.enums.inventoryitem.ActionType;

import java.util.Map;

public @Data @AllArgsConstructor class Action {

    private String id;
    private Plugin plugin;
    private ActionType actionType;
    private String value;

    public String[] getInfo() {
        return new String[]{
                "==========[ Action 动作详细信息 ]==========",
                "  > 主管插件: " + plugin.getName(),
                "  > ID: " + id,
                "  > 操作详细信息: ",
                "    > 类型: " + actionType.getType(),
                "    > 值: " + value
        };
    }

    public void cast(Player player, Map<String, String> placeholder) {
        replaceVariable(placeholder);

        switch (actionType) {
            case COMMAND:
                player.performCommand(value);
                break;
            case MESSAGE:
                player.sendMessage(ChatColor.translateAlternateColorCodes('&', value));
                break;
            case FORMATEDMSG:
                player.sendMessage(CityLifeCore.getAPI().getLocaleManager().getTargetLocaleUtil(plugin).buildMessage(MessageType.valueOf(value.split("//.")[0].toUpperCase()), value.split("//.")[1]));
                break;
            case CHATREQUEST:
                CityLifeCore.getAPI().getChatRequestAPI().registerChatRequest(id, CityLifeCore.getInstance(), ChatRequestDataType.valueOf(value.toUpperCase()), player);
                break;
            default:
                break;
        }
    }

    public void replaceVariable(Map<String, String> placeholder) {
        for(String key : placeholder.keySet()) {
            value.replace("%" + key + "%", placeholder.get(key));
        }
    }
}
