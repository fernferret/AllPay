package com.fernferret.allpay;

import org.bukkit.entity.Player;

import com.earth2me.essentials.api.Economy;
import com.earth2me.essentials.api.NoLoanPermittedException;
import com.earth2me.essentials.api.UserDoesNotExistException;

/**
 * Essentials class is on hold until they give me access to the currency string
 * @author Eric Stokes
 *
 */
public class EssentialsBank extends GenericBank {

	@Override
	public String getEconUsed() {
		return " using Essentials Economy!";
	}

	@Override
	public String getFormattedMoneyAmount(double amount) {
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
	public void payMoney(Player player, double amount) {
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
}
