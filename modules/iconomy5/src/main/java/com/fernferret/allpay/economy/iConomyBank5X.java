package com.fernferret.allpay.economy;

import com.fernferret.allpay.commons.GenericBank;
import com.iConomy.iConomy;
import org.bukkit.entity.Player;

/**
 * The bank implementation for iConomy 5.
 */
public class iConomyBank5X extends GenericBank { // SUPPRESS CHECKSTYLE: TypeName

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEconUsed() {
        return "iConomy 5";
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        iConomy.getAccount(player.getName()).getHoldings().set(amount);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasMoney(Player player, double money, String message) {
        boolean result = iConomy.getAccount(player.getName()).getHoldings().hasEnough(money);
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
        iConomy.getAccount(player.getName()).getHoldings().subtract(amount);
        showReceipt(player, amount, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
        return iConomy.format(amount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double getMoneyBalance(Player p) {
        return iConomy.getAccount(p.getName()).getHoldings().balance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void giveMoney(Player player, double amount) {
        iConomy.getAccount(player.getName()).getHoldings().add(amount);
        showReceipt(player, (amount * -1), -1);
    }
}
