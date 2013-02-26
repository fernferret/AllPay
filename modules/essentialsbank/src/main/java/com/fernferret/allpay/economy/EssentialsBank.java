package com.fernferret.allpay.economy;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;
import com.fernferret.allpay.commons.GenericBank;
import org.bukkit.entity.Player;

/**
 * The bank implementation for Essentials Economy.
 */
public class EssentialsBank extends GenericBank {

    @Override
    public String getEconUsed() {
        return "Essentials Economy";
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        try {
            Economy.setMoney(player.getName(), amount);
            return true;
        } catch (UserDoesNotExistException e) {
            showError(player, "You don't have an account!");
        } catch (NoLoanPermittedException e) {
            showError(player, "Your bank doesn't allow loans!");
        }
        return false;
    }

    @Override
    public String getFormattedMoneyAmount(Player player, double amount) {
        return Economy.format(amount);
    }

    @Override
    public boolean hasMoney(Player player, double money, String message) {
        try {
            return Economy.hasEnough(player.getName(), money);
        } catch (UserDoesNotExistException e) {
            return false;
        }
    }

    @Override
    public void takeMoney(Player player, double amount) {
        try {
            Economy.subtract(player.getName(), amount);
            showReceipt(player, amount, -1);
        } catch (UserDoesNotExistException e) {
            showError(player, "You don't have an account!");
        } catch (NoLoanPermittedException e) {
            showError(player, "Your bank doesn't allow loans!");
        }
        // Don't need to show receipt, Essentials already does
    }

    @Override
    protected double getMoneyBalance(Player p) {
        try {
            return Economy.getMoney(p.getName());
        } catch (UserDoesNotExistException e) {
            showError(p, "You don't have an account!");
            return 0;
        }
    }

    @Override
    public void giveMoney(Player player, double amount) {
        try {
            Economy.add(player.getName(), amount);
            showReceipt(player, (amount * -1), -1);
        } catch (UserDoesNotExistException e) {
            showError(player, "You don't have an account!");
        } catch (NoLoanPermittedException e) {
            showError(player, "Your bank doesn't allow loans!");
        }
    }

}
