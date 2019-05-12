package org.serverct.sir.anohanamarry.configuration.PlayerData;

import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.configuration.LanguageData.Language;
import org.serverct.sir.anohanamarry.configuration.LanguageData.MessageType;
import org.serverct.sir.anohanamarry.hook.AMarryEconomy;
import org.serverct.sir.anohanamarry.runnable.ExpiredMarryPropose;
import org.serverct.sir.anohanamarry.runnable.ExpiredSocializePropose;
import org.serverct.sir.anohanamarry.util.BasicUtils;
import org.serverct.sir.anohanamarry.util.TimeUtils;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerDataManager {

    private static PlayerDataManager playerDataManager;

    public static PlayerDataManager getInstance() {
        if(playerDataManager == null) {
            playerDataManager = new PlayerDataManager();
        }
        return playerDataManager;
    }

    private File playerDataFolder = new File(ANOHANAMarry.getINSTANCE().getDataFolder() + File.separator + "Players");
    @Getter private Map<String, PlayerData> loadedPlayerDataMap = new HashMap<>();
    @Getter private Map<String, Integer> loveLevelRequirementMap = new HashMap<>();

    private File newPlayerDataFile;
    private FileConfiguration newPlayerData;

    private FileConfiguration targetPlayerData;
    private String targetPlayerName;
    private SexType targetSex;
    private StatusType targetStatus;
    private String targetLover;
    private int targetLoveLevel;
    private int targetLovePoint;
    private String targetMarriedTime;
    private long targetMarriedTimeStamp;
    private Location targetHome;
    private List<String> targetQueue;
    private boolean targetShareExp;
    private boolean targetShareHealth;

    private ConfigurationSection targetHomeSection;
    private World targetHomeWorld;
    private double targetHomeX;
    private double targetHomeY;
    private double targetHomeZ;

    private ConfigurationSection targetShareSection;

    private PlayerData senderData;
    private PlayerData loverData;

    private BukkitRunnable expireTask;

    public void loadPlayerDatas() {
        if(!playerDataFolder.exists()){
            playerDataFolder.mkdirs();

            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7未找到玩家数据文件夹, 已自动生成."));
        } else {
            File[] playerDataFiles = playerDataFolder.listFiles(pathname -> {
                String fileName = pathname.getName();
                return fileName.endsWith(".yml");
            });

            if(playerDataFiles != null) {
                int dataFileAmount = 0;
                for(File playerDataFile : playerDataFiles) {
                    loadedPlayerDataMap.put(BasicUtils.getFileNameNoEx(playerDataFile.getName()), buildPlayerData(playerDataFile));
                    dataFileAmount ++;
                }

                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7共加载 &c" + String.valueOf(dataFileAmount) + " &7个玩家数据文件."));
            } else {
                Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &e> &7无玩家数据文件可供加载."));
            }
        }

        if(ANOHANAMarry.getINSTANCE().getLoveLevelMode()) {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7当前恋爱等级提升模式: &c消耗值&7."));
        } else {
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7当前恋爱等级提升模式: &c累计值&7."));
        }

        ConfigurationSection loveLevelRequirement = ANOHANAMarry.getINSTANCE().getConfig().getConfigurationSection("LoveLevel.Setting");
        int loveLevelAmount = 0;
        for(String level : loveLevelRequirement.getKeys(false)) {
            loveLevelRequirementMap.put(level, loveLevelRequirement.getInt(level));
            // Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7已加载恋爱等级配置: &cLv." + String.valueOf(level) + " &7-> &c" + String.valueOf(loveLevelRequirement.getInt(level) + "&7.")));
            loveLevelAmount++;
        }
        Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  &a> &7共加载 &c" + String.valueOf(loveLevelAmount) + " &7个恋爱等级配置."));
    }

    private PlayerData buildPlayerData(File playerDataFile) {
        targetPlayerData = YamlConfiguration.loadConfiguration(playerDataFile);

        targetPlayerName = BasicUtils.getFileNameNoEx(playerDataFile.getName());
        targetSex = SexType.valueOf(targetPlayerData.getString("Sex"));
        targetStatus = StatusType.valueOf(targetPlayerData.getString("Status"));
        targetLover = targetPlayerData.getString("Lover");
        targetLoveLevel = targetPlayerData.getInt("LoveLevel");
        targetLovePoint = targetPlayerData.getInt("LovePoint");
        targetMarriedTime = targetPlayerData.getString("MarriedTime.String");
        targetMarriedTimeStamp = targetPlayerData.getLong("MarriedTime.Stamp");

        targetHomeSection = targetPlayerData.getConfigurationSection("Home");
        targetHomeWorld = ANOHANAMarry.getINSTANCE().getServer().getWorld(targetHomeSection.getString("World"));
        targetHomeX = targetHomeSection.getDouble("X");
        targetHomeY = targetHomeSection.getDouble("Y");
        targetHomeZ = targetHomeSection.getDouble("Z");
        targetHome = new Location(targetHomeWorld, targetHomeX, targetHomeY, targetHomeZ);

        targetQueue = targetPlayerData.getStringList("Queue");

        targetShareSection = targetPlayerData.getConfigurationSection("Share");
        targetShareExp = targetShareSection.getBoolean("Exp");
        targetShareHealth = targetShareSection.getBoolean("Health");

        return new PlayerData(targetPlayerName, targetSex, targetStatus, targetLover, targetLoveLevel, targetLovePoint, targetMarriedTime, targetMarriedTimeStamp, targetHome, targetQueue, targetShareExp, targetShareHealth);
    }

    public void saveData(PlayerData playerData) {
        newPlayerDataFile = new File(playerDataFolder.getAbsolutePath() + File.separator + playerData.getPlayerName() + ".yml");
        newPlayerData = YamlConfiguration.loadConfiguration(newPlayerDataFile);

        newPlayerData.set("Sex", playerData.getSex().toString());
        newPlayerData.set("Status", playerData.getStatus().toString());
        newPlayerData.set("Lover", playerData.getLover());
        newPlayerData.set("LoveLevel", playerData.getLoveLevel());
        newPlayerData.set("LovePoint", playerData.getLovePoint());
        newPlayerData.set("MarriedTime.String", playerData.getMarriedTime());
        newPlayerData.set("MarriedTime.Stamp", playerData.getMarriedTimeStamp());
        newPlayerData.set("Home.World", playerData.getHome().getWorld().getName());
        newPlayerData.set("Home.X", playerData.getHome().getX());
        newPlayerData.set("Home.Y", playerData.getHome().getY());
        newPlayerData.set("Home.Z", playerData.getHome().getZ());
        newPlayerData.set("Queue", playerData.getQueue());
        newPlayerData.set("Share.Exp", playerData.isShareExp());
        newPlayerData.set("Share.Health", playerData.isShareHealth());

        try {
            newPlayerData.save(newPlayerDataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
        loadedPlayerDataMap.put(playerData.getPlayerName(), buildPlayerData(newPlayerDataFile));
    }

    public void updateStatus(Player sender, Player lover, boolean isSocialize) {
        senderData = loadedPlayerDataMap.get(sender.getName());
        loverData = loadedPlayerDataMap.get(lover.getName());

        senderData.setStatus(isSocialize ? StatusType.FallInLove : StatusType.Married);
        senderData.setLover(lover.getName());
        senderData.setLoveLevel(isSocialize ? 0 : 1);
        senderData.setLovePoint(0);
        senderData.setMarriedTime(isSocialize ? "" : TimeUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
        senderData.setMarriedTimeStamp(isSocialize ? 0 : System.currentTimeMillis());
        senderData.setQueue(new ArrayList<>());

        loverData.setStatus(isSocialize ? StatusType.FallInLove : StatusType.Married);
        loverData.setLover(sender.getName());
        loverData.setLoveLevel(isSocialize ? 0 : 1);
        loverData.setLovePoint(0);
        loverData.setMarriedTime(isSocialize ? "" : TimeUtils.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
        loverData.setMarriedTimeStamp(isSocialize ? 0 : System.currentTimeMillis());
        loverData.setQueue(new ArrayList<>());

        saveData(senderData);
        saveData(loverData);

        if(!isSocialize) {
            Language.getInstance().broadcastMessage(
                    Language.getInstance().getMessage(MessageType.INFO, "Common.Married.Broadcast")
                            .replace("%sender%", senderData.getPlayerName())
                            .replace("%receiver%", loverData.getPlayerName())
            );
            Language.getInstance().broadcastTitle(
                    Language.getInstance().getMessage("Common.Married.Title"),
                    Language.getInstance().getMessage(MessageType.INFO, "Common.Married.Broadcast")
                            .replace("%sender%", senderData.getPlayerName())
                            .replace("%receiver%", loverData.getPlayerName())
            );
        }
    }

    public void divorce(Player sender) {
        if(loadedPlayerDataMap.containsKey(sender.getName())) {
            senderData = loadedPlayerDataMap.get(sender.getName());

            if(senderData.getStatus().equals(StatusType.Married)) {
                loverData = loadedPlayerDataMap.get(senderData.getLover());

                senderData.setStatus(StatusType.Single);
                senderData.setLover("");
                senderData.setLoveLevel(0);
                senderData.setLovePoint(0);
                senderData.setMarriedTime("");
                senderData.setMarriedTimeStamp(0);
                senderData.setQueue(new ArrayList<>());

                loverData.setStatus(StatusType.Single);
                loverData.setLover("");
                loverData.setLoveLevel(0);
                loverData.setLovePoint(0);
                loverData.setMarriedTime("");
                loverData.setMarriedTimeStamp(0);
                loverData.setQueue(new ArrayList<>());

                saveData(senderData);
                saveData(loverData);

                sender.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Divorce.Success").replace("%lover%", senderData.getLover()));
                Language.getInstance().broadcastMessage(
                        Language.getInstance().getMessage(MessageType.INFO, "Common.Divorced.Broadcast")
                                .replace("%sender%", senderData.getPlayerName())
                                .replace("%receiver%", loverData.getPlayerName())
                );
                if(Bukkit.getPlayer(senderData.getLover()).isOnline()) {
                    Bukkit.getPlayer(senderData.getLover()).sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Common.Divorced.Target"));
                }
            }
        }
    }

    public void sendMarryPropose(Player player, Player targetPlayer) {
        if(targetPlayer.isOnline()) {
            if (!player.getName().equalsIgnoreCase(targetPlayer.getName())) {
                if (PlayerDataManager.getInstance().getLoadedPlayerDataMap().containsKey(targetPlayer.getName())) {
                    senderData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(player.getName());
                    loverData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(targetPlayer.getName());

                    if (loverData.getStatus() == StatusType.Married || senderData.getStatus() == StatusType.Married) {
                        player.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Propose.Marry.PlayerHasMarried"));
                    } else {
                        if(senderData.getStatus() == StatusType.FallInLove) {
                            if(senderData.getLover().equalsIgnoreCase(targetPlayer.getName())) {
                                if(senderData.getLovePoint() >= ANOHANAMarry.getINSTANCE().getMarryRequirement()) {
                                    if(AMarryEconomy.getAMarryEconomyUtil().cost(player.getName(), targetPlayer.getName(), "sendPropose")) {
                                        loverData = loadedPlayerDataMap.get(targetPlayer.getName());
                                        targetQueue = loverData.getQueue();
                                        targetQueue.add(player.getName());
                                        loverData.setQueue(targetQueue);
                                        PlayerDataManager.getInstance().saveData(loverData);

                                        expireTask = new ExpiredMarryPropose(player, targetPlayer);
                                        expireTask.runTaskLater(ANOHANAMarry.getINSTANCE(), ANOHANAMarry.getINSTANCE().getExpireDelay(false) * 60 *20);

                                        player.sendMessage(
                                                Language.getInstance().getMessage(MessageType.INFO, "Commands.Propose.Marry.Send")
                                                        .replace("%receiver%", targetPlayer.getName())
                                                        .replace("%time%", String.valueOf(ANOHANAMarry.getINSTANCE().getExpireDelay(false)))
                                        );
                                        Language.getInstance().sendSubtitle(targetPlayer.getName(), Language.getInstance().getMessage("Common.Propose.Marry.Received.Subtitle").replace("%sender%", targetPlayer.getName()));
                                        Language.getInstance().sendProposeActionMessage(player.getName(), targetPlayer.getName(), false);
                                    }
                                } else {
                                    player.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Common.Propose.Marry.Requirement.LovePoint"));
                                }
                            } else {
                                player.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Common.Propose.Marry.Requirement.NotLover"));
                            }
                        } else {
                            player.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Common.Propose.Marry.Requirement.NotSocialize"));
                        }
                    }
                } else {
                    player.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Player"));
                }
            } else {
                player.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Propose.Marry.CantMarryYourself"));
            }
        } else {
            player.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Player"));
        }
    }

    public void sendProposeResult(Player player, Player targetPlayer, boolean accepted) {
        if(accepted) {
            if(AMarryEconomy.getAMarryEconomyUtil().cost(player.getName(), targetPlayer.getName(), "marry")) {
                updateStatus(player, targetPlayer, false);

                Language.getInstance().sendSubtitle(player.getName(), Language.getInstance().getMessage("Common.Propose.Marry.Result.Accept.Subtitle").replace("%receiver%", targetPlayer.getName()));
                player.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Common.Propose.Marry.Result.Accept.Message").replace("%receiver%", targetPlayer.getName()));
                targetPlayer.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Commands.Propose.Marry.Accepted").replace("%sender%", player.getName()));
            }
        } else {
            loverData = loadedPlayerDataMap.get(targetPlayer.getName());
            targetQueue = loverData.getQueue();
            targetQueue.remove(player.getName());
            loverData.setQueue(targetQueue);
            PlayerDataManager.getInstance().saveData(loverData);

            Language.getInstance().sendSubtitle(player.getName(), Language.getInstance().getMessage("Common.Propose.Marry.Result.Refuse.SubTitle").replace("%receiver%", targetPlayer.getName()));
            player.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Common.Propose.Marry.Result.Refuse.Message").replace("%receiver%", targetPlayer.getName()));
            targetPlayer.sendMessage(Language.getInstance().getMessage(MessageType.INFO, "Commands.Propose.Marry.Refused").replace("%sender%", player.getName()));
        }
    }

    public void sendSocailizePropose(Player player, Player targetPlayer) {
        if (!player.getName().equalsIgnoreCase(targetPlayer.getName())) {
            if (PlayerDataManager.getInstance().getLoadedPlayerDataMap().containsKey(targetPlayer.getName())) {
                senderData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(player.getName());
                loverData = PlayerDataManager.getInstance().getLoadedPlayerDataMap().get(targetPlayer.getName());

                if (loverData.getStatus() == StatusType.Married || senderData.getStatus() == StatusType.Married) {
                    player.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Propose.Socialize.PlayerHasMarried"));
                } else if(loverData.getStatus() == StatusType.FallInLove || senderData.getStatus() == StatusType.FallInLove) {

                } else {
                    loverData = loadedPlayerDataMap.get(targetPlayer.getName());
                    targetQueue = loverData.getQueue();
                    targetQueue.add(player.getName());
                    loverData.setQueue(targetQueue);
                    PlayerDataManager.getInstance().saveData(loverData);

                    expireTask = new ExpiredSocializePropose(player, targetPlayer);
                    expireTask.runTaskLater(ANOHANAMarry.getINSTANCE(), ANOHANAMarry.getINSTANCE().getExpireDelay(true) * 60 *20);

                    player.sendMessage(
                            Language.getInstance().getMessage(MessageType.INFO, "Commands.Propose.Socialize.Send")
                                    .replace("%receiver%", targetPlayer.getName())
                                    .replace("%time%", String.valueOf(ANOHANAMarry.getINSTANCE().getExpireDelay(true)))
                    );
                    Language.getInstance().sendSubtitle(targetPlayer.getName(), Language.getInstance().getMessage("Common.Propose.Socialize.Received.Subtitle").replace("%sender%", targetPlayer.getName()));
                    Language.getInstance().sendProposeActionMessage(player.getName(), targetPlayer.getName(), true);
                }
            } else {
                    player.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Unknown.Player"));
            }
        } else {
            player.sendMessage(Language.getInstance().getMessage(MessageType.WARN, "Commands.Propose.Socialize.CantSocializeYourself"));
        }
    }
}
