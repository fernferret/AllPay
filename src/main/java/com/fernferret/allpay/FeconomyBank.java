package com.fernferret.allpay;

import org.bukkit.entity.Player;
import org.melonbrew.fe.Fe;

/**
 * @author krinsdeath
 */
public class FeconomyBank extends GenericBank {
    private Fe plugin;

    public FeconomyBank(Fe plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getEconUsed() {
        return "Fe";
    }

    @Override
    protected boolean hasMoney(Player player, double money, String message) {
        return this.plugin.getShortenedAccount(player.getName()).getMoney() >= money;
    }

    @Override
    protected void takeMoney(Player player, double amount) {
        this.plugin.getShortenedAccount(player.getName()).withdraw(amount);
    }

    @Override
    protected void giveMoney(Player player, double amount) {
        this.plugin.getShortenedAccount(player.getName()).deposit(amount);
    }

    @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
        return this.plugin.getAPI().format(amount);
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        try {
            this.plugin.getShortenedAccount(player.getName()).setMoney(amount);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected double getMoneyBalance(Player player) {
        return this.plugin.getShortenedAccount(player.getName()).getMoney();
    }
}
