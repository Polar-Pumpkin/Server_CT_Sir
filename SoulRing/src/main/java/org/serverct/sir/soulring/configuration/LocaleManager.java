package org.serverct.sir.soulring.configuration;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.serverct.sir.soulring.SoulRing;

import java.io.File;
import java.io.IOException;

public class LocaleManager {

    private static LocaleManager localeManager;

    public static LocaleManager getLocaleManager() {
        if(localeManager == null) {
            localeManager = new LocaleManager();
        }
        return localeManager;
    }

    private String[] statsMsg = {
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "",
            "  &6&lSoul Ring &7| &d&l魂环 &7>>>",
            "",
            "  &9&l物品属性 &7>>>",
            "",
            "%AttributesPreview%",
            "",
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    private File localeFile = new File(SoulRing.getInstance().getDataFolder() + File.separator + "Locale.yml");
    private FileConfiguration localeData = YamlConfiguration.loadConfiguration(localeFile);

    public void loadLanguage() {
        if (!localeFile.exists()) {
            createDefaultLanguage();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 未找到语言文件, 已自动生成."));
        } else {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 已加载语言文件."));
        }

        localeData = YamlConfiguration.loadConfiguration(localeFile);
    }

    private void createDefaultLanguage() {
        ConfigurationSection plugins = localeData.createSection("Plugins");
        plugins.set("Prefix", "&7[&a&lSoul Ring&7] ");
        plugins.set("Info", "&a&l&o>> ");
        plugins.set("Warn", "&e&l&o>> ");
        plugins.set("Error", "&c&l&o>> ");
        plugins.set("NotPlayer", "&7此命令仅玩家可使用.");
        plugins.set("NoPermission", "&7操作成功, 花费 &c%money%&7.");
        plugins.set("NotEnoughMoney", "&7您的金钱不足以执行此操作(&c%money%&7).");
        plugins.set("CostSuccess", "&7操作成功, 花费 &c%money%&7.");
        plugins.set("ReloadSuccess", "&7重载成功.");

        ConfigurationSection commandMsg = localeData.createSection("Commands");
        commandMsg.set("UnknownParam", "&7未知参数. 请检查您的命令拼写是否正确.");
        commandMsg.set("UnknownCmd", "&7未知命令. 请输入 &d&l/sr help &7查看帮助.");
        commandMsg.set("UnknownRing", "&7未知魂环, 操作失败.");
        commandMsg.set("PlayerOffline", "&7目标玩家不在线, 操作失败.");
        commandMsg.set("RingGaveSuccess", "&7已给予 &c%player% &c%amount% &7个魂环 &c%ringKey%&7.");
        commandMsg.set("RingReceivedSuccess", "&7已收到 &c%amount% &7个魂环 &c%ringKey%&7.");
        commandMsg.set("PunchSuccess", "&7打孔成功.");
        commandMsg.set("InlaySuccess", "&7吸收魂环 &c%ringDisplay% &7成功.");
        commandMsg.set("NoSlot", "&7此物品没有其他空魂环空位了.");
        commandMsg.set("RingSlotEmpty", "&7请在物品栏第 &c9 &7格放入目标魂环.");
        commandMsg.set("AmountError", "&7请勿对叠加物品打孔/吸收魂环.");
        commandMsg.set("TooManyRing", "&7请勿放入多个目标魂环.");
        commandMsg.set("Stats", statsMsg);
        commandMsg.set("CantPunchMore", "&7此物品孔数已达到最大值(&c%amount%&7), 您无法再继续打孔了.");
        commandMsg.set("CantAbsorbMore", "&7该物品吸收的此魂环已达到最大值(&c%amount%&7), 您无法再继续为该物品吸收此魂环了了.");

        ConfigurationSection attributeMsg = localeData.createSection("Attributes");
        attributeMsg.set("VAMPIRE", "%color%&l生命吸取! &7为您恢复了 &c%amount% &7点生命值.");
        attributeMsg.set("CRITICAL", "%color%&l暴击! &7打出 &c%amount% &7点伤害.");
        attributeMsg.set("NAUSEA", "%color%&l反胃! &7你的攻击击中腹部使敌人感到一阵恶心(持续 &c%amount% &7秒).");
        attributeMsg.set("IMPRISONMENT", "%color%&l禁锢! &7你的攻击带来的剧痛使敌人无法动弹(持续 &c%amount% &7秒).");
        attributeMsg.set("BURN", "%color%&l引燃! &7你的攻击点着了敌人的衣服.");
        attributeMsg.set("PHYSICAL_DEFENSE", "%color%&l防御! &7你的魂环为你防御了 &c%amount% &7点敌人的攻击.");

        try {
            localeData.save(localeFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String getMessage(String msgType, String classification, String path) {
        ConfigurationSection targetSection = localeData.getConfigurationSection(classification);
        String pluginPrefix = localeData.getString("Plugins.Prefix");

        if(targetSection.getString(path) != null) {
            switch (msgType) {
                case "INFO":
                    String infoPrefix = localeData.getString("Plugins.Info");
                    return ChatColor.translateAlternateColorCodes('&', pluginPrefix + infoPrefix + targetSection.getString(path));
                case "WARN":
                    String warnPrefix = localeData.getString("Plugins.Warn");
                    return ChatColor.translateAlternateColorCodes('&', pluginPrefix + warnPrefix + targetSection.getString(path));
                case "ERROR":
                    String errorPrefix = localeData.getString("Plugins.Error");
                    return ChatColor.translateAlternateColorCodes('&', pluginPrefix + errorPrefix + targetSection.getString(path));
                default:
                    return null;
            }
        }

        return null;
    }

    public FileConfiguration getLocaleData() {
        return localeData;
    }
}
