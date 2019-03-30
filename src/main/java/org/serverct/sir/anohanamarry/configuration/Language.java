package org.serverct.sir.anohanamarry.configuration;

import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.ANOHANAMarry;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class Language {

    private static Language languageClass;

    public static Language getLanguageClass() {
        if(languageClass == null){
            languageClass = new Language();
        }
        return  languageClass;
    }

    private File languageFile = new File(ANOHANAMarry.getINSTANCE().getDataFolder() + File.separator + "Language.yml");
    private FileConfiguration languageData = YamlConfiguration.loadConfiguration(languageFile);
    private Player target;

    private int fadeIn = 20 * ANOHANAMarry.getINSTANCE().getConfig().getInt("Title.FadeIn");
    private int stay = 20 * ANOHANAMarry.getINSTANCE().getConfig().getInt("Title.Stay");
    private int fadeOut = 20 * ANOHANAMarry.getINSTANCE().getConfig().getInt("Title.FadeOut");

    /*
    "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
    "",
    "  &e&l> &7您收到了来自 &c%sender% &7的求婚.",
    "",
    "  &a&l▸ &7接收",
    "  &c&l▸ &7拒绝",
    "",
    "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
     */

    private String[] proposeMsgUpperHalf = {
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "",
            "  &e&l> %msg%",
            "",
    };

    private String[] proposeMsgLowerHalf = {
            "",
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄"
    };

    private String[] playerInfo = {
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
            "",
            "  &e&l> &c%player% &7玩家信息",
            "",
            "  &a> &7婚姻状态: &c%status%",
            "  &a> &7爱人: &c%lover%",
            "  &a> &7亲密等级: &cLv.%lovelevel%&7(%lovepoint%&7)",
            "  &a> &7结婚时间: &c%marriedtime%",
            "",
            "&a&m┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄┄",
    };

    public void loadLanguageData() {
        if (!languageFile.exists()) {
            createDefaultLanguage();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7未找到语言文件, 已自动生成."));
        } else {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7已加载语言文件."));
        }
    }

    private void createDefaultLanguage() {
        ConfigurationSection plugins = languageData.createSection("Plugins");
        plugins.set("Prefix", "&7[&a&lANOHANA Marry&7] ");
        plugins.set("Info", "&a&l&o>> ");
        plugins.set("Warn", "&e&l&o>> ");
        plugins.set("Error", "&c&l&o>> ");
        plugins.set("NotPlayer", "&7此命令仅玩家可使用.");
        plugins.set("NotInLog", "&7您不在玩家数据中.");

        ConfigurationSection commandMsg = languageData.createSection("Commands");
        commandMsg.set("Unknown.Param", "&7未知参数. 请检查您的命令格式.");
        commandMsg.set("Unknown.command", "&7未知命令. 输入 &d/amarry help &7查看帮助.");
        commandMsg.set("Unknown.Player", "&7该玩家不存在.");
        commandMsg.set("Gender.Change", "&7性别成功设置为 &c%gender%&7!");
        commandMsg.set("Gender.CantChange", "&7您已经无法再修改自己的性别了.");
        commandMsg.set("MarryPropose.Send", "&7成功向 &c%receiver% &7求婚!");
        commandMsg.set("MarryPropose.PlayerHasMarried", "&7该玩家已婚, 请不要做老王!");
        commandMsg.set("MarryPropose.CantMarryYourself", "&7无法向自己求婚!");
        commandMsg.set("MarryPropose.NotInQueueList", "&7该玩家没有向您求过婚(或求婚请求已过期).");
        commandMsg.set("MarryPropose.Accepted", "&7您接受了来自 &c%sender% &7的求婚.");
        commandMsg.set("MarryPropose.Refused", "&7您拒绝了来自 &c%sender% &7的求婚.");
        commandMsg.set("Divorce.Success", "&7您已和 &c%lover% &7离婚.");
        commandMsg.set("Divorce.NotMarried", "&7您还没有结婚.");
        commandMsg.set("Home.Set", "&7爱之家设置成功.");
        commandMsg.set("Home.Teleport", "&7传送至爱之家...(延时: &c%delay%&7s)");
        commandMsg.set("Home.Delete", "&7已删除爱之家.");
        commandMsg.set("PlayerInfo", playerInfo);

        ConfigurationSection commonMsg = languageData.createSection("Common");
        commonMsg.set("LoverOnline.SubTitle", "&7您的爱人 &c%lover% &7上线了.");
        commonMsg.set("LoverOffline.SubTitle", "&7您的爱人 &c%lover% &7下线了.");

        commonMsg.set("Married.Broadcast", "&c&l囍! &c%sender% &7和 &c%receiver% &7结婚了!");
        commonMsg.set("Married.Title", "&c&l囍");
        commonMsg.set("Married.Subtitle", "&c%sender% &7和 &c%receiver% &7结婚了!");

        commonMsg.set("Divorced.Broadcast", "&c%sender% &7和 &c%receiver% &7离婚了!");
        commonMsg.set("Divorced.Target", "&c%lover% 已和您离婚.");

        commonMsg.set("MarryPropose.Received.SubTitle", "&7您收到了来自 &c%sender% &7的求婚.");
        commonMsg.set("MarryPropose.Received.Message.Upper", proposeMsgUpperHalf);
        commonMsg.set("MarryPropose.Received.Message.Lower", proposeMsgLowerHalf);
        commonMsg.set("MarryPropose.Received.Message.Msg", "&7您收到了来自 &c%sender% &7的求婚.");
        commonMsg.set("MarryPropose.Received.Accept", "接受");
        commonMsg.set("MarryPropose.Received.Refuse", "拒绝");
        commonMsg.set("MarryPropose.Received.TypeToAccept", "指令: /amarry propose accept %sender%");
        commonMsg.set("MarryPropose.Received.TypeToRefuse", "指令: /amarry propose refuse %sender%");

        commonMsg.set("MarryPropose.Result.Accept.SubTitle", "&c%receiver% &7接受了您的求婚.");
        commonMsg.set("MarryPropose.Result.Accept.Message", "&c%receiver% &7接受了您的求婚.");
        commonMsg.set("MarryPropose.Result.Refuse.SubTitle", "&c%receiver% &7拒绝了您的求婚.");
        commonMsg.set("MarryPropose.Result.Refuse.Message", "&c%receiver% &7拒绝了您的求婚.");

        commonMsg.set("Cost.NotEnough", "&7您的金钱不足(&c%economy_price%&7).");
        commonMsg.set("Cost.Deposit", "&7您收到 &c%economy_price%&7.");
        commonMsg.set("Cost.Success", "&7操作成功! 花费 &c%economy_price%&7.");


        try {
            languageData.save(languageFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private HoverEvent buildHoverEvent(String senderName, boolean isAccept) {
        if(isAccept) {
            return new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(
                            getMessage("Common.MarryPropose.Received.TypeToAccept").replace("%sender%", senderName))
                            .color(net.md_5.bungee.api.ChatColor.GRAY)
                            .create()
            );
        } else {
            return new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(
                            getMessage("Common.MarryPropose.Received.TypeToRefuse").replace("%sender%", senderName))
                            .color(net.md_5.bungee.api.ChatColor.GRAY)
                            .create()
            );
        }
    }

    private ClickEvent buildClickEvent(String senderName, boolean isAccept) {
        if(isAccept) {
            return new ClickEvent(
                    ClickEvent.Action.SUGGEST_COMMAND,
                    "/amarry propose accept %sender%".replace("%sender%", senderName)
            );
        } else {
            return new ClickEvent(
                    ClickEvent.Action.SUGGEST_COMMAND,
                    "/amarry propose refuse %sender%".replace("%sender%", senderName)
            );
        }
    }

    private TextComponent buildActionTextComponent(String senderName, boolean isAccept) {
        TextComponent actionText = new TextComponent("  " + ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Action"));
        actionText.setBold(true);
        if(isAccept) {
            actionText.setColor(net.md_5.bungee.api.ChatColor.GREEN);

            TextComponent acceptText = new TextComponent(getMessage("Common.MarryPropose.Received.Accept"));
            acceptText.setColor(net.md_5.bungee.api.ChatColor.GRAY);
            acceptText.setBold(false);
            actionText.addExtra(acceptText);

            actionText.setHoverEvent(buildHoverEvent(senderName, true));
            actionText.setClickEvent(buildClickEvent(senderName, true));
        } else {
            actionText.setColor(net.md_5.bungee.api.ChatColor.RED);

            TextComponent refuseText = new TextComponent(getMessage("Common.MarryPropose.Received.Refuse"));
            refuseText.setColor(net.md_5.bungee.api.ChatColor.GRAY);
            refuseText.setBold(false);
            actionText.addExtra(refuseText);

            actionText.setHoverEvent(buildHoverEvent(senderName, false));
            actionText.setClickEvent(buildClickEvent(senderName, false));
        }
        return actionText;
    }

    public void sendProposeMessage(String senderName, String receiverName) {
        Player receiver = Bukkit.getPlayer(receiverName);

        for(String msg : languageData.getStringList("Common.MarryPropose.Received.Message.Upper")) {
            receiver.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            msg.replace("%msg%", getMessage("Common.MarryPropose.Received.Message.Msg").replace("%sender%", senderName))
                    )
            );
        }
        receiver.spigot().sendMessage(buildActionTextComponent(senderName, true));
        receiver.spigot().sendMessage(buildActionTextComponent(senderName, false));
        for(String msg : languageData.getStringList("Common.MarryPropose.Received.Message.Lower")) {
            receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    public List<String> getPlayerInfo(String playerName) {
        List<String> playerInfo = languageData.getStringList("Commands.PlayerInfo");
        List<String> playerInfoMsg = new ArrayList<>();
        for(String msg : playerInfo) {
            ChatColor.translateAlternateColorCodes(
                    '&',
                    msg
                            .replace("%player%", playerName)
                            .replace("%status%", PlayerData.getPlayerDataManager().getFormatedStatus(playerName))
                            .replace("%lover%", PlayerData.getPlayerDataManager().getLover(playerName))
                            .replace("%lovelevel%", String.valueOf(PlayerData.getPlayerDataManager().getLoveLevel(playerName)))
                            .replace("%lovepoint%", String.valueOf(PlayerData.getPlayerDataManager().getLovePoint(playerName)))
                            .replace("%marriedtime%", PlayerData.getPlayerDataManager().getMarriedTime(playerName))
            );
        }
        return playerInfoMsg;
    }

    public void sendSubtitle(String playerName, String path) {
        target = Bukkit.getPlayer(playerName);

        target.sendTitle(
                "",
                getMessage(path),
                fadeIn, stay, fadeOut);
    }

    public void sendTitle(String playerName, String titlePath, String subtitle) {
        target = Bukkit.getPlayer(playerName);

        target.sendTitle(
                getMessage(titlePath),
                subtitle,
                fadeIn, stay, fadeOut);
    }

    public void sendSubtitle(String playerName, String path, String variableText, String replaceText) {
        target = Bukkit.getPlayer(playerName);

        target.sendTitle(
                "",
                getMessage(path).replace(variableText, replaceText),
                fadeIn, stay, fadeOut);
    }

    public void sendMessage(String playerName, String path, String msgType) {
        Player target = Bukkit.getPlayer(playerName);

        target.sendMessage(getMessage(msgType, path));
    }

    public void sendMessage(String playerName, String path, String msgType, String variableText, String replaceText) {
        Player target = Bukkit.getPlayer(playerName);

        target.sendMessage(getMessage(msgType, path).replace(variableText, replaceText));
    }

    public String getMessage(String msgType, String path) {
        if(languageData.getString(path) != null) {
            String prefix = ChatColor.translateAlternateColorCodes('&', languageData.getString("Plugins.Prefix"));
            String msg = ChatColor.translateAlternateColorCodes('&', languageData.getString(path));
            if(msgType.equalsIgnoreCase("info")) {
                String infoLog = ChatColor.translateAlternateColorCodes('&', languageData.getString("Plugins.Info"));
                return prefix + infoLog + msg;
            } else if(msgType.equalsIgnoreCase("warn")) {
                String warnLog = ChatColor.translateAlternateColorCodes('&', languageData.getString("Plugins.Warn"));
                return prefix + warnLog + msg;
            } else if(msgType.equalsIgnoreCase("error")) {
                String errorLog = ChatColor.translateAlternateColorCodes('&', languageData.getString("Plugins.Error"));
                return prefix + errorLog + msg;
            } else if(msgType.equalsIgnoreCase("debug")) {
                String debugPrefix = ChatColor.translateAlternateColorCodes('&', "&7[&d&lANOHANA Marry&7(&c&lDEBUG&7)] ");
                String debugLog = ChatColor.translateAlternateColorCodes('&', "&d&l&o>> ");
                return prefix + debugLog + msg;
            }
        }
        return "";
    }

    public String getMessage(String path) {
        if(languageData.getString(path) != null) {
            return ChatColor.translateAlternateColorCodes('&', languageData.getString(path));
        }
        return "";
    }

}
