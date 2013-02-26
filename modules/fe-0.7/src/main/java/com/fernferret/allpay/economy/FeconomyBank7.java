package com.fernferret.allpay.economy;

import com.fernferret.allpay.commons.GenericBank;
import com.fernferret.allpay.commons.IncompleteBankException;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;
import org.melonbrew.fe.Fe;
import org.melonbrew.fe.loaders.FeBukkitLoader;

import java.lang.reflect.Field;
import java.util.logging.Level;

/**
 * @author krinsdeath
 */
public class FeconomyBank7 extends GenericBank {
    private final FeBukkitLoader plugin;
    private final Fe service;

    public FeconomyBank7(Plugin plugin) {
        this.plugin = (FeBukkitLoader) plugin;
        try {
            Field field = FeBukkitLoader.class.getDeclaredField("fe");
            field.setAccessible(true);
            this.service = (Fe) field.get(this.plugin);
        } catch (Exception e) {
            this.plugin.getLogger().log(Level.SEVERE, e.getLocalizedMessage(), e);
            throw new IncompleteBankException("Couldn't get API instance for Fe-economy");
        }
    }

    @Override
    public String getEconUsed() {
        return "Fe";
    }

    @Override
    protected boolean hasMoney(Player player, double money, String message) {
        return this.service.getShortenedAccount(player.getName()).getMoney() >= money;
    }

    @Override
    protected void takeMoney(Player player, double amount) {
        this.service.getShortenedAccount(player.getName()).withdraw(amount);
    }

    @Override
    protected void giveMoney(Player player, double amount) {
        this.service.getShortenedAccount(player.getName()).deposit(amount);
    }

    @Override
    protected String getFormattedMoneyAmount(Player player, double amount) {
        return this.service.getAPI().format(amount);
    }

    @Override
    protected boolean setMoneyBalance(Player player, double amount) {
        try {
            this.service.getShortenedAccount(player.getName()).setMoney(amount);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    @Override
    protected double getMoneyBalance(Player player) {
        return this.service.getShortenedAccount(player.getName()).getMoney();
    }
}
