package com.fernferret.allpay;

import ca.agnate.EconXP.EconXP;
import cosine.boseconomy.BOSEconomy;
import fr.crafter.tickleman.RealEconomy.RealEconomy;
import fr.crafter.tickleman.RealPlugin.RealPlugin;
import org.bukkit.plugin.Plugin;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AllPay is a nifty little payment wrapper class that takes the heavy lifting out of integrating payments into your
 * plugin!
 *
 * @author Eric Stokes
 */
public class AllPay {
    private static double version;
    private Properties props = new Properties();
    protected final String logPrefix = "[AllPay] - Version " + version;

    protected static final Logger log = Logger.getLogger("Minecraft");
    private String prefix;
    private Plugin plugin;
    private GenericBank bank;
    private final static String[] validEconPlugins =
            {"Essentials", "RealShop", "BOSEconomy", "iConomy", "MultiCurrency", "EconXP"};
    private static List<String> pluginsThatUseUs = new ArrayList<String>();

    public AllPay(Plugin plugin, String prefix) {
        try {
            props.load(this.getClass().getResourceAsStream("/allpay.properties"));
            version = Integer.parseInt(props.getProperty("version", "-1"));
        } catch (NumberFormatException e) {
            this.logBadAllPay(plugin, "111");
        } catch (FileNotFoundException e) {
            this.logBadAllPay(plugin, "222");
        } catch (IOException e) {
            this.logBadAllPay(plugin, "333");
        }

        this.plugin = plugin;
        this.prefix = prefix;
        pluginsThatUseUs.add(this.plugin.getDescription().getName());
    }

    /**
     * This should not be required with the new shade plugin.
     * @return
     */
    public List<String> getPluginsThatUseUs() {
        return pluginsThatUseUs;
    }

    public static String[] getValidEconPlugins() {
        return validEconPlugins;
    }

    private void logBadAllPay(Plugin plugin, String code) {
        plugin.getLogger().log(Level.SEVERE,
                        String.format("AllPay looks corrupted, meaning this plugin (%s) is corrupted too!",
                        plugin.getDescription().getName()));
        plugin.getLogger().log(Level.SEVERE, code);
    }

    /**
     * Load an econ plugin. Plugins are loaded in this order: iConomy, BOSEconomy, RealShop, Essentials and simple
     * items
     *
     * @return The GenericBank object to process payments.
     */
    public GenericBank loadEconPlugin() {
        this.loadiConomy(); // Supports both 4.x, 5.x and 6.x
        this.loadBOSEconomy();
        this.loadRealShopEconomy();
        this.loadEssentialsEconomoy();
        this.loadEconXPEconomy();
        this.loadDefaultItemEconomy();
        this.bank.setPrefix(this.prefix);
        return this.bank;
    }

    /**
     * Returns the AllPay GenericBank object that you can issue calls to and from
     *
     * @return The GenericBank object to process payments.
     */
    public GenericBank getEconPlugin() {
        return this.bank;
    }

    public double getVersion() {
        return this.version;
    }

    private void loadEssentialsEconomoy() {
        if (this.bank == null) {
            try {
                Plugin essentialsPlugin = this.plugin.getServer().getPluginManager().getPlugin("Essentials");
                if (essentialsPlugin != null) {
                    this.bank = new EssentialsBank();
                    log.info(logPrefix + " - hooked into Essentials Economy for " + this.plugin.getDescription().getFullName());
                }
            } catch (Exception e) {
                log.warning(logPrefix + "You are using a VERY old version of Essentials. Please upgrade it.");
            }
        }
    }

    private void loadRealShopEconomy() {
        if (this.bank == null && !(this.bank instanceof EssentialsBank)) {
            Plugin realShopPlugin = this.plugin.getServer().getPluginManager().getPlugin("RealShop");
            if (realShopPlugin != null) {
                RealEconomy realEconPlugin = new RealEconomy((RealPlugin) realShopPlugin);
                log.info(logPrefix + " - hooked into RealEconomy for " + this.plugin.getDescription().getFullName());
                this.bank = new RealEconomyBank(realEconPlugin);
            }
        }
    }

    private void loadBOSEconomy() {
        if (this.bank == null && !(this.bank instanceof EssentialsBank)) {
            Plugin boseconPlugin = this.plugin.getServer().getPluginManager().getPlugin("BOSEconomy");
            if (boseconPlugin != null) {

                this.bank = new BOSEconomyBank((BOSEconomy) boseconPlugin);
                log.info(logPrefix + " - hooked into BOSEconomy for " + this.plugin.getDescription().getFullName());
            }
        }
    }

    private void loadEconXPEconomy() {
            if (this.bank == null && !(this.bank instanceof EssentialsBank)) {
                Plugin econXPPlugin = this.plugin.getServer().getPluginManager().getPlugin("EconXP");
                if (econXPPlugin != null) {
                    this.bank = new EconXPBank((EconXP) econXPPlugin);
                    log.info(logPrefix + " - hooked into EconXP for " + this.plugin.getDescription().getFullName());
                }
            }
        }

    private void loadDefaultItemEconomy() {
        if (this.bank == null) {
            this.bank = new ItemBank();
            log.info(logPrefix + " - using only an item based economy for " + this.plugin.getDescription().getFullName());
        }
    }

    private void loadiConomy() {
        if (this.bank == null && !(this.bank instanceof EssentialsBank)) {
            Plugin iConomyTest = this.plugin.getServer().getPluginManager().getPlugin("iConomy");
            try {
                if (iConomyTest != null && iConomyTest instanceof com.iCo6.iConomy) {
                    this.bank = new iConomyBank6X();
                    log.info(logPrefix + " - hooked into iConomy 6 for " + this.plugin.getDescription().getFullName());
                }
            } catch (NoClassDefFoundError e) {
                loadiConomy5X(iConomyTest);
            }
        }
    }

    private void loadiConomy5X(Plugin iConomyTest) {
        try {
            if (iConomyTest != null && iConomyTest instanceof com.iConomy.iConomy) {
                this.bank = new iConomyBank5X();
                log.info(logPrefix + " - hooked into iConomy 5 for " + this.plugin.getDescription().getFullName());
            }
        } catch (NoClassDefFoundError ex) {
            if (iConomyTest != null) {
                loadiConomy4X();
            }
        }
    }

    private void loadiConomy4X() {
        com.nijiko.coelho.iConomy.iConomy iConomyPlugin = (com.nijiko.coelho.iConomy.iConomy) this.plugin.getServer().getPluginManager().getPlugin("iConomy");
        if (iConomyPlugin != null) {
            this.bank = new iConomyBank4X();
            log.info(logPrefix + " - hooked into iConomy 4 for " + this.plugin.getDescription().getFullName());
        }
    }

}
