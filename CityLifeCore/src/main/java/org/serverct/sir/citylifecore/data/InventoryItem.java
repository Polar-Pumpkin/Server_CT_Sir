package org.serverct.sir.citylifecore.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.bukkit.inventory.ItemStack;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public @Data @AllArgsConstructor class InventoryItem {

    private CLID id;

    private ItemStack item;
    private List<Integer> positionList;

    private Map<CLID, InventoryClick> clicks;

    private boolean keepOpen;
    private int price;
    private int point;

    public List<String> getInfo(boolean withActionInfo) {
        String[] info = {
                "==========[ InventoryItem 菜单物品详细信息 ]==========",
                "  > ID: " + id,
                "  > ItemStack: " + item.toString(),
                "  > 槽位: " + positionList.toString(),
                "  > 动作数量: " + clicks.size(),
                "  > 附加参数",
                "    > 保持开启: " + (keepOpen ? "是" : "否"),
                "    > 金钱要求: " + price,
                "    > 点卷要求: " + point,
        };
        List<String> infoMsg = new ArrayList<>(Arrays.asList(info));
        if(withActionInfo) {
            for(InventoryClick action : clicks.values()) {
                infoMsg.addAll(Arrays.asList(action.getInfo()));
            }
        }
        return infoMsg;
    }

    public boolean containclick(String id) {
        for(InventoryClick click : clicks.values()) {
            if(click.getId().equals(id)) {
                return true;
            }
        }
        return false;
    }

    public InventoryClick getClick(String id) {
        if(containclick(id)) {
            for(InventoryClick click : clicks.values()) {
                if(click.getId().equals(id)) {
                    return click;
                }
            }
        }
        return null;
    }
}
