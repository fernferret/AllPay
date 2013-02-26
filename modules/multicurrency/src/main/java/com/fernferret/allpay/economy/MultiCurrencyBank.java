package com.fernferret.allpay.economy;

import com.fernferret.allpay.commons.GenericBank;
import me.ashtheking.currency.CurrencyList;
import org.bukkit.entity.Player;

/**
 * The bank implementation for MultiCurrency.
 */
public class MultiCurrencyBank extends GenericBank {

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEconUsed() {
        return "MultiCurrency";
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        CurrencyList.setValue(((String) CurrencyList.maxCurrency(player.getName())[0]), player.getName(), amount);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasMoney(Player player, double money, String message) {
        boolean result = CurrencyList.hasEnough(player.getName(), money);
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
        CurrencyList.subtract(player.getName(), amount);
        showReceipt(player, amount, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
        return this.formatCurrency(amount, ((String) CurrencyList.maxCurrency(player.getName())[0]), null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double getMoneyBalance(Player p) {
        return CurrencyList.getValue(((String) CurrencyList.maxCurrency(p.getName())[0]), p.getName());
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void giveMoney(Player player, double amount) {
        CurrencyList.add(player.getName(), amount);
        showReceipt(player, (amount * -1), -1);
    }
}
