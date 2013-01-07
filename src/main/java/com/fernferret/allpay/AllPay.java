package com.fernferret.allpay;

import ca.agnate.EconXP.EconXP;
import cosine.boseconomy.BOSEconomy;
import fr.crafter.tickleman.RealEconomy.RealEconomy;
import fr.crafter.tickleman.RealPlugin.RealPlugin;
import org.bukkit.plugin.Plugin;
import org.melonbrew.fe.Fe;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * AllPay is a nifty little payment wrapper class that takes the heavy
 * lifting out of integrating payments into your plugin!
 */
public class AllPay {
    private static double version;
    private Properties props = new Properties();
    /**
     * This string is used when logging allpay versions.
     */
    protected String logPrefix = "";

    /**
     * This is the logger we're using to show console output.
     */
    protected static final Logger LOGGER = Logger.getLogger("Minecraft");
    private String prefix;
    private Plugin plugin;
    private GenericBank bank;
    private static final String[] VALID_ECON_PLUGINS =
    {"Essentials", "RealShop", "BOSEconomy", "iConomy", "MultiCurrency", "EconXP", "Fe", "Craftconomy"};

    public AllPay(Plugin plugin, String prefix) {
        try {
            props.load(this.getClass().getResourceAsStream("/allpay.properties"));
            version = Integer.parseInt(props.getProperty("version", "-1"));
        } catch (NumberFormatException e) {
            this.logBadAllPay(plugin);
        } catch (FileNotFoundException e) {
            this.logBadAllPay(plugin);
        } catch (IOException e) {
            this.logBadAllPay(plugin);
        }
        this.logPrefix = "[AllPay] - Version " + version;
        this.plugin = plugin;
        this.prefix = prefix;
    }

    /**
     * Returns an array of all valid plugins.
     * <p>
     * You can use this to determine when to load a plugin.
     * @return A String[] of all valid econ plugin names.
     */
    public static String[] getValidEconPlugins() {
        return VALID_ECON_PLUGINS;
    }

    private void logBadAllPay(Plugin plugin) {
        plugin.getLogger().log(Level.SEVERE,
                        String.format("AllPay looks corrupted, meaning this plugin (%s) is corrupted too!",
                        plugin.getDescription().getName()));
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
        this.loadFeconomy();
	this.loadCraftconomy3();
        this.loadDefaultItemEconomy();
        this.bank.setPrefix(this.prefix);
        return this.bank;
    }

    /**
     * Returns the AllPay GenericBank object that you can issue calls to and from.
     *
     * @return The GenericBank object to process payments.
     */
    public GenericBank getEconPlugin() {
        return this.bank;
    }

    /**
     * Returns the version of AllPay.
     * @return The version of AllPay.
     */
    public double getVersion() {
        return this.version;
    }

    private void loadEssentialsEconomoy() {
        if (this.bank == null) {
            try {
                Plugin essentialsPlugin = this.plugin.getServer().getPluginManager().getPlugin("Essentials");
                if (essentialsPlugin != null) {
                    this.bank = new EssentialsBank();
                    LOGGER.info(logPrefix + " - hooked into Essentials Economy for " + this.plugin.getDescription().getFullName());
                }
            } catch (Exception e) {
                LOGGER.warning(logPrefix + "You are using a VERY old version of Essentials. Please upgrade it.");
            }
        }
    }

    private void loadRealShopEconomy() {
        if (this.bank == null && !(this.bank instanceof EssentialsBank)) {
            Plugin realShopPlugin = this.plugin.getServer().getPluginManager().getPlugin("RealShop");
            if (realShopPlugin != null) {
                RealEconomy realEconPlugin = new RealEconomy((RealPlugin) realShopPlugin);
                LOGGER.info(logPrefix + " - hooked into RealEconomy for " + this.plugin.getDescription().getFullName());
                this.bank = new RealEconomyBank(realEconPlugin);
            }
        }
    }

    private void loadBOSEconomy() {
        if (this.bank == null && !(this.bank instanceof EssentialsBank)) {
            Plugin boseconPlugin = this.plugin.getServer().getPluginManager().getPlugin("BOSEconomy");
            if (boseconPlugin != null) {

                this.bank = new BOSEconomyBank((BOSEconomy) boseconPlugin);
                LOGGER.info(logPrefix + " - hooked into BOSEconomy for " + this.plugin.getDescription().getFullName());
            }
        }
    }

    private void loadEconXPEconomy() {
        if (this.bank == null && !(this.bank instanceof EssentialsBank)) {
            Plugin econXPPlugin = this.plugin.getServer().getPluginManager().getPlugin("EconXP");
            if (econXPPlugin != null) {
                this.bank = new EconXPBank((EconXP) econXPPlugin);
                LOGGER.info(logPrefix + " - hooked into EconXP for " + this.plugin.getDescription().getFullName());
            }
        }
    }

    private void loadFeconomy() {
        if (this.bank == null && !(this.bank instanceof FeconomyBank)) {
            Plugin feconplugin = this.plugin.getServer().getPluginManager().getPlugin("Fe");
            if (feconplugin != null) {
                this.bank = new FeconomyBank((Fe) feconplugin);
                LOGGER.info(logPrefix + " - hooked into Fe-conomy for " + this.plugin.getDescription().getFullName());
            }
        }
    }
    
    private void loadCraftconomy3() {
	 if (this.bank == null && !(this.bank instanceof Craftconomy3Bank)) {
            Plugin craftconomy3Test = this.plugin.getServer().getPluginManager().getPlugin("Craftconomy3");
            if (craftconomy3Test != null && craftconomy3Test instanceof com.greatmancode.craftconomy3.BukkitLoader) {
		this.bank = new Craftconomy3Bank();
                LOGGER.info(logPrefix + " - hooked into Craftconomy3 for " + this.plugin.getDescription().getFullName());
	    }
        }   
    }

    private void loadDefaultItemEconomy() {
        if (this.bank == null) {
            this.bank = new ItemBank();
            LOGGER.info(logPrefix + " - using only an item based economy for " + this.plugin.getDescription().getFullName());
        }
    }

    private void loadiConomy() {
        if (this.bank == null && !(this.bank instanceof EssentialsBank)) {
            Plugin iConomyTest = this.plugin.getServer().getPluginManager().getPlugin("iConomy");
            try {
                if (iConomyTest != null && iConomyTest instanceof com.iCo6.iConomy) {
                    this.bank = new iConomyBank6X();
                    LOGGER.info(logPrefix + " - hooked into iConomy 6 for " + this.plugin.getDescription().getFullName());
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
                LOGGER.info(logPrefix + " - hooked into iConomy 5 for " + this.plugin.getDescription().getFullName());
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
            LOGGER.info(logPrefix + " - hooked into iConomy 4 for " + this.plugin.getDescription().getFullName());
        }
    }

}
