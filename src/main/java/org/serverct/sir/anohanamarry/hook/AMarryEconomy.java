package org.serverct.sir.anohanamarry.hook;

import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.entity.Player;
import org.serverct.sir.anohanamarry.ANOHANAMarry;
import org.serverct.sir.anohanamarry.configuration.Language;

public class AMarryEconomy {

    private static AMarryEconomy ame;

    public static AMarryEconomy getAMarryEconomyUtil() {
        if(ame == null) {
            ame = new AMarryEconomy();
        }
        return  ame;
    }

    private String payer;
    private String receiver;
    private int price;

    public boolean cost(OfflinePlayer player, int price) {
        if(ANOHANAMarry.getEconomy().getBalance(player) >= price) {
            ANOHANAMarry.getEconomy().withdrawPlayer(player, price);
            if(player.isOnline()) {
                Language.getInstance().sendMessage(player.getName(), "Common.Cost.Success", "info", "%economy_price%", ANOHANAMarry.getEconomy().format(price));
            }
            return true;
        } else {
            if(player.isOnline()) {
                Language.getInstance().sendMessage(player.getName(), "Common.Cost.NotEnough", "error", "%economy_price%", ANOHANAMarry.getEconomy().format(price));
            }
            return false;
        }
    }

    public boolean cost(String senderName, String receiverName, String key) {
        if(ANOHANAMarry.getINSTANCE().getConfig().getBoolean("Economy.Enable")) {
            if(key.equalsIgnoreCase("sendPropose")) {
                price = ANOHANAMarry.getINSTANCE().getConfig().getInt("Economy.Setting.Propose.Send.Price");
                payer = ANOHANAMarry.getINSTANCE().getConfig().getString("Economy.Setting.Propose.Send.Payer");
                receiver = ANOHANAMarry.getINSTANCE().getConfig().getString("Economy.Setting.Propose.Send.Receiver");
                return check(price, payer, receiver, senderName, receiverName);
            } else if(key.equalsIgnoreCase("acceptPropose")) {
                price = ANOHANAMarry.getINSTANCE().getConfig().getInt("Economy.Setting.Propose.Accept.Price");
                payer = ANOHANAMarry.getINSTANCE().getConfig().getString("Economy.Setting.Propose.Accept.Payer");
                receiver = ANOHANAMarry.getINSTANCE().getConfig().getString("Economy.Setting.Propose.Accept.Receiver");
                return check(price, payer, receiver, senderName, receiverName);
            } else if(key.equalsIgnoreCase("refusePropose")) {
                price = ANOHANAMarry.getINSTANCE().getConfig().getInt("Economy.Setting.Propose.Refuse.Price");
                payer = ANOHANAMarry.getINSTANCE().getConfig().getString("Economy.Setting.Propose.Refuse.Payer");
                receiver = ANOHANAMarry.getINSTANCE().getConfig().getString("Economy.Setting.Propose.Refuse.Receiver");
                return check(price, payer, receiver, senderName, receiverName);
            } else if(key.equalsIgnoreCase("marry")) {
                price = ANOHANAMarry.getINSTANCE().getConfig().getInt("Economy.Setting.Marry.Price");
                payer = ANOHANAMarry.getINSTANCE().getConfig().getString("Economy.Setting.Marry.Payer");
                receiver = ANOHANAMarry.getINSTANCE().getConfig().getString("Economy.Setting.Marry.Receiver");
                return check(price, payer, receiver, senderName, receiverName);
            } else if(key.equalsIgnoreCase("divorce")) {
                price = ANOHANAMarry.getINSTANCE().getConfig().getInt("Economy.Setting.Divorce.Price");
                payer = ANOHANAMarry.getINSTANCE().getConfig().getString("Economy.Setting.Divorce.Payer");
                receiver = ANOHANAMarry.getINSTANCE().getConfig().getString("Economy.Setting.Divorce.Receiver");
                return check(price, payer, receiver, senderName, receiverName);
            }
        }
        return false;
    }

    private boolean check(int price, String payer, String receiver, String senderName, String receiverName) {
        if(receiver.equalsIgnoreCase("system")) {
            if(payer.equalsIgnoreCase("sender")) {
                return removeMoney(price, senderName, "system");
            } else if(payer.equalsIgnoreCase("receiver")) {
                return removeMoney(price, receiverName, "system");
            } else return payer.equalsIgnoreCase("system");
        } else if(receiver.equalsIgnoreCase("sender")) {
            if(payer.equalsIgnoreCase("sender")) {
                return true;
            } else if(payer.equalsIgnoreCase("receiver")) {
                return removeMoney(price, receiverName, senderName);
            } else if(payer.equalsIgnoreCase("system")) {
                return removeMoney(price, "system", senderName);
            }
        } else if(receiver.equalsIgnoreCase("receiver")) {
            if(payer.equalsIgnoreCase("sender")) {
                return removeMoney(price, senderName, receiverName);
            } else if(payer.equalsIgnoreCase("receiver")) {
                return true;
            } else if(payer.equalsIgnoreCase("system")) {
                return removeMoney(price, "system", receiverName);
            }
        }
        return false;
    }

    private boolean removeMoney(int price, String payerName, String receiverName) {
        if(!payerName.equalsIgnoreCase("system")) {
            OfflinePlayer payer = Bukkit.getPlayer(payerName);
            if(ANOHANAMarry.getEconomy().getBalance(payer) >= price) {
                ANOHANAMarry.getEconomy().withdrawPlayer(payer, price);
                if(payer.isOnline()) {
                    Language.getInstance().sendMessage(payerName, "Common.Cost.Success", "info", "%economy_price%", ANOHANAMarry.getEconomy().format(price));
                }
                if(!receiverName.equalsIgnoreCase("system")) {
                    OfflinePlayer receiver = Bukkit.getPlayer(receiverName);
                    ANOHANAMarry.getEconomy().depositPlayer(receiver, price);
                    if(receiver.isOnline()) {
                        Language.getInstance().sendMessage(receiverName, "Common.Cost.Deposit", "info", "%economy_price%", ANOHANAMarry.getEconomy().format(price));
                    }
                }
                return true;
            } else {
                if(payer.isOnline()) {
                    Language.getInstance().sendMessage(payerName, "Common.Cost.NotEnough", "error", "%economy_price%", ANOHANAMarry.getEconomy().format(price));
                }
                return false;
            }
        } else {
            if(!receiverName.equalsIgnoreCase("system")) {
                OfflinePlayer receiver = Bukkit.getPlayer(receiverName);
                ANOHANAMarry.getEconomy().depositPlayer(receiver, price);
                if(receiver.isOnline()) {
                    Language.getInstance().sendMessage(receiverName, "Common.Cost.Deposit", "info", "%economy_price%", ANOHANAMarry.getEconomy().format(price));
                }
            }
            return true;
        }
    }
}
