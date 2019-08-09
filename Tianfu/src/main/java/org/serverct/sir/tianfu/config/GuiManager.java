package org.serverct.sir.tianfu.config;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.inventory.Inventory;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.enums.MessageType;
import org.serverct.sir.tianfu.enums.Position;
import org.serverct.sir.tianfu.enums.TalentType;
import org.serverct.sir.tianfu.util.CommonUtil;
import org.serverct.sir.tianfu.util.LocaleUtil;

import java.io.File;

public class GuiManager {

    private static GuiManager instance;
    public static GuiManager getInstance() {
        if(instance == null) {
            instance = new GuiManager();
        }
        return instance;
    }

    private LocaleUtil locale = Tianfu.getInstance().getLocale();

    private File dataFile = new File(Tianfu.getInstance().getDataFolder() + File.separator + "gui.yml");
    private FileConfiguration data;

    public void load() {
        if(!dataFile.exists()) {
            Tianfu.getInstance().saveResource("gui.yml", false);
            locale.buildMessage(Tianfu.getInstance().getLocaleKey(), MessageType.WARN, "&7未找到 Gui 配置文件, 已自动生成.");
        }
        data = YamlConfiguration.loadConfiguration(dataFile);
    }

    public Inventory init(PlayerData playerData) {
        ConfigurationSection itemSection = data.getConfigurationSection("Items");
        ConfigurationSection talentSection;

        Inventory gui = Bukkit.createInventory(null, data.getInt("Setting.Row") * 9, ChatColor.translateAlternateColorCodes('&', "&6&l天赋系统"));
        TalentType talentType;
        for(String type : itemSection.getKeys(false)) {
            talentType = TalentType.valueOf(type.toUpperCase());
            talentSection = itemSection.getConfigurationSection(type);

            gui.setItem(
                    Position.getPositon(Position.valueOf("x" + talentSection.getInt("X")),  Position.valueOf("y" + talentSection.getInt("Y"))),
                    CommonUtil.applyPlaceholder(CommonUtil.buildItem(talentSection), talentType, playerData)
            );
        }

        return gui;
    }

    public TalentType getTalentByDisplay(String display, PlayerData playerData) {
        ConfigurationSection itemSection = data.getConfigurationSection("Items");
        ConfigurationSection talentSection;
        String configDisplay;

        for(String type : itemSection.getKeys(false)) {
            talentSection = itemSection.getConfigurationSection(type);
            configDisplay = CommonUtil.applyPlaceholder(CommonUtil.buildItem(talentSection), TalentType.valueOf(type.toUpperCase()), playerData).getItemMeta().getDisplayName();
            if(display.equalsIgnoreCase(configDisplay)) {
                return TalentType.valueOf(type.toUpperCase());
            }
        }
        return null;
    }

    public Sound getSound(boolean isOpen) {
        if(isOpen) {
            return Sound.valueOf(data.getString("Setting.Sound.Open").toUpperCase());
        } else {
            return Sound.valueOf(data.getString("Setting.Sound.Close").toUpperCase());
        }
    }

    public String getTitle() {
        return ChatColor.translateAlternateColorCodes('&', data.getString("Setting.Title"));
    }
}
