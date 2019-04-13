package org.serverct.sir.anohanamarry.inventory;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class MenuItem {

    @Getter private ItemStack item;
    @Getter private int position;
    @Getter private List<Integer> positionList;
    @Getter private String command;
    @Getter private boolean keepOpen;
    @Getter private int price;

    public MenuItem(ItemStack item, int position, String command, boolean keepOpen) {
        this.item = item;
        this.position = position;
        this.command = command;
        this.keepOpen = keepOpen;
    }

    public MenuItem(ItemStack item, List<Integer> positionList, String command, boolean keepOpen, int price) {
        this.item = item;
        this.positionList = positionList;
        this.command = command;
        this.keepOpen = keepOpen;
        this.price = price;
    }

    public void info() {
        Bukkit.getLogger().info("ItemStack: " + this.item.toString());
        Bukkit.getLogger().info("PositionList: " + this.positionList.toString());
        Bukkit.getLogger().info("Command: " + this.command);
        Bukkit.getLogger().info("KeepOpen: " + this.keepOpen);
        Bukkit.getLogger().info("Price: " + this.price);
    }
}
