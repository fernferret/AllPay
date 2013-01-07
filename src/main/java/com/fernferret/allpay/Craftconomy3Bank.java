package com.fernferret.allpay;


import com.greatmancode.craftconomy3.account.Account;
import com.greatmancode.craftconomy3.currency.Currency;
import com.greatmancode.craftconomy3.account.AccountManager;
import com.greatmancode.craftconomy3.currency.CurrencyManager;
import com.greatmancode.craftconomy3.Common;
import org.bukkit.entity.Player;

/**
 * The bank implementation for Craftconomy3.
 */
public class Craftconomy3Bank extends GenericBank { // SUPPRESS CHECKSTYLE: TypeName
    //private Accounts accounts;
    private AccountManager accounts;
    private CurrencyManager currencies;

    public Craftconomy3Bank() {
        this.accounts = Common.getInstance().getAccountManager();
	this.currencies = Common.getInstance().getCurrencyManager();
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public String getEconUsed() {
        return "Craftconomy3";
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        Account account = this.accounts.getAccount(player.getName());
	String world = account.getWorldPlayerCurrentlyIn();
	String currency = this.currencies.getCurrencyNames().get(0);
	
	account.set(amount, world, currency);
        return true;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected boolean hasMoney(Player player, double money, String message) {
	Account account = this.accounts.getAccount(player.getName());
	String world = account.getWorldPlayerCurrentlyIn();
	String currency = this.currencies.getCurrencyNames().get(0);
	
        boolean result = account.hasEnough(money, world, currency);
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
	Account account = this.accounts.getAccount(player.getName());
	String world = account.getWorldPlayerCurrentlyIn();
	String currency = this.currencies.getCurrencyNames().get(0);
	
        account.withdraw(amount, world, currency);
        showReceipt(player, amount, -1);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
	Account account = this.accounts.getAccount(player.getName());
	String world = account.getWorldPlayerCurrentlyIn();
	String currency = this.currencies.getCurrencyNames().get(0);
	Currency cCurrency = this.currencies.getCurrency(currency);
	
        return Common.getInstance().format(world, cCurrency, amount);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected double getMoneyBalance(Player p) {
	Account account = this.accounts.getAccount(p.getName());
	String world = account.getWorldPlayerCurrentlyIn();
	String currency = this.currencies.getCurrencyNames().get(0);
	
        return account.getBalance(world, currency);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    protected void giveMoney(Player player, double amount) {
        Account account = this.accounts.getAccount(player.getName());
	String world = account.getWorldPlayerCurrentlyIn();
	String currency = this.currencies.getCurrencyNames().get(0);
	
	account.deposit(amount, world, currency);
        showReceipt(player, (amount * -1), -1);
    }
}
