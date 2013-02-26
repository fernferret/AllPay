package com.fernferret.allpay.economy;

import com.fernferret.allpay.commons.GenericBank;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.melonbrew.fe.Fe;

public class FeconomyBank6 extends GenericBank {
    private final Fe plugin;

    public FeconomyBank6(Plugin plugin) {
        this.plugin = (Fe) plugin;
    }

    @Override
    protected boolean hasMoney(Player player, double money, String message) {
        return this.plugin.getAPI().getAccount(player.getName()).has(money);
    }

    @Override
    protected void takeMoney(Player player, double amount) {
        this.plugin.getAPI().getAccount(player.getName()).withdraw(amount);
    }

    @Override
    protected void giveMoney(Player player, double amount) {
        this.plugin.getAPI().getAccount(player.getName()).deposit(amount);
    }

    @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
        return this.plugin.getAPI().formatNoColor(amount);
    }

    @Override
    public String getEconUsed() {
        return "Fe";
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        this.plugin.getAPI().getAccount(player.getName()).setMoney(amount);
        return true;
    }

    @Override
    protected double getMoneyBalance(Player player) {
        return this.plugin.getAPI().getAccount(player.getName()).getMoney();
    }
}
