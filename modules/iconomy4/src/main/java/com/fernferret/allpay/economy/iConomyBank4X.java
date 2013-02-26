package com.fernferret.allpay.economy;

import com.fernferret.allpay.commons.GenericBank;
import com.nijiko.coelho.iConomy.iConomy;
import org.bukkit.entity.Player;

/**
 * The bank implementation for iConomy 4.
 */
public class iConomyBank4X extends GenericBank { // SUPPRESS CHECKSTYLE: TypeName

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEconUsed() {
        return "iConomy 4";
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        iConomy.getBank().getAccount(player.getName()).setBalance(amount);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasMoney(Player player, double money, String message) {
        boolean result = iConomy.getBank().getAccount(player.getName()).hasEnough(money);
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
        iConomy.getBank().getAccount(player.getName()).subtract(amount);
        showReceipt(player, amount, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
        return this.formatCurrency(amount, iConomy.getBank().getCurrency(), null);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double getMoneyBalance(Player p) {
        return iConomy.getBank().getAccount(p.getName()).getBalance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void giveMoney(Player player, double amount) {
        iConomy.getBank().getAccount(player.getName()).add(amount);
        showReceipt(player, (amount * -1), -1);
    }

}
