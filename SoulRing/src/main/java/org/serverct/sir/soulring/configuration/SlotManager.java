package org.serverct.sir.soulring.configuration;

import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.serverct.sir.soulring.util.ItemStackUtil;

import java.util.ArrayList;
import java.util.List;

public class SlotManager {

    private static SlotManager slotManager;

    public static SlotManager getSlotManager() {
        if(slotManager == null) {
            slotManager = new SlotManager();
        }
        return slotManager;
    }

    private ItemMeta resultMeta;
    private List<String> targetLore;
    private List<String> resultLore;

    public int getFirstEmptySlot(ItemStack item) {
        int row = 0;
        if(item.getItemMeta().hasLore()) {
            for(String lore : item.getItemMeta().getLore()) {
                if(lore.contains(RingManager.getRingManager().getEmptySlotDisplay())) {
                    return row;
                }
                row++;
            }
        }
        return -1;
    }

    public int countEmptySlot(ItemStack item) {
        int amount = 0;
        for(String lore : item.getItemMeta().getLore()) {
            if(lore.contains(RingManager.getRingManager().getEmptySlotDisplay())) {
                amount++;
            }
        }
        return -1;
    }

    public int countAllSlot(ItemStack item) {
        int amount = 0;
        if(item.hasItemMeta()) {
            for(String lore : item.getItemMeta().getLore()) {
                if(lore.contains(RingManager.getRingManager().getEmptySlotDisplay()) || lore.contains(RingManager.getRingManager().getFilledSlotDisplay())) {
                    amount++;
                }
            }
        }
        return amount;
    }

    public boolean containSlot(ItemStack item) {
        if(item.hasItemMeta()) {
            targetLore = item.getItemMeta().getLore();

            if(targetLore.contains(RingManager.getRingManager().getEmptySlotDisplay())) {
                return true;
            }
            for(String lore : targetLore) {
                if(lore.contains(RingManager.getRingManager().getFilledSlotDisplay())) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getInlayRings(ItemStack item) {
        targetLore = item.getItemMeta().getLore();
        resultLore = new ArrayList<>();

        for(String lore : targetLore) {
            if(lore.contains(RingManager.getRingManager().getFilledSlotDisplay())) {
                resultLore.add(RingManager.getRingManager().getRingKey(lore.replace(RingManager.getRingManager().getFilledSlotDisplay(), "")));
            }
        }
        return resultLore;
    }

    public ItemStack inlay(ItemStack ringItem, ItemStack targetItem) {
        resultMeta = targetItem.getItemMeta();
        resultLore = resultMeta.getLore();

        if(getFirstEmptySlot(targetItem) != -1) {
            if (RingManager.getRingManager().getRingDisplay(ringItem) != null) {
                resultLore.set(getFirstEmptySlot(targetItem), RingManager.getRingManager().getFilledSlotDisplay() + RingManager.getRingManager().getRingDisplay(ringItem));
            }
        }

        resultMeta.setLore(resultLore);
        targetItem.setItemMeta(resultMeta);
        return targetItem;
    }

    public ItemStack punch(ItemStack item) {
        resultMeta = item.getItemMeta();
        resultLore = new ArrayList<>();

        if(resultMeta.hasLore()) {
            resultLore.addAll(resultMeta.getLore());
            if(resultLore.contains(RingManager.getRingManager().getHeader())) {
                resultLore.add(ItemStackUtil.getLoreIndex(resultLore, RingManager.getRingManager().getHeader())+ 1, RingManager.getRingManager().getEmptySlotDisplay());
            } else {
                resultLore.add(RingManager.getRingManager().getHeader());
                resultLore.add(RingManager.getRingManager().getEmptySlotDisplay());
            }
        } else {
            resultLore.add(RingManager.getRingManager().getHeader());
            resultLore.add(RingManager.getRingManager().getEmptySlotDisplay());
        }

        resultMeta.setLore(resultLore);
        item.setItemMeta(resultMeta);
        return item;
    }

    public ItemStack punch(ItemStack item, int index) {
        resultMeta = item.getItemMeta();
        resultLore = new ArrayList<>();

        if(resultMeta.hasLore()) {
            resultLore.addAll(resultMeta.getLore());
            if(resultLore.contains(RingManager.getRingManager().getHeader())) {
                resultLore.add(index - 1, RingManager.getRingManager().getEmptySlotDisplay());
            } else {
                resultLore.add(index - 2, RingManager.getRingManager().getHeader());
                resultLore.add(index - 1, RingManager.getRingManager().getEmptySlotDisplay());
            }
        } else {
            resultLore.add(RingManager.getRingManager().getHeader());
            resultLore.add(RingManager.getRingManager().getEmptySlotDisplay());
        }

        resultMeta.setLore(resultLore);
        item.setItemMeta(resultMeta);
        return item;
    }
}
