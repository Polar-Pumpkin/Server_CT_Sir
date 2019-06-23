package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.CityLifeCore;
import org.serverct.sir.citylifecore.enums.inventoryitem.ActionType;
import org.serverct.sir.citylifecore.enums.inventoryitem.ClickType;
import org.serverct.sir.citylifecore.utils.LocaleUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public @Data @AllArgsConstructor class InventoryClick {

    private CLID id;
    private Plugin plugin;
    private ClickType clickType;
    private Map<CLID, Action> actionList;


    public String[] getInfo() {
        return null;
    }

    public boolean containChatRequest() {
        for(Action action : actionList.values()) {
            if(action.getActionType() == ActionType.CHATREQUEST) {
                return true;
            }
        }
        return false;
    }

    public int getChatRequestIndex() {
        for(int index = 0; index <= actionList.size() - 1; index++) {
            if(actionList.get(index).getActionType() == ActionType.CHATREQUEST) {
                return index;
            }
        }
        return -1;
    }

    public List<Action> getActionAfterCR() {
        List<Action> result = new ArrayList<>();
        if(getChatRequestIndex() < actionList.size() - 1) {
            for(int index = getChatRequestIndex() + 1; index <= actionList.size() - 1; index++) {
                result.add(actionList.get(index));
            }
        }
        return result;
    }

    public void start(Player player, Map<String, String> placeHolder) {
        LocaleUtil locale = CityLifeCore.getAPI().getLocaleManager().getTargetLocaleUtil(plugin);

        locale.debug("      > 开始执行 InventoryClick - " + id);
        if(containChatRequest()) {
            locale.debug("      > 包含 ChatRequest 类型 Action 动作.");
            locale.debug("      > 开始循环执行 Action 动作.");
            for(Action action : actionList.values()) {
                locale.debug("        > Action ID: " + action.getId());
                locale.debug("          > 类型: " + action.getActionType().getType());
                locale.debug("          > 值: " + action.getValue());
                action.cast(player, placeHolder);
                if(action.getActionType() == ActionType.CHATREQUEST) {
                    locale.debug("        > 已执行 ChatRequest 类型 Action 动作, 循环终止, 等待数据输入.");
                    break;
                }
            }
        } else {
            locale.debug("      > 不包含 ChatRequest 类型 Action 动作.");
            locale.debug("      > 开始循环执行 Action 动作.");
            for(Action action : actionList.values()) {
                locale.debug("        > Action ID: " + action.getId());
                locale.debug("          > 类型: " + action.getActionType().getType());
                locale.debug("          > 值: " + action.getValue());
                action.cast(player, placeHolder);
            }
        }
    }
}
