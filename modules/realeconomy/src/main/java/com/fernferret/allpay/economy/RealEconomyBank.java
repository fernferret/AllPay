package com.fernferret.allpay.economy;

import com.fernferret.allpay.commons.GenericBank;
import fr.crafter.tickleman.RealEconomy.RealEconomy;
import org.bukkit.entity.Player;

/**
 * The bank implementation for RealEconomy.
 */
public class RealEconomyBank extends GenericBank {
    private RealEconomy plugin;

    public RealEconomyBank(RealEconomy plugin) {
        this.plugin = plugin;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
        // NOTE: This plugin does not support currency plurals
        return this.formatCurrency(amount, this.plugin.getCurrency(), null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasMoney(Player player, double money, String message) {
        boolean result = this.plugin.getBalance(player.getName()) >= money;
        if (!result) {
            userIsTooPoor(player, -1, message);
        }
        return result;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void takeMoney(Player player, double amount) {
        double totalmoney = this.plugin.getBalance(player.getName());
        this.plugin.setBalance(player.getName(), totalmoney - amount);
        showReceipt(player, amount, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEconUsed() {
        return "RealEconomy";
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        this.plugin.setBalance(player.getName(), amount);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double getMoneyBalance(Player p) {
        return this.plugin.getBalance(p.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void giveMoney(Player player, double amount) {
        double totalmoney = this.plugin.getBalance(player.getName());
        this.plugin.setBalance(player.getName(), totalmoney + amount);
        showReceipt(player, (amount * -1), -1);
    }

}
