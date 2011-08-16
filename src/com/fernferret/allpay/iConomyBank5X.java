package com.fernferret.allpay;

import org.bukkit.entity.Player;

import com.iConomy.iConomy;

/**
 * Adapter class for iConomy 5
 * 
 * @author Eric Stokes
 */
public class iConomyBank5X extends GenericBank {

    @Override
    public String getEconUsed() {
        return "iConomy 5";
    }

    public boolean hasMoney(Player player, double money, String message) {
        boolean result = iConomy.getAccount(player.getName()).getHoldings().hasEnough(money);
        if (!result) {
            userIsTooPoor(player, -1, message);
        }
        return result;
    }

    @Override
    public void payMoney(Player player, double amount) {
        iConomy.getAccount(player.getName()).getHoldings().subtract(amount);
        showReceipt(player, amount, -1);
    }

    @Override
    public String getFormattedMoneyAmount(double amount) {
        return iConomy.format(amount);
    }
}
