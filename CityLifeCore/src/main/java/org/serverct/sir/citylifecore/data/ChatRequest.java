package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;
import org.serverct.sir.citylifecore.enums.ChatRequestDataType;
import org.serverct.sir.citylifecore.utils.CommonUtil;

import java.util.List;

public @Data @AllArgsConstructor class ChatRequest {

    private String pluginName;
    private ChatRequestDataType type;
    private Player player;
    private String value;
    private List<Action> todos;

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
