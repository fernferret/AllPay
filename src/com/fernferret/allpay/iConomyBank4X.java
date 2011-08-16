package com.fernferret.allpay;

import org.bukkit.entity.Player;
import com.nijiko.coelho.iConomy.iConomy;

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

    public boolean hasMoney(Player player, double money, String message) {
        boolean result = iConomy.getBank().getAccount(player.getName()).hasEnough(money);
        if (!result) {
            userIsTooPoor(player, -1, message);
        }
        return result;
    }

    @Override
    public void payMoney(Player player, double amount) {
        iConomy.getBank().getAccount(player.getName()).subtract(amount);
        showReceipt(player, amount, -1);
    }

    @Override
    public String getFormattedMoneyAmount(double amount) {
        return amount + " " + iConomy.getBank().getCurrency();
    }
}
