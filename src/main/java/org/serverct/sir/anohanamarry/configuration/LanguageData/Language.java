package org.serverct.sir.anohanamarry.configuration.LanguageData;

import lombok.Getter;
import net.md_5.bungee.api.chat.*;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.util.BasicUtils;

import java.io.File;
import java.util.List;

public class Language {

    private static Language languageClass;

    public static Language getInstance() {
        if(languageClass == null){
            languageClass = new Language();
        }
        return  languageClass;
    }

    private File languageFile = new File(ANOHANAMarry.getINSTANCE().getDataFolder() + File.separator + "Language.yml");
    @Getter private FileConfiguration languageData = YamlConfiguration.loadConfiguration(languageFile);
    private Player target;

    private int fadeIn = 20 * ANOHANAMarry.getINSTANCE().getConfig().getInt("Title.FadeIn");
    private int stay = 20 * ANOHANAMarry.getINSTANCE().getConfig().getInt("Title.Stay");
    private int fadeOut = 20 * ANOHANAMarry.getINSTANCE().getConfig().getInt("Title.FadeOut");

    private List<Player> onlinePlayerList;

    public void loadLanguageData() {
        if (!languageFile.exists()) {
            ANOHANAMarry.getINSTANCE().saveResource("Language.yml", true);
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7未找到语言文件, 已自动生成."));
        } else {
            languageData = YamlConfiguration.loadConfiguration(languageFile);
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7已加载语言文件."));
        }
    }

    private HoverEvent buildHoverEvent(String senderName, boolean isAccept, boolean isSocialize) {
        if(isAccept) {
            return new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(
                            getMessage(isSocialize ? "Common.Propose.Socialize.Received.Action.TypeToAccept" : "Common.Propose.Marry.Received.Action.TypeToAccept").replace("%sender%", senderName))
                            .color(net.md_5.bungee.api.ChatColor.GRAY)
                            .create()
            );
        } else {
            return new HoverEvent(
                    HoverEvent.Action.SHOW_TEXT,
                    new ComponentBuilder(
                            getMessage(isSocialize ? "Common.Propose.Socialize.Received.Action.TypeToRefuse" : "Common.Propose.Marry.Received.Action.TypeToRefuse").replace("%sender%", senderName))
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

    private TextComponent buildActionTextComponent(String senderName, boolean isAccept, boolean isSocialize) {
        TextComponent actionText = new TextComponent("  " + ANOHANAMarry.getINSTANCE().getConfig().getString("Symbol.Action"));
        actionText.setBold(true);
        if(isAccept) {
            actionText.setColor(net.md_5.bungee.api.ChatColor.GREEN);

            TextComponent acceptText = new TextComponent(getMessage(isSocialize ? "Common.Propose.Socialize.Received.Accept" : "Common.Propose.Marry.Received.Accept"));
            acceptText.setColor(net.md_5.bungee.api.ChatColor.GRAY);
            acceptText.setBold(false);
            actionText.addExtra(acceptText);

            actionText.setHoverEvent(buildHoverEvent(senderName, true, isSocialize));
            actionText.setClickEvent(buildClickEvent(senderName, true));
        } else {
            actionText.setColor(net.md_5.bungee.api.ChatColor.RED);

            TextComponent refuseText = new TextComponent(getMessage(isSocialize ? "Common.Propose.Socialize.Received.Refuse" : "Common.Propose.Marry.Received.Refuse"));
            refuseText.setColor(net.md_5.bungee.api.ChatColor.GRAY);
            refuseText.setBold(false);
            actionText.addExtra(refuseText);

            actionText.setHoverEvent(buildHoverEvent(senderName, false, isSocialize));
            actionText.setClickEvent(buildClickEvent(senderName, false));
        }
        return actionText;
    }

    public void sendProposeActionMessage(String senderName, String receiverName, boolean isSocialize) {
        Player receiver = Bukkit.getPlayer(receiverName);

        for(String msg : languageData.getStringList(isSocialize ? "Common.Propose.Socialize.Received.Action.Upper" : "Common.Propose.Marry.Received.Action.Upper")) {
            receiver.sendMessage(
                    ChatColor.translateAlternateColorCodes(
                            '&',
                            msg
                                    .replace("%msg%", getMessage(isSocialize ? "Common.Propose.Socialize.Received.Action.Msg" : "Common.Propose.Marry.Received.Action.Msg")
                                    .replace("%sender%", senderName))
                                    .replace("%time%", String.valueOf(ANOHANAMarry.getINSTANCE().getExpireDelay(isSocialize)))
                    )
            );
        }
        receiver.spigot().sendMessage(buildActionTextComponent(senderName, true, isSocialize));
        receiver.spigot().sendMessage(buildActionTextComponent(senderName, false, isSocialize));
        for(String msg : languageData.getStringList(isSocialize ? "Common.Propose.Socialize.Received.Action.Lower" : "Common.Propose.Marry.Received.Action.Lower")) {
            receiver.sendMessage(ChatColor.translateAlternateColorCodes('&', msg));
        }
    }

    public void sendSubtitle(String playerName, String path) {
        target = Bukkit.getPlayer(playerName);

        target.sendTitle(
                "",
                getMessage(path),
                fadeIn, stay, fadeOut);
    }

    public void sendTitle(String playerName, String titlePath, String subtitlePath) {
        target = Bukkit.getPlayer(playerName);

        target.sendTitle(
                getMessage(titlePath),
                getMessage(subtitlePath),
                fadeIn, stay, fadeOut);
    }

    public String getMessage(MessageType msgType, String path) {
        if(languageData.getString(path) != null) {
            String prefix = ChatColor.translateAlternateColorCodes('&', languageData.getString("Plugins.Prefix"));
            String msg = ChatColor.translateAlternateColorCodes('&', languageData.getString(path));

            switch (msgType) {
                case INFO:
                    String infoLog = ChatColor.translateAlternateColorCodes('&', languageData.getString("Plugins.Info"));
                    return prefix + infoLog + msg;
                case WARN:
                    String warnLog = ChatColor.translateAlternateColorCodes('&', languageData.getString("Plugins.Warn"));
                    return prefix + warnLog + msg;
                case ERROR:
                    String errorLog = ChatColor.translateAlternateColorCodes('&', languageData.getString("Plugins.Error"));
                    return prefix + errorLog + msg;
                case DEBUG:
                    String debugPrefix = ChatColor.translateAlternateColorCodes('&', "&7[&d&lANOHANA Marry&7(&c&lDEBUG&7)] ");
                    String debugLog = ChatColor.translateAlternateColorCodes('&', "&d&l&o>> ");
                    return debugPrefix + debugLog + msg;
            }
        }
        return "ERROR(获取语言信息失败)!";
    }

    public String getMessage(String path) {
        if(languageData.getString(path) != null) {
            return ChatColor.translateAlternateColorCodes('&', languageData.getString(path));
        }
        return "";
    }

    public void broadcastMessage(String message) {
        onlinePlayerList = BasicUtils.getOnlinePlayers();
        for(Player onlinePlayer : onlinePlayerList) {
            onlinePlayer.sendMessage(message);
        }
    }

    public void broadcastTitle(String title, String subtitle) {
        onlinePlayerList = BasicUtils.getOnlinePlayers();
        for(Player onlinePlayer : onlinePlayerList) {
            onlinePlayer.sendTitle(title, subtitle, fadeIn, stay, fadeOut);
        }
    }
}
