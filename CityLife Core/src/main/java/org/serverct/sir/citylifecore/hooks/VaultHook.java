package org.serverct.sir.citylifecore.hooks;

import net.milkbowl.vault.economy.Economy;
import net.milkbowl.vault.economy.EconomyResponse;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.serverct.sir.citylifecore.CityLifeCore;

public class VaultHook {

    private Economy economy;

    public void loadVault() {
        if (!setupEconomy()) {
            CityLifeCore.getInstance().setVaultHook(false);
            Bukkit.getLogger().info("  > 未找到 Vault, 扣费模块不可用.");
        } else {
            CityLifeCore.getInstance().setVaultHook(true);
            Bukkit.getLogger().info("  > 已连接 Vault.");
        }
    }

    private boolean setupEconomy() {
        if(CityLifeCore.getInstance().getServer().getPluginManager().getPlugin("Vault") != null) {
            RegisteredServiceProvider<Economy> rsp = CityLifeCore.getInstance().getServer().getServicesManager().getRegistration(Economy.class);
            if (rsp == null) {
                return false;
            }
            economy = rsp.getProvider();
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
            if(getBalances(player) >= amount) {
                EconomyResponse localEconomyResponse = this.economy.withdrawPlayer(player, amount);
                return localEconomyResponse.transactionSuccess();
            }
        }
        return false;
    }
}
