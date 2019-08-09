package org.serverct.sir.duobao.manager;

import lombok.Getter;
import org.bukkit.*;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Firework;
import org.bukkit.inventory.meta.FireworkMeta;
import org.serverct.sir.duobao.Duobao;
import org.serverct.sir.duobao.data.TreasureItem;
import org.serverct.sir.duobao.enums.MessageType;
import org.serverct.sir.duobao.util.AreaUtil;
import org.serverct.sir.duobao.util.CommonUtil;
import org.serverct.sir.duobao.util.LocaleUtil;

import java.util.*;

public class GameManager {

    private static LocaleUtil locale;

    private static GameManager instance;
    public static GameManager getInstance() {
        if(instance == null) {
            instance = new GameManager();
        }
        locale = Duobao.getInstance().getLocale();
        return instance;
    }

    @Getter private Map<String, List<Location>> gameMap = new HashMap<>();
    @Getter private Map<Integer, Location> area = new HashMap<>();

    public void loadArea() {
        FileConfiguration config = Duobao.getInstance().getConfig();

        if(config.isConfigurationSection("Area.1") && config.isConfigurationSection("Area.2")) {
            area.put(
                    1,
                    new Location(
                            Bukkit.getWorld(config.getString("Area.1.World")),
                            config.getDouble("Area.1.X"),
                            config.getDouble("Area.1.Y"),
                            config.getDouble("Area.1.Z")
                    )
            );
            area.put(
                    2,
                    new Location(
                            Bukkit.getWorld(config.getString("Area.2.World")),
                            config.getDouble("Area.2.X"),
                            config.getDouble("Area.2.Y"),
                            config.getDouble("Area.2.Z")
                    )
            );
        }
    }

    public void spawnChest(String id, int amount, boolean sendMsg) {
        locale.debug("执行 spawnChest 方法.");
        List<Location> area = AreaUtil.generateTreasureLocation(getArea());
        locale.debug("生成区域坐标列表: " + area.toString());

        if(!area.isEmpty()) {
            locale.debug("可用坐标列表非空, 开始尝试随机生成 " + amount + " 个箱子");
            Random random = new Random();
            List<Location> target = new ArrayList<>();

            for(int i = 0; i < amount; i++) {
                Location loc = area.get(random.nextInt(area.size() - 1));
                locale.debug("随机坐标: " + loc.toString());

                if(!isGamingLocation(loc)) {
                    locale.debug("非已生成箱子坐标.");
                    if(!target.contains(loc)) {
                        locale.debug("不含于目标坐标列表.");
                        target.add(loc);
                        locale.debug("已添加该坐标至箱子生成目标坐标列表.");
                        continue;
                    }
                }
                locale.debug("坐标不符合添加条件.");
                i--;
            }

            locale.debug("开始循环箱子生成目标坐标列表.");
            for(Location loc : target) {
                locale.debug("生成目标坐标: " + loc.toString());
                if(!isGamingLocation(loc)) {
                    locale.debug("非已生成箱子坐标.");
                    if(addGamingLocation(id, loc)) {
                        locale.debug("成功添加坐标至已生成箱子坐标列表.");
                        if(AreaUtil.checkAround(loc)) {
                            locale.debug("周围没有箱子, 许可生成箱子.");
                            loc.getBlock().setType(Material.CHEST);
                        }
                    }
                }
            }

            if(sendMsg) {
                CommonUtil.broadcast(locale.getMessage(Duobao.getInstance().getLocaleKey(), MessageType.INFO, "Game", "respawnChest").replace("%amount%", String.valueOf(amount)));
                locale.debug("已发送箱子补充公告.");
            }
        }
    }

    public boolean addGamingLocation(String id, Location target) {
        if (gameMap.containsKey(id)) {
            List<Location> hasBeenChest = gameMap.get(id);
            if(!hasBeenChest.contains(target)) {
                hasBeenChest.add(target);
                gameMap.put(id, hasBeenChest);
                return true;
            }
        }
        return false;
    }

    public void removeGamingLocation(String id, Location location) {
        if(gameMap.containsKey(id)) {
            List<Location> hasBeenChest = gameMap.get(id);
            if(hasBeenChest.contains(location)) {
                hasBeenChest.remove(location);
                gameMap.put(id, hasBeenChest);
            }
        }
    }

    public boolean chestFound(Location loc) {
        locale.debug("执行 chestFound 方法.");
        if(isGamingLocation(loc)) {
            String id = getGameID(loc);
            locale.debug("传入坐标为游戏箱子坐标.");
            Firework firework = (Firework) loc.getWorld().spawnEntity(loc, EntityType.FIREWORK);
            FireworkMeta meta = firework.getFireworkMeta();

            loc.getBlock().setType(Material.AIR);
            locale.debug("摧毁奖励箱子.");

            meta.addEffect(
                    FireworkEffect.builder()
                            .with(FireworkEffect.Type.BALL_LARGE)
                            .withColor(Color.LIME, Color.RED, Color.PURPLE, Color.ORANGE)
                            .withFade(Color.GRAY, Color.SILVER)
                            .withFlicker()
                            .withTrail()
                            .build()
            );
            firework.setFireworkMeta(meta);
            locale.debug("构建特效烟花.");

            locale.debug("开始循环奖品列表.");
            for(TreasureItem treasure : ItemManager.getInstance().getTreasures()) {
                locale.debug("奖品: " + treasure.toString());
                if(treasure.check()) {
                    loc.getWorld().dropItemNaturally(loc, treasure.item());
                    locale.debug("命中几率, 已投放奖品.");
                }
            }
            removeGamingLocation(id, loc);
            locale.debug("从已生成箱子坐标列表中移除此坐标.");

            if(gameMap.get(id).size() < 3) {
                locale.debug("剩余箱子数量小于 3, 重新生成 5 个箱子.");
                spawnChest(id, 5, true);
            }

            return true;
        }
        return false;
    }

    public boolean isGamingLocation(Location target) {
        return getGameID(target) != null;
    }

    public void startNewGame(String id, int amount) {
        gameMap.put(id, new ArrayList<>());
        spawnChest(id, amount, false);
    }

    public void stopGame(String id) {
        if(gameMap.containsKey(id)) {
            List<Location> locs = gameMap.get(id);
            if(!locs.isEmpty()) {
                for(Location loc : locs) {
                    loc.getBlock().setType(Material.AIR);
                }
            }
        }
    }

    public String getGameID(Location loc) {
        for(String id : gameMap.keySet()) {
            if(gameMap.get(id).contains(loc)) {
                return id;
            }
        }
        return null;
    }
}
