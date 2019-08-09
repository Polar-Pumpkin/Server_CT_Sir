package org.serverct.sir.tianfu.util;

import org.bukkit.ChatColor;
import org.serverct.sir.tianfu.Tianfu;
import org.serverct.sir.tianfu.config.TalentManager;
import org.serverct.sir.tianfu.data.PlayerData;
import org.serverct.sir.tianfu.data.Talent;
import org.serverct.sir.tianfu.enums.PlaceholderType;
import org.serverct.sir.tianfu.enums.TalentType;

import java.util.ArrayList;
import java.util.List;

public class PlaceholderUtil {

    private static LocaleUtil locale = Tianfu.getInstance().getLocale();

    public static String parse(PlaceholderType type, TalentType talentType, PlayerData data) {
        locale.debug("&7调用 (PlaceholderUtil) parse 方法.");
        locale.debug("&7目标变量: " + type.toString());
        String result;

        if(talentType != null) {
            Talent target = TalentManager.getInstance().getTalent(talentType);
            locale.debug("&7目标天赋数据: " + target.toString());
            int level = data.getLevel().get(talentType);
            int maxLevel = target.getLevels().size() - 1;
            locale.debug("&7玩家目标天赋等级: " + level + "/" + maxLevel);
            switch (type) {
                case PLAYER:
                    result = data.getPlayerName();
                    break;

                case TALENT_NAME:
                    result = target.getDisplayName();
                    break;
                case TALENT_LEVEL:
                    result = String.valueOf(level);
                    break;
                case TALENT_LEVEL_MAX:
                    result = String.valueOf(maxLevel);
                    break;
                case TALENT_LEVEL_NEXT:
                    result = String.valueOf(level >= maxLevel ? (level == maxLevel ? "&6&lMAX" : "&7&o已达到最大等级") : level + 1);
                    break;
                case TALENT_LEVEL_SHORT:
                    result = level >= maxLevel ? "&6&lMAX" : level + "&7/" + maxLevel;
                    break;
                case TALENT_AMOUNT:
                    result = String.valueOf(target.getLevels().get(level));
                    break;
                case TALENT_AMOUNT_NEXT:
                    result = String.valueOf(level >= maxLevel ? "&7&o已达到最大等级" : target.getLevels().get(level + 1));
                    break;
                case TALENT_DESC:
                    result = target.getDescription();
                    break;
                case TALENT_DESC_SHORT:
                    result = target.getDescription() + " +" + target.getLevels().get(level) + target.getSymbol();
                    break;
                case TALENT_DESC_SHORT_MAX:
                    result = target.getDescription() + " +" + target.getLevels().get(maxLevel) + target.getSymbol();
                    break;
                case TALENT_DESC_SHORT_NEXT:
                    result = level >= maxLevel ? "&7&o已达到最大等级" : target.getDescription() + " +" + target.getLevels().get(level + 1) + target.getSymbol();
                    break;
                case TALENT_SYMBOL:
                    result = target.getSymbol();
                    break;
                case TALENT_COST:
                    result = "&d" + (target.getMoney() > 0 ? target.getMoney() + " &7金币" : "") + (target.getPoint() > 0 ? ", &d" + target.getPoint() + " &7点卷": "");
                    break;
                case TALENT_COST_SHORT:
                    result = level >= maxLevel ? "&7&o已达到最大等级" : "购买下一等级花费: &d" + (target.getMoney() > 0 ? target.getMoney() + " &7金币" : "") + (target.getPoint() > 0 ? ", &d" + target.getPoint() + " &7点卷": "");
                    break;
                case TALENT_COST_MONEY:
                    result = String.valueOf(target.getMoney());
                    break;
                case TALENT_COST_POINT:
                    result = String.valueOf(target.getPoint());
                    break;
                case TALENT_PROCESS:
                    StringBuilder process = new StringBuilder();
                    int levelDiff = maxLevel - level;
                    String level_reached = ChatColor.translateAlternateColorCodes('&', Tianfu.getInstance().getConfig().getString("Setting.Level.Reached"));
                    String level_not = ChatColor.translateAlternateColorCodes('&', Tianfu.getInstance().getConfig().getString("Setting.Level.Not"));
                    if(maxLevel >= 30) {
                        level = (int) Math.floor(level / maxLevel);
                        levelDiff = 30 - level;
                    }
                    for(int i = 0; i < level; i++) {
                        process.append(level_reached);
                    }
                    for(int i = 0; i < levelDiff; i++) {
                        process.append(level_not);
                    }
                    result = process.toString();
                    break;
                default:
                    result = "&c&lERROR&7(获取变量信息遇到错误)";
                    break;
            }
        } else {
            switch (type) {
                case PLAYER:
                    result = data.getPlayerName();
                    break;

                case DAMAGE_NAME:
                    result = parse(PlaceholderType.TALENT_NAME, TalentType.DAMAGE, data);
                    break;
                case DAMAGE_LEVEL:
                    result = parse(PlaceholderType.TALENT_LEVEL, TalentType.DAMAGE, data);
                    break;
                case DAMAGE_LEVEL_MAX:
                    result = parse(PlaceholderType.TALENT_LEVEL_MAX, TalentType.DAMAGE, data);
                    break;
                case DAMAGE_LEVEL_SHORT:
                    result = parse(PlaceholderType.TALENT_LEVEL_SHORT, TalentType.DAMAGE, data);
                    break;
                case DAMAGE_AMOUNT:
                    result = parse(PlaceholderType.TALENT_AMOUNT, TalentType.DAMAGE, data);
                    break;
                case DAMAGE_DESC:
                    result = parse(PlaceholderType.TALENT_DESC, TalentType.DAMAGE, data);
                    break;
                case DAMAGE_DESC_SHORT:
                    result = parse(PlaceholderType.TALENT_DESC_SHORT, TalentType.DAMAGE, data);
                    break;
                case DAMAGE_SYMBOL:
                    result = parse(PlaceholderType.TALENT_SYMBOL, TalentType.DAMAGE, data);
                    break;
                case DAMAGE_PROCESS:
                    result = parse(PlaceholderType.TALENT_PROCESS, TalentType.DAMAGE, data);
                    break;

                case HEALTHREFILL_NAME:
                    result = parse(PlaceholderType.TALENT_NAME, TalentType.HEALTHREFILL, data);
                    break;
                case HEALTHREFILL_LEVEL:
                    result = parse(PlaceholderType.TALENT_LEVEL, TalentType.HEALTHREFILL, data);
                    break;
                case HEALTHREFILL_LEVEL_MAX:
                    result = parse(PlaceholderType.TALENT_LEVEL_MAX, TalentType.HEALTHREFILL, data);
                    break;
                case HEALTHREFILL_LEVEL_SHORT:
                    result = parse(PlaceholderType.TALENT_LEVEL_SHORT, TalentType.HEALTHREFILL, data);
                    break;
                case HEALTHREFILL_AMOUNT:
                    result = parse(PlaceholderType.TALENT_AMOUNT, TalentType.HEALTHREFILL, data);
                    break;
                case HEALTHREFILL_DESC:
                    result = parse(PlaceholderType.TALENT_DESC, TalentType.HEALTHREFILL, data);
                    break;
                case HEALTHREFILL_DESC_SHORT:
                    result = parse(PlaceholderType.TALENT_DESC_SHORT, TalentType.HEALTHREFILL, data);
                    break;
                case HEALTHREFILL_SYMBOL:
                    result = parse(PlaceholderType.TALENT_SYMBOL, TalentType.HEALTHREFILL, data);
                    break;
                case HEALTHREFILL_PROCESS:
                    result = parse(PlaceholderType.TALENT_PROCESS, TalentType.HEALTHREFILL, data);
                    break;

                case IMPRISONMENT_NAME:
                    result = parse(PlaceholderType.TALENT_NAME, TalentType.IMPRISONMENT, data);
                    break;
                case IMPRISONMENT_LEVEL:
                    result = parse(PlaceholderType.TALENT_LEVEL, TalentType.IMPRISONMENT, data);
                    break;
                case IMPRISONMENT_LEVEL_MAX:
                    result = parse(PlaceholderType.TALENT_LEVEL_MAX, TalentType.IMPRISONMENT, data);
                    break;
                case IMPRISONMENT_LEVEL_SHORT:
                    result = parse(PlaceholderType.TALENT_LEVEL_SHORT, TalentType.IMPRISONMENT, data);
                    break;
                case IMPRISONMENT_AMOUNT:
                    result = parse(PlaceholderType.TALENT_AMOUNT, TalentType.IMPRISONMENT, data);
                    break;
                case IMPRISONMENT_DESC:
                    result = parse(PlaceholderType.TALENT_DESC, TalentType.IMPRISONMENT, data);
                    break;
                case IMPRISONMENT_DESC_SHORT:
                    result = parse(PlaceholderType.TALENT_DESC_SHORT, TalentType.IMPRISONMENT, data);
                    break;
                case IMPRISONMENT_SYMBOL:
                    result = parse(PlaceholderType.TALENT_SYMBOL, TalentType.IMPRISONMENT, data);
                    break;
                case IMPRISONMENT_PROCESS:
                    result = parse(PlaceholderType.TALENT_PROCESS, TalentType.IMPRISONMENT, data);
                    break;

                case LIGHTNING_NAME:
                    result = parse(PlaceholderType.TALENT_NAME, TalentType.LIGHTNING, data);
                    break;
                case LIGHTNING_LEVEL:
                    result = parse(PlaceholderType.TALENT_LEVEL, TalentType.LIGHTNING, data);
                    break;
                case LIGHTNING_LEVEL_MAX:
                    result = parse(PlaceholderType.TALENT_LEVEL_MAX, TalentType.LIGHTNING, data);
                    break;
                case LIGHTNING_LEVEL_SHORT:
                    result = parse(PlaceholderType.TALENT_LEVEL_SHORT, TalentType.LIGHTNING, data);
                    break;
                case LIGHTNING_AMOUNT:
                    result = parse(PlaceholderType.TALENT_AMOUNT, TalentType.LIGHTNING, data);
                    break;
                case LIGHTNING_DESC:
                    result = parse(PlaceholderType.TALENT_DESC, TalentType.LIGHTNING, data);
                    break;
                case LIGHTNING_DESC_SHORT:
                    result = parse(PlaceholderType.TALENT_DESC_SHORT, TalentType.LIGHTNING, data);
                    break;
                case LIGHTNING_SYMBOL:
                    result = parse(PlaceholderType.TALENT_SYMBOL, TalentType.LIGHTNING, data);
                    break;
                case LIGHTNING_PROCESS:
                    result = parse(PlaceholderType.TALENT_PROCESS, TalentType.LIGHTNING, data);
                    break;

                case REGEN_NAME:
                    result = parse(PlaceholderType.TALENT_NAME, TalentType.REGEN, data);
                    break;
                case REGEN_LEVEL:
                    result = parse(PlaceholderType.TALENT_LEVEL, TalentType.REGEN, data);
                    break;
                case REGEN_LEVEL_MAX:
                    result = parse(PlaceholderType.TALENT_LEVEL_MAX, TalentType.REGEN, data);
                    break;
                case REGEN_LEVEL_SHORT:
                    result = parse(PlaceholderType.TALENT_LEVEL_SHORT, TalentType.REGEN, data);
                    break;
                case REGEN_AMOUNT:
                    result = parse(PlaceholderType.TALENT_AMOUNT, TalentType.REGEN, data);
                    break;
                case REGEN_DESC:
                    result = parse(PlaceholderType.TALENT_DESC, TalentType.REGEN, data);
                    break;
                case REGEN_DESC_SHORT:
                    result = parse(PlaceholderType.TALENT_DESC_SHORT, TalentType.REGEN, data);
                    break;
                case REGEN_SYMBOL:
                    result = parse(PlaceholderType.TALENT_SYMBOL, TalentType.REGEN, data);
                    break;
                case REGEN_PROCESS:
                    result = parse(PlaceholderType.TALENT_PROCESS, TalentType.REGEN, data);
                    break;

                default:
                    result = "";
                    break;
            }
        }

        locale.debug("返回变量值: " + result);
        return ChatColor.translateAlternateColorCodes('&', result);
    }

    public static String check(String text, TalentType type, PlayerData data) {
        String result = text;
        locale.debug("&7调用 (PlaceholderUtil) check 方法, 目标文本: " + result + ".");
        for(PlaceholderType placeholder : PlaceholderType.values()) {
            String placeholderName = "%" + placeholder.toString() + "%";
            locale.debug("&7生成变量名: " + placeholderName);
            if(result.contains(placeholderName)) {
                locale.debug("&7目标文本包含此变量.");
                String placeholderResult = parse(placeholder, type, data);

                result = result.replace(placeholderName, placeholderResult);
            } else {
                locale.debug("&7目标文本不包含此变量, 继续遍历.");
            }
        }
        locale.debug("&7最终返回文本: " + result);
        return result;
    }

    public static List<String> checkAll(List<String> list, TalentType type, PlayerData data) {
        List<String> result = new ArrayList<>();
        for(String text : list) {
            result.add(check(text, type, data));
        }
        return result;
    }
}
