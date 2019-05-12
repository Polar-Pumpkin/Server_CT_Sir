package org.serverct.sir.mood.hooks;

import lombok.Getter;
import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.serverct.sir.mood.Mood;

public class VaultHook {

    private static VaultHook vaultHook;

    public static VaultHook getInstance() {
        if(vaultHook == null) {
            vaultHook = new VaultHook();
        }
        return  vaultHook;
    }

    private Economy economy;

    public void loadVault() {
        if (!setupEconomy()) {
            Mood.getInstance().setVaultHook(false);
            Bukkit.getLogger().info("  > 未找到 Vault, 扣费模块不可用.");
        } else {
            Mood.getInstance().setVaultHook(true);
            Bukkit.getLogger().info("  > 已连接 Vault.");
        }
    }

    private boolean setupEconomy() {
        if(Mood.getInstance().getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = Mood.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            }
            economy = rsp.getProvider();
            Bukkit.getLogger().info(ChatColor.translateAlternateColorCodes('&', "  > 已连接 Vault."));
            return economy != null;
        }
        return false;
    }

    public double getBalances(Player player)
    {
        if (!Bukkit.getPluginManager().getPlugin("Vault").isEnabled()) {
            return 0.0D;
        }
        return this.economy.getBalance(player);
    }

    public boolean give(Player player, double amount)
    {
        if (Bukkit.getPluginManager().getPlugin("Vault").isEnabled()) {
            EconomyResponse localEconomyResponse = this.economy.depositPlayer(player, amount);
            return localEconomyResponse.transactionSuccess();
        }
        return false;
    }

    public boolean take(Player player, double amount)
    {
        if (Bukkit.getPluginManager().getPlugin("Vault").isEnabled()) {
            EconomyResponse localEconomyResponse = this.economy.withdrawPlayer(player, amount);
            return localEconomyResponse.transactionSuccess();
        }
        return false;
    }
}
