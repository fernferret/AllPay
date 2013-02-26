package com.fernferret.allpay.economy;

import com.fernferret.allpay.commons.GenericBank;
import com.iCo6.iConomy;
import com.iCo6.system.Accounts;
import org.bukkit.entity.Player;

/**
 * The bank implementation for iConomy 6.
 */
public class iConomyBank6X extends GenericBank { // SUPPRESS CHECKSTYLE: TypeName
    private Accounts accounts;

    public iConomyBank6X() {
        this.accounts = new Accounts();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEconUsed() {
        return "iConomy 6";
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        this.accounts.get(player.getName()).getHoldings().setBalance(amount);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasMoney(Player player, double money, String message) {

        boolean result = this.accounts.get(player.getName()).getHoldings().hasEnough(money);
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
        this.accounts.get(player.getName()).getHoldings().subtract(amount);
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
        return this.accounts.get(p.getName()).getHoldings().getBalance();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void giveMoney(Player player, double amount) {
        this.accounts.get(player.getName()).getHoldings().add(amount);
        showReceipt(player, (amount * -1), -1);
    }
}
