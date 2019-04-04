package org.serverct.sir.soulring.command.subcommand;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.serverct.sir.soulring.Attributes;
import org.serverct.sir.soulring.command.SubCommand;
import org.serverct.sir.soulring.configuration.AttributeManager;
import org.serverct.sir.soulring.configuration.LocaleManager;
import org.serverct.sir.soulring.configuration.RingManager;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class Stat implements SubCommand {

    private Map<Attributes, Double> attributesMap;
    private Map<Attributes, Double> cacheAttributesMap;

    private List<String> statMsgFormat;
    private List<String> attributesPreview;
    private List<String> resultStatMsg;

    private DecimalFormat formatter = new java.text.DecimalFormat("0");

    @Override
    public boolean execute(CommandSender sender, String[] args) {
        if(sender instanceof Player) {
            Player playerSender = (Player) sender;
            attributesMap = AttributeManager.getInstance().getAttributesFromPlayer(playerSender);
            System.out.println(attributesMap);

            for(String stat : buildStatMessage(attributesMap)) {
                playerSender.sendMessage(ChatColor.translateAlternateColorCodes('&', stat));
            }
        } else {
            sender.sendMessage(LocaleManager.getLocaleManager().getMessage("ERROR", "Plugins", "NotPlayer"));
        }

        return true;
    }

    private List<String> buildStatMessage(Map<Attributes, Double> attributesMap) {
        statMsgFormat = LocaleManager.getLocaleManager().getLocaleData().getStringList("Commands.Stats");
        resultStatMsg = new ArrayList<>(statMsgFormat);

        attributesPreview = new ArrayList<>();

        if(!attributesMap.isEmpty()) {
            for(Attributes attribute : attributesMap.keySet()) {
                if (AttributeManager.getInstance().getAllAttributes().contains(attribute)) {
                    if (AttributeManager.getInstance().hasEnabled(attribute)) {
                        if(AttributeManager.getInstance().isPercentValue(attribute)) {
                            attributesPreview.add(
                                    ChatColor.translateAlternateColorCodes(
                                            '&',
                                            RingManager.getRingManager().getSettingData().getString("AttributePreview")
                                                    .replace("%attribute%", AttributeManager.getInstance().getDisplay(attribute))
                                                    .replace("%value%", AttributeManager.getInstance().getFormattedValue(attribute, Integer.parseInt(formatter.format(attributesMap.get(attribute) / 100))))
                                    )
                            );
                        } else {
                            attributesPreview.add(
                                    ChatColor.translateAlternateColorCodes(
                                            '&',
                                            RingManager.getRingManager().getSettingData().getString("AttributePreview")
                                                    .replace("%attribute%", AttributeManager.getInstance().getDisplay(attribute))
                                                    .replace("%value%", AttributeManager.getInstance().getFormattedValue(attribute, Integer.parseInt(formatter.format(attributesMap.get(attribute)))))
                                    )
                            );
                        }
                    }
                }
            }
        } else {
            attributesPreview.add(ChatColor.translateAlternateColorCodes('&', "&c&l无."));
        }


        int msgIndex = 0;
        for(String msg : statMsgFormat) {
            if(msg.equalsIgnoreCase("%AttributesPreview%")) {
                int attributePreviewLoreRow = 0;
                for(String attributeMsg : attributesPreview) {
                    if(attributePreviewLoreRow == 0) {
                        resultStatMsg.set(msgIndex, attributeMsg);
                    } else {
                        resultStatMsg.add(msgIndex, attributeMsg);
                    }
                    attributePreviewLoreRow++;
                }
            }
            msgIndex++;
        }

        return resultStatMsg;
    }
}
