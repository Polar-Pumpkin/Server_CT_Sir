package org.serverct.sir.anohanamarry.inventory;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.HumanEntity;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.configuration.ItemData;
import org.serverct.sir.anohanamarry.util.BasicUtils;

import java.io.File;
import java.util.*;

public class InventoryManager {

    private static InventoryManager inventoryManager;

    public static InventoryManager getInstance() {
        if(inventoryManager == null) {
            inventoryManager = new InventoryManager();
        }
        return inventoryManager;
    }

    @Getter private Map<String, FileConfiguration> guiInventoryMap = new HashMap<>();

    private File guiFolder = new File(ANOHANAMarry.getINSTANCE().getDataFolder() + File.separator + "Guis");

    private List<String> resolutionPositionList;
    private List<Integer> resultPositionList;

    private Set<String> targetKeyList;
    private ItemStack targetItem;
    private List<Integer> targetPositionList;
    private String targetCommand;
    private boolean targetKeepOpen;
    private int targetPrice;

    private File[] guiDataFiles;

    private BukkitRunnable invTask;

    public void loadGuiData() {
        if(!guiFolder.exists()) {
            ANOHANAMarry.getINSTANCE().saveResource("Guis" + File.separator, false);
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7未找到Gui配置文件夹, 已自动生成."));
        }

        guiDataFiles = guiFolder.listFiles(pathname -> {
            String fileName = pathname.getName();
            return fileName.endsWith(".yml");
        });
        if(guiDataFiles == null || guiDataFiles.length == 0) {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &c> &7无Gui配置文件可供加载, 请注意删除Gui配置文件可能导致严重错误."));
            ANOHANAMarry.getINSTANCE().saveResource("Guis" + File.separator + "SexSelector.yml", false);
            ANOHANAMarry.getINSTANCE().saveResource("Guis" + File.separator + "LoveShop.yml", false);
            ANOHANAMarry.getINSTANCE().saveResource("Guis" + File.separator + "MainMenu.yml", false);
            ANOHANAMarry.getINSTANCE().saveResource("Guis" + File.separator + "ShareSetting.yml", false);
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7已自动生成Gui配置文件."));

            guiDataFiles = guiFolder.listFiles(pathname -> {
                String fileName = pathname.getName();
                return fileName.endsWith(".yml");
            });
        }

        if(guiDataFiles != null) {
            int dataFileAmount = 0;
            for(File guiDataFile : guiDataFiles) {
                guiInventoryMap.put(BasicUtils.getFileNameNoEx(guiDataFile.getName()), YamlConfiguration.loadConfiguration(guiDataFile));
                // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7已加载Gui配置: &c" + guiDataFile.getName() + "&7."));
                dataFileAmount ++;
            }
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7共加载 &c" + String.valueOf(dataFileAmount) + " &7个Gui配置文件."));
        } else {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &c> &7生成后依然无Gui配置文件可供加载, 此错误出现时请手动导入Gui配置文件并重启服务器."));
        }
    }

    public List<Integer> resolutionLocation(String x, String y) {
        resultPositionList = new ArrayList<>();

        for(String yPosition : resolutionPosition(y)) {
            for(String xPosition : resolutionPosition(x)) {
                resultPositionList.add(Position.getPositon(Position.valueOf("x" + xPosition), Position.valueOf("y" + yPosition)));
            }
        }

        System.out.println(resultPositionList);
        return resultPositionList;
    }

    private List<String> resolutionPosition(String positionString) {
        resolutionPositionList = new ArrayList<>();

        if(positionString.contains("-")) {
            for(int i = Integer.valueOf(positionString.split("-")[0]); i <= Integer.valueOf(positionString.split("-")[1]); i++) {
                resolutionPositionList.add(String.valueOf(i));
            }
        } else if(positionString.contains(",")) {
            for(String position : positionString.split(",")) {
                resolutionPositionList.add(String.valueOf(position));
            }
        } else {
            resolutionPositionList.add(positionString);
        }

        return resolutionPositionList;
    }

    public MenuItem buildMenuItem(ConfigurationSection section) {
        targetKeyList = section.getKeys(false);
        targetItem = ItemData.getInstance().buildItem(section);
        targetPositionList = resolutionLocation(section.getString("Position-X"), section.getString("Position-Y"));

        if(targetKeyList.contains("Command")) {
            targetCommand = section.getString("Command");
        } else {
            targetCommand = "";
        }
        if(targetKeyList.contains("Keep-Open")) {
            targetKeepOpen = section.getBoolean("Keep-Open");
        } else {
            targetKeepOpen = false;
        }
        if(targetKeyList.contains("Price")) {
            targetPrice = section.getInt("Price");
        } else {
            targetPrice = 0;
        }

        return new MenuItem(targetItem, targetPositionList, targetCommand, targetKeepOpen, targetPrice);
    }

    public MenuItem getMenuItem(ItemStack targetItem, List<MenuItem> items) {
        for(MenuItem item : items) {
            if(targetItem.isSimilar(item.getItem())) {
                return item;
            }
        }
        return null;
    }

    public void closeInventory(HumanEntity humanEntity) {
        invTask = new BukkitRunnable() {
            @Override
            public void run() {
                humanEntity.closeInventory();
            }
        };
        invTask.runTask(ANOHANAMarry.getINSTANCE());
    }

    public void openInventory(HumanEntity humanEntity, Inventory inventory){
        invTask = new BukkitRunnable() {
            @Override
            public void run() {
                humanEntity.openInventory(inventory);
            }
        };
        invTask.runTask(ANOHANAMarry.getINSTANCE());
    }

    public String getInventoryTitle(String key) {
        return ChatColor.translateAlternateColorCodes('&', guiInventoryMap.get(key).getString("Menu-Setting.Title"));
    }
}
