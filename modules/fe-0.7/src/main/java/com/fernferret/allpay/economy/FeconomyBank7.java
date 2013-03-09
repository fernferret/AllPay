package com.fernferret.allpay.economy;

import com.fernferret.allpay.commons.GenericBank;
import com.fernferret.allpay.commons.IncompleteBankException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.melonbrew.fe.Fe;
import org.melonbrew.fe.database.Account;
import org.melonbrew.fe.loaders.FeBukkitLoader;

import java.lang.reflect.Field;
import java.util.logging.Level;

/**
 * @author krinsdeath
 */
public class FeconomyBank7 extends GenericBank {
    private final FeBukkitLoader plugin;
    private Fe service;

    public FeconomyBank7(Plugin plugin) {
        this.plugin = (FeBukkitLoader) plugin;
        getFe();
    }

    @Override
    public String getEconUsed() {
        return "Fe";
    }

    private void getFe() {
        if (this.service == null) {
            try {
                Field field = FeBukkitLoader.class.getDeclaredField("fe");
                field.setAccessible(true);
                this.service = (Fe) field.get(this.plugin);
            } catch (Exception e) {
                this.plugin.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
                throw new IncompleteBankException("Couldn't get API instance for Fe-economy");
            }
        }
    }

    @Override
    protected boolean hasMoney(Player player, double money, String message) {
        getFe();
        Account acc = this.service.getAPI().getAccount(player.getName());
        return acc != null && acc.has(money);
    }

    @Override
    protected void takeMoney(Player player, double amount) {
        getFe();
        Account acc = this.service.getAPI().getAccount(player.getName());
        if (acc != null) {
            acc.withdraw(amount);
        }
    }

    @Override
    protected void giveMoney(Player player, double amount) {
        getFe();
        Account acc = this.service.getAPI().getAccount(player.getName());
        if (acc != null) {
            acc.deposit(amount);
        }
    }

        @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
        getFe();
        return this.service.getAPI().formatNoColor(amount);
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        getFe();
        try {
            Account acc = this.service.getAPI().getAccount(player.getName());
            acc.setMoney(amount);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected double getMoneyBalance(Player player) {
        getFe();
        Account acc = this.service.getAPI().getAccount(player.getName());
        if (acc != null) {
            return acc.getMoney();
        }
        return 0D;
    }
}
