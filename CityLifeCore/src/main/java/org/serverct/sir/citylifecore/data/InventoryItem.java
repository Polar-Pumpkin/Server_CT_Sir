package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;
import org.serverct.sir.citylifecore.enums.inventoryitem.ActionType;

import java.util.List;

public @Data @AllArgsConstructor class InventoryItem {

    private String id;

    private ItemStack item;
    private List<Integer> positionList;

    private List<Action> actions;

    private boolean keepOpen;
    private int price;
    private int point;

    public String[] info() {
        String[] infoMsg = {
                "==========[ InventoryItem 菜单物品详细信息 ]==========",
                "  > ID: " + id,
                "  > ItemStack: " + item.toString(),
                "  > 槽位: " + positionList.toString(),
                "  > 附加参数",
                "    > 保持开启: " + (keepOpen ? "是" : "否"),
                "    > 金钱要求: " + price,
                "    > 点卷要求: " + point,
        };
        return infoMsg;
    }

    public boolean hasChatRequestAction() {
        for(Action action : actions) {
            if(action.getActionType() == ActionType.CHATREQUEST) {
                return true;
            }
        }
        return false;
    }

    public int getChatRequestActionIndex() {
        for(int index = 0; index <= actions.size(); index++) {
            if(actions.get(index).getActionType() == ActionType.CHATREQUEST) {
                return index;
            }
        }
        return -1;
    }

}
