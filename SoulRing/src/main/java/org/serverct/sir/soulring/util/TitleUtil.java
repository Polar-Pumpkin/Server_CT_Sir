package org.serverct.sir.soulring.util;

import java.lang.reflect.Constructor;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

public class TitleUtil {

    /**
     * 给一个玩家发送Title信息 1.8+
     * @param player 发送的玩家
     * @param fadeIn 淡入时间
     * @param stay 停留时间
     * @param fadeOut 淡出时间
     * @param title 主标题
     * @param subtitle 副标题
     */
    public static void sendTitle(Player player, Integer fadeIn, Integer stay, Integer fadeOut, String title, String subtitle) {
        String version = Bukkit.getServer().getClass().getPackage().getName().substring(23);
        try {
            if (title != null) { //要发送的title
                title = ChatColor.translateAlternateColorCodes('&', title); //支持&颜色代码
                title = title.replaceAll("%player%", player.getName());
                //取nms类，先判断是否是1.8测试版本,之后 搞到EnumTitleAction的TITLE枚举
                Object enumTitle = NMSUtil.getNMSClass(version.equalsIgnoreCase("v1_8_R1") ? "EnumTitleAction" : "PacketPlayOutTitle$EnumTitleAction").getField("TITLE").get(null);
                //取nms类，先判断是否是1.8测试版本,之后 调用到ChatSerializer里的 方法 a 并且保存其对象
                Object chatTitle = NMSUtil.getNMSClass(version.equalsIgnoreCase("v1_8_R1") ? "ChatSerializer" : "IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { title });
                //构造器 PacketPlayOutTitle包出来
                Constructor<?> titleConstructor = NMSUtil.getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] {NMSUtil.getNMSClass(version.equalsIgnoreCase("v1_8_R1") ? "EnumTitleAction" : "PacketPlayOutTitle$EnumTitleAction"), NMSUtil.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE } );
                //对象 标题数据包 对象 = 构造器.新建实例(填入一个Object的数组,或者是单个Object)
                Object titlePacket = titleConstructor.newInstance(new Object[] { enumTitle, chatTitle, fadeIn, stay, fadeOut });
                //调用sendPacket方法
                NMSUtil.sendPacket(player, titlePacket);
            }
            if (subtitle != null) { //要发送的子title
                subtitle = ChatColor.translateAlternateColorCodes('&', subtitle); //支持&颜色代码
                subtitle = subtitle.replaceAll("%player%", player.getName());
                Object enumSubtitle = NMSUtil.getNMSClass(version.equalsIgnoreCase("v1_8_R1") ? "EnumTitleAction" : "PacketPlayOutTitle$EnumTitleAction").getField("SUBTITLE").get(null);
                Object chatSubtitle = NMSUtil.getNMSClass(version.equalsIgnoreCase("v1_8_R1") ? "ChatSerializer" : "IChatBaseComponent$ChatSerializer").getMethod("a", new Class[] { String.class }).invoke(null, new Object[] { subtitle });
                Constructor<?> subtitleConstructor = NMSUtil.getNMSClass("PacketPlayOutTitle").getConstructor(new Class[] {NMSUtil.getNMSClass(version.equalsIgnoreCase("v1_8_R1") ? "EnumTitleAction" : "PacketPlayOutTitle$EnumTitleAction"), NMSUtil.getNMSClass("IChatBaseComponent"), Integer.TYPE, Integer.TYPE, Integer.TYPE } );
                Object subtitlePacket = subtitleConstructor.newInstance(new Object[] { enumSubtitle, chatSubtitle, fadeIn, stay, fadeOut });
                NMSUtil.sendPacket(player, subtitlePacket);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
