package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.enums.ChatRequestDataType;
import org.serverct.sir.citylifecore.utils.CommonUtil;

public @Data @AllArgsConstructor class ChatRequest {

    private String id;
    private Plugin plugin;
    private ChatRequestDataType type;
    private Player player;
    private String value;

    public boolean validate() {
        if(value != null && value.equalsIgnoreCase("")) {
            switch (type) {
                case INT:
                    return CommonUtil.isInteger(value);
                case STRING:
                    return true;
                case CHINESE:
                    return CommonUtil.isChinese(value);
                default:
                    return false;
            }
        }
        return false;
    }

}
