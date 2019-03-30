package org.serverct.sir.anohanamarry.configuration;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.hook.AMarryEconomy;
import org.serverct.sir.anohanamarry.util.BasicUtils;
import org.serverct.sir.anohanamarry.util.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.*;

/*
player1:
  Sex: Male
  Status: Single
  Lover: player2
  LoveLevel: 0
  LovePoint: 0
  MarriedTime: 2019/2/8
  Home:
    World: world
    x: 111
    y: 222
    z: 333
  Queue:
    - player3
  Share:
    Exp: true
    Health: false
 */

public class PlayerData {

    private static PlayerData playerDataClass;

    public static PlayerData getPlayerDataManager() {
        if(playerDataClass == null){
            playerDataClass = new PlayerData();
        }
        return playerDataClass;
    }

    private File playerDataFolder = new File(ANOHANAMarry.getINSTANCE().getDataFolder() + File.separator + "Players");
    private Map<String, FileConfiguration> playerDataMap = new HashMap<>();
    private Map<String, Integer> loveLevelRequirementMap = new HashMap<>();
    private File playerDataFile;
    private FileConfiguration playerData;
    private File loverDataFile;
    private FileConfiguration loverData;
    private List<String> queueList;

    public void loadPlayerData() {
        if(!playerDataFolder.exists()){
            playerDataFolder.mkdirs();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7未找到玩家数据文件夹, 已自动生成."));
        } else {
            File[] playerDataFiles = playerDataFolder.listFiles(pathname -> {
                String fileName = pathname.getName();
                return fileName.endsWith(".yml");
            });
            if(playerDataFiles != null) {
                int dataFileNumber = 0;
                for(File playerDataFile : playerDataFiles) {
                    playerDataMap.put(BasicUtils.getFileNameNoEx(playerDataFile.getName()), YamlConfiguration.loadConfiguration(playerDataFile));
                    dataFileNumber ++;
                }
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7已加载 " + String.valueOf(dataFileNumber) + " 个玩家数据文件."));
            } else {
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7无玩家数据文件可供加载."));
            }
        }
        ConfigurationSection loveLevelRequirement = ANOHANAMarry.getINSTANCE().getConfig().getConfigurationSection("LoveLevel.Setting");
        int loveLevelNumber = 0;
        for(String level : loveLevelRequirement.getKeys(false)) {
            loveLevelRequirementMap.put(level, loveLevelRequirement.getInt(level));
            loveLevelNumber++;
        }
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a&l> &7已加载 " + String.valueOf(loveLevelNumber) + " 个恋爱等级配置."));
    }

    public String getLoveLevelIncreaseMode() {
        return ANOHANAMarry.getINSTANCE().getConfig().getString("LoveLevel.Mode");
    }

    public boolean hasDataFile(String playerName) {
        return playerDataMap.containsKey(playerName);
    }

    public void addNewPlayer(String playerName) {
        playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
        playerData = YamlConfiguration.loadConfiguration(playerDataFile);
        queueList = new ArrayList<>();

        playerData.set("Sex", "Unknown");
        playerData.set("Status", "Single");
        playerData.set("Lover", "None");
        playerData.set("LoveLevel", 0);
        playerData.set("LovePoint", 0);
        playerData.set("Queue", queueList);
        playerData.set("MarriedTime", "");

        try {
            playerData.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        playerDataMap.put(playerName, playerData);
    }

    public void setHomeOfLove(String playerName, Location home) {
        if(playerDataMap.containsKey(playerName)) {
            playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            playerData = playerDataMap.get(playerName);

            playerData.set("Home.World", home.getWorld().getName());
            playerData.set("Home.x", home.getX());
            playerData.set("Home.y", home.getY());
            playerData.set("Home.z", home.getZ());

            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeHomeOfLove(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            playerData = playerDataMap.get(playerName);

            playerData.set("Home.World", null);
            playerData.set("Home.x", null);
            playerData.set("Home.y", null);
            playerData.set("Home.z", null);

            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void setSex(String playerName, boolean isMale) {
        if(playerDataMap.containsKey(playerName)) {
            playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            playerData = playerDataMap.get(playerName);

            if(isMale) {
                playerData.set("Sex", "Male");
            } else {
                playerData.set("Sex", "Female");
            }

            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void marry(String playerName, String loverName) {
        if(playerDataMap.containsKey(playerName)) {
            playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            playerData = playerDataMap.get(playerName);
            loverDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + loverName + ".yml");
            loverData = playerDataMap.get(loverName);

            playerData.set("Status", "Married");
            playerData.set("Lover", loverName);
            playerData.set("LoveLevel", 1);
            playerData.set("LovePoint", 0);
            playerData.set("MarriedTime", TimeUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
            clearQueue(playerName);

            loverData.set("Status", "Married");
            loverData.set("Lover", playerName);
            loverData.set("LoveLevel", 1);
            loverData.set("LovePoint", 0);
            loverData.set("MarriedTime", TimeUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
            clearQueue(loverName);

            List<Player> onlinePlayerList = BasicUtils.getOnlinePlayers();
            for(Player onlinePlayer : onlinePlayerList) {
                onlinePlayer.sendMessage(
                        Language.getLanguageClass().getMessage("info", "Common.Married.Broadcast")
                                .replace("%sender%", playerName).replace("%receiver%", loverName)
                );
            }
            onlinePlayerList.remove(Bukkit.getPlayer(playerName));
            onlinePlayerList.remove(Bukkit.getPlayer(loverName));
            for(Player onlinePlayer : onlinePlayerList) {
                Language.getLanguageClass().sendTitle(
                        onlinePlayer.getName(),
                        "Common.Married.Title",
                        Language.getLanguageClass().getMessage("Common.Married.Subtitle")
                                .replace("%sender%", playerName).replace("%receiver%", loverName)
                );
            }

            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                loverData.save(loverDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void socialize(String playerName, String loverName) {
        if(playerDataMap.containsKey(playerName)) {
            playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            playerData = playerDataMap.get(playerName);
            loverDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + loverName + ".yml");
            loverData = playerDataMap.get(loverName);

            playerData.set("Status", "Fall_In_Love");
            playerData.set("Lover", loverName);
            playerData.set("LoveLevel", 1);
            playerData.set("LovePoint", 0);
            clearQueue(playerName);

            loverData.set("Status", "Fall_In_Love");
            loverData.set("Lover", playerName);
            loverData.set("LoveLevel", 1);
            loverData.set("LovePoint", 0);
            clearQueue(loverName);

            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                loverData.save(loverDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void divorce(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            if(hasMarried(playerName)) {
                playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
                playerData = playerDataMap.get(playerName);
                if(hasDataFile(getLover(playerName))) {
                    loverDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + getLover(playerName) + ".yml");
                    loverData = playerDataMap.get(getLover(playerName));
                } else {
                    return;
                }

                Bukkit.getPlayer(playerName).sendMessage(Language.getLanguageClass().getMessage("info", "Commands.Divorce.Success").replace("%lover%", getLover(playerName)));
                Bukkit.getPlayer(getLover(playerName)).sendMessage(Language.getLanguageClass().getMessage("info", "Common.Divorced.Target").replace("%lover%", playerName));

               for(Player onlinePlayer : BasicUtils.getOnlinePlayers()) {
                   onlinePlayer.sendMessage(Language.getLanguageClass().getMessage("warn", "Common.Divorced.Broadcast").replace("%sender%", playerName).replace("%receiver%", getLover(playerName)));
               }

                playerData.set("Status", "Single");
                playerData.set("Lover", "None");
                playerData.set("LoveLevel", 0);
                playerData.set("LovePoint", 0);
                playerData.set("MarriedTime", "");

                loverData.set("Status", "Single");
                loverData.set("Lover", "None");
                loverData.set("LoveLevel", 0);
                loverData.set("LovePoint", 0);
                loverData.set("MarriedTime", "");

                try {
                    playerData.save(playerDataFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                try {
                    loverData.save(loverDataFile);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                Bukkit.getPlayer(playerName).sendMessage(Language.getLanguageClass().getMessage("error", "Commands.Divorce.NotMarried"));
            }
        } else {
            Bukkit.getPlayer(playerName).sendMessage(Language.getLanguageClass().getMessage("error", "Plugins.NotInLog"));
        }
    }

    public void addLovePoint(String playerName, int point) {
        if(playerDataMap.containsKey(playerName)) {
            playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            playerData = playerDataMap.get(playerName);
            if(!getLover(playerName).equalsIgnoreCase("None")) {
                if(hasDataFile(getLover(playerName))) {
                    loverDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + getLover(playerName) + ".yml");
                    loverData = playerDataMap.get(getLover(playerName));
                    return;
                }
            }

            if(getLoveLevelIncreaseMode().equalsIgnoreCase("requirement")) {
                if(getLovePoint(playerName) + point >= loveLevelRequirementMap.get(String.valueOf(getLoveLevel(playerName) + 1))) {
                    playerData.set("LovePoint", getLovePoint(playerName) + point - loveLevelRequirementMap.get(String.valueOf(getLoveLevel(playerName) + 1)));
                    loverData.set("LovePoint", getLovePoint(getLover(playerName)) + point - loveLevelRequirementMap.get(String.valueOf(getLoveLevel(getLover(playerName)) + 1)));

                    playerData.set("LoveLevel", getLoveLevel(playerName) + 1);
                    loverData.set("LoveLevel", getLoveLevel(getLover(playerName)) + 1);
                } else {
                    playerData.set("LovePoint", getLovePoint(playerName) + point);
                    loverData.set("LovePoint", getLovePoint(playerName) + point);
                }
            } else {
                if(getLovePoint(playerName) + point >= loveLevelRequirementMap.get(String.valueOf(getLoveLevel(playerName) + 1))) {
                    playerData.set("LovePoint", getLovePoint(playerName) + point);
                    loverData.set("LovePoint", getLovePoint(getLover(playerName)) + point);

                    playerData.set("LoveLevel", getLoveLevel(playerName) + 1);
                    loverData.set("LoveLevel", getLoveLevel(getLover(playerName)) + 1);
                } else {
                    playerData.set("LovePoint", getLovePoint(playerName) + point);
                    loverData.set("LovePoint", getLovePoint(playerName) + point);
                }
            }

            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }

            try {
                loverData.save(loverDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void addQueue(String playerName, String targetName) {
        if(playerDataMap.containsKey(playerName)) {
            playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            playerData = playerDataMap.get(playerName);
            queueList = getQueue(playerName);

            queueList.add(targetName);
            playerData.set("Queue", queueList);

            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void removeQueue(String playerName, String targetName) {
        if(playerDataMap.containsKey(playerName)) {
            playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            playerData = playerDataMap.get(playerName);
            queueList = getQueue(playerName);

            if(queueList.contains(targetName)) {
                queueList.remove(targetName);
                playerData.set("Queue", queueList);
            }

            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void clearQueue(String playerName) {
        playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
        playerData = playerDataMap.get(playerName);

        queueList = new ArrayList<>();
        playerData.set("Queue", queueList);

        try {
            playerData.save(playerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setShareSetting(String playerName, String key, boolean isEnable) {
        if(playerDataMap.containsKey(playerName)) {
            playerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerName + ".yml");
            playerData = playerDataMap.get(playerName);

            if(key.equalsIgnoreCase("Exp")) {
                playerData.set("Share.Exp", isEnable);
            } else if(key.equalsIgnoreCase("Health")) {
                playerData.set("Share.Health", isEnable);
            }

            try {
                playerData.save(playerDataFile);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean hasMarried(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerData = playerDataMap.get(playerName);

            return !playerData.getString("Lover").equalsIgnoreCase("None");
        }
        return  false;
    }

    public boolean hasHome(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerData = playerDataMap.get(playerName);

            return playerData.getString("Home.World") != null && playerData.getString("Home.x") != null && playerData.getString("Home.y") != null && playerData.getString("Home.z") != null;
        }
        return  false;
    }

    public boolean isLoverOnline(String playerName) {
        if(hasMarried(playerName)) {
            return Bukkit.getPlayer(getLover(playerName)).isOnline();
        } else {
            return false;
        }
    }

    public String getSex(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerData = playerDataMap.get(playerName);

            return playerData.getString("Sex");
        }
        return "Unknown";
    }

    public String getLover(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerData = playerDataMap.get(playerName);

            return playerData.getString("Lover");
        }
        return "None";
    }

    public String getFormatedStatus(String playerName) {
        switch (getStatus(playerName)) {
            case "Single":
                return "单身";
            case "Fall_In_Love":
                return "热恋中";
            case "Married":
                return "已婚";
            default:
                return "无数据";
        }
    }

    public String getStatus(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerData = playerDataMap.get(playerName);

            return playerData.getString("Status");
        }
        return "Single";
    }

    public Location getHome(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerData = playerDataMap.get(playerName);

            return new Location(Bukkit.getWorld(playerData.getString("Home.World")), playerData.getDouble("Home.x"), playerData.getDouble("Home.y"), playerData.getDouble("Home.z"));
        }
        return Bukkit.getPlayer(playerName).getBedSpawnLocation();
    }

    public int getLovePoint(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerData = playerDataMap.get(playerName);

            return playerData.getInt("LovePoint");
        }
        return 0;
    }

    public int getLoveLevel(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerData = playerDataMap.get(playerName);

            return playerData.getInt("LoveLevel");
        }
        return 0;
    }

    public String getMarriedTime(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerData = playerDataMap.get(playerName);

            return playerData.getString("MarriedTime");
        }
        return "";
    }

    public List<String> getQueue(String playerName) {
        if(playerDataMap.containsKey(playerName)) {
            playerData = playerDataMap.get(playerName);

            queueList = playerData.getStringList("Queue");
            return queueList;
        }
        queueList = new ArrayList<>();
        return queueList;
    }

    public void sendMarryPropose(String senderName, String receiverName) {

        addQueue(receiverName, senderName);

        Language.getLanguageClass().sendMessage(senderName, "Commands.MarryPropose.Send", "info", "%receiver%", receiverName);
        Language.getLanguageClass().sendSubtitle(receiverName, "Common.MarryPropose.Received.SubTitle", "%sender%", senderName);
        Language.getLanguageClass().sendProposeMessage(senderName, receiverName);
    }

    public void sendLoverLoginStatusMsg(String playerName, boolean isOnline) {
        if(isOnline) {
            Language.getLanguageClass().sendSubtitle(getLover(playerName), "Common.LoverOnline.SubTitle", "%lover%", playerName);
        } else {
            Language.getLanguageClass().sendSubtitle(getLover(playerName), "Common.LoverOffline.SubTitle", "%lover%", playerName);
        }
    }

    public void sendProposeResult(String senderName, String receiverName, boolean accepted) {
        if(accepted) {
            if(AMarryEconomy.getAMarryEconomyUtil().cost(senderName, receiverName, "marry")) {
                marry(senderName, receiverName);

                Language.getLanguageClass().sendSubtitle(senderName, "Common.MarryPropose.Result.Accept.SubTitle", "%receiver%", receiverName);
                Language.getLanguageClass().sendMessage(senderName, "Common.MarryPropose.Result.Accept.Message", "info", "%receiver%", receiverName);
                Language.getLanguageClass().sendMessage(receiverName, "Commands.MarryPropose.Accepted", "info", "%sender%", senderName);
            }
        } else {
            removeQueue(receiverName, senderName);

            Language.getLanguageClass().sendSubtitle(senderName, "Common.MarryPropose.Result.Refuse.SubTitle", "%receiver%", receiverName);
            Language.getLanguageClass().sendMessage(senderName, "Common.MarryPropose.Result.Refuse.Message", "info", "%receiver%", receiverName);
            Language.getLanguageClass().sendMessage(receiverName, "Commands.MarryPropose.Refused", "info", "%sender%", senderName);
        }
    }
}
