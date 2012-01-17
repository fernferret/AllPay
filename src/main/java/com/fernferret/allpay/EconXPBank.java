package com.fernferret.allpay;

import ca.agnate.EconXP.EconXP;
import org.bukkit.entity.Player;

public class EconXPBank extends GenericBank {
    private EconXP plugin;

    public EconXPBank(EconXP plugin) {
        this.plugin = plugin;
    }

    @Override
    public String getFormattedMoneyAmount(Player player, double amount) {
        return this.formatCurrency(amount, "XP", "XP");
    }

    @Override
    public boolean hasMoney(Player player, double money, String message) {
        boolean result = this.plugin.getExp(player) >= money;
        if (!result) {
            userIsTooPoor(player, -1, message);
        }
        return result;
    }

    @Override
    public void takeMoney(Player player, double amount) {
        this.plugin.removeExp(player, (int) amount);
        showReceipt(player, amount, -1);
    }

    @Override
    public String getEconUsed() {
        return "EconXP";
    }

    @Override
    protected double getMoneyBalance(Player p) {
        return this.plugin.getExp(p);
    }

    @Override
    protected void giveMoney(Player player, double amount) {
        this.plugin.addExp(player, (int) amount);
        showReceipt(player, (amount * -1), -1);
    }

}
