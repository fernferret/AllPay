package com.fernferret.allpay;

import com.nijiko.coelho.iConomy.iConomy;
import org.bukkit.entity.Player;

/**
 * Adapter class for iConomy 4
 *
 * @author Eric Stokes
 */
public class iConomyBank4X extends GenericBank {

    @Override
    public String getEconUsed() {
        return "iConomy 4";
    }

    protected boolean hasMoney(Player player, double money, String message) {
        boolean result = iConomy.getBank().getAccount(player.getName()).hasEnough(money);
        if (!result) {
            userIsTooPoor(player, -1, message);
        }
        return result;
    }

    @Override
    protected void takeMoney(Player player, double amount) {
        iConomy.getBank().getAccount(player.getName()).subtract(amount);
        showReceipt(player, amount, -1);
    }


    @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
        return this.formatCurrency(amount, iConomy.getBank().getCurrency(), null);
    }

    @Override
    protected double getMoneyBalance(Player p) {
        return iConomy.getBank().getAccount(p.getName()).getBalance();
    }

    @Override
    protected void giveMoney(Player player, double amount) {
        iConomy.getBank().getAccount(player.getName()).add(amount);
        showReceipt(player, (amount * -1), -1);
    }

}
