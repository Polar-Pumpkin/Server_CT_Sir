package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.plugin.Plugin;
import org.serverct.sir.citylifecore.enums.inventoryitem.ActionType;
import org.serverct.sir.citylifecore.enums.inventoryitem.ClickType;

import java.util.ArrayList;
import java.util.List;

public @Data @AllArgsConstructor class InventoryClick {

    private String id;
    private Plugin plugin;
    private ClickType clickType;
    private List<Action> actionList;


    public String[] getInfo() {
        return null;
    }

    public boolean containChatRequest() {
        for(Action action : actionList) {
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

    public void start() {

    }
}
