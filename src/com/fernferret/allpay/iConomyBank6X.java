package com.fernferret.allpay;

import org.bukkit.entity.Player;

import com.iCo6.iConomy;
import com.iCo6.system.Accounts;

/**
 * Adapter class for iConomy 6
 * 
 * @author Eric Stokes
 */
public class iConomyBank6X extends GenericBank {
    private Accounts accounts;

    public iConomyBank6X() {
        this.accounts = new Accounts();
    }

    @Override
    public String getEconUsed() {
        return "iConomy 6";
    }

    @Override
    public boolean hasMoney(Player player, double money, String message) {

        boolean result = this.accounts.get(player.getName()).getHoldings().hasEnough(money);
        if (!result) {
            userIsTooPoor(player, -1, message);
        }
        return result;
    }

    @Override
    public void payMoney(Player player, double amount) {
        this.accounts.get(player.getName()).getHoldings().subtract(amount);
        showReceipt(player, amount, -1);
    }

    @Override
    public String getFormattedMoneyAmount(double amount) {
        return iConomy.format(amount);
    }
}
