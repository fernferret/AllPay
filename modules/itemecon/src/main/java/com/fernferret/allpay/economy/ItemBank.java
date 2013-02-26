package com.fernferret.allpay.economy;

import com.fernferret.allpay.commons.GenericBank;
import org.bukkit.entity.Player;

/**
 * Special bank class that handles items. If any money (item id = -1) comes in here, it will always return true and
 * never take away This class should be the default bank
 */
public class ItemBank extends GenericBank {

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
        return "";
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasMoney(Player player, double money, String message) {
        // The player always has enough money in this bank
        // someone needs to configure a bank differently if they're getting here...
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void showReceipt(Player player, double price, int item) {
        if (item != -1) {
            super.showReceipt(player, price, item);
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void takeMoney(Player player, double amount) {
        // No need to take anything away here, someone needs to configure a bank differently if they're getting here...
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEconUsed() {
        return "Simple Item Economy";
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        return false;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double getMoneyBalance(Player p) {
        // Should never get here...
        return 0;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void giveMoney(Player player, double amount) {
        // Should never get here...
    }

}
