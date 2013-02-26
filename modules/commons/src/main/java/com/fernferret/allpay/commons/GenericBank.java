package com.fernferret.allpay.commons;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * This is a very simple bank interface.
 * <p>
 * This is abstract to allow some checks to be done for all banks.
 */
public abstract class GenericBank {
    private boolean receipts = true;
    private String prefix;

    /**
     * Check to ensure the player has enough items for a transaction.
     * <p>
     * You can't touch this
     * You can't touch this
     * You can't touch this
     * You can't touch this
     * You can't touch this
     *
     * @param player  Check this player's item in hand.
     * @param amount  How many items should we see if they have?
     * @param type    A valid item id. This will check to see if they have the item(s) in their hand.
     * @param message What message should the user be shown if they're too poor?
     *
     * @return true if they have enough items false if not.
     */
    protected final boolean hasItem(Player player, double amount, int type, String message) {
        boolean hasEnough = player.getInventory().contains(type, (int) amount);
        if (!hasEnough) {
            userIsTooPoor(player, type, message);
        }
        return hasEnough;
    }

    /**
     * Check to ensure the player has enough money.
     *
     * @param player  Check this player's bank/pocket for money.
     * @param money   How much money should we see if they have?
     * @param message The error message to display after the string. NULL will be passed if one is not required.
     *
     * @return true if they have enough, false if not
     */
    protected abstract boolean hasMoney(Player player, double money, String message);

    /**
     * Check to ensure the player has enough money or items.
     * This method is intended if you want to accept items or money.
     *
     * @param player  Check this player's bank/currently held item for money/items.
     * @param amount  How much money or how many items should we see if they have?
     * @param type    -1 for money, any other valid item id for items. This will check to see if they have the items in
     *                their hand.
     * @param message The error message to display after the string. NULL should be passed if one is not required.
     *
     * @return true if they have enough money/items false if not.
     */
    public final boolean hasEnough(Player player, double amount, int type, String message) {
        if (amount == 0) {
            return true;
        }
        if (type == -1) {
            return hasMoney(player, amount, message);
        } else {
            return hasItem(player, amount, type, message);
        }
    }

    /**
     * Convenience method that does not require a message.
     * <p>
     * My, my, my music hits me so hard
     * Makes me say "Oh my Lord"
     * Thank you for blessing me
     * With a mind to rhyme and two hype feet
     * It feels good, when you know you're down
     * A super dope homeboy from the Oaktown
     * And I'm known as such
     * And this is a beat, uh, you can't touch
     *
     * @param player Check this player's bank/currently held item for money/items.
     * @param amount How much money or how many items should we see if they have?
     * @param type   -1 for money, any other valid item id for items. This will check to see if they have the items in
     *               their hand.
     *
     * @return true if they have enough money/items false if not.
     */
    public final boolean hasEnough(Player player, double amount, int type) {
        return hasEnough(player, amount, type, null);
    }

    /**
     * Take the required items from the player.
     * <p>
     * I told you homeboy (You can't touch this)
     * Yeah, that's how we living and you know (You can't touch this)
     * Look at my eyes, man (You can't touch this)
     * Yo, let me bust the funky lyrics (You can't touch this)
     * <p>
     * Your plugin should never reflect to call this.
     * Just use {@link #take(org.bukkit.entity.Player, double, int)}.
     *
     * @param player The player to take from
     * @param amount How much should we take
     * @param type   What itemid should we take?
     */
    protected final void takeItem(Player player, double amount, int type) {
        int removed = 0;
        HashMap<Integer, ItemStack> items = (HashMap<Integer, ItemStack>) player.getInventory().all(type);
        for (int i : items.keySet()) {
            if (removed >= amount) {
                break;
            }
            int diff = (int) (amount - removed);
            int amt = player.getInventory().getItem(i).getAmount();
            if (amt - diff > 0) {
                player.getInventory().getItem(i).setAmount(amt - diff);
                break;
            } else {
                removed += amt;
                player.getInventory().clear(i);
            }
        }
        showReceipt(player, amount, type);
    }

    /**
     * Take the required amount of money from the player.
     * <p>
     * This is implemented by the bank that you're using.
     * <p>
     * Your plugin should never reflect to call this.
     * Just use {@link #take(org.bukkit.entity.Player, double, int)}.
     *
     * @param player The player to take from.
     * @param amount How much should we take.
     */
    protected abstract void takeMoney(Player player, double amount);

    /**
     * Take the required items/money from the player.
     * <p>
     * Fresh new kicks, advance
     * You gotta like that, now you know you wanna dance
     * So move, outta your seat
     * And get a fly girl and catch this beat
     * While it's rolling, hold on
     * Pump a little bit and let 'em know it's going on
     * Like that, like that
     * Cold on a mission so fall them back
     * Let 'em know, that you're too much
     * And this is a beat, uh, you can't touch
     *
     * @param player The player to take from
     * @param amount How much should we take
     * @param type   What should we take? (-1 for money, item id for item)
     */
    public final void take(Player player, double amount, int type) {
        if (type == -1) {
            takeMoney(player, amount);
        } else {
            takeItem(player, amount, type);
        }
    }

    /**
     * Give the specified items/money to the player.
     * <p>
     * Yo, I told you (You can't touch this)
     * Why you standing there, man? (You can't touch this)
     * Yo, sound the bell, school is in, sucka (You can't touch this)
     *
     * @param player The player to add to.
     * @param amount The amount to give.
     * @param type   The type of currency, -1 for money, itemID otherwise.
     */
    public final void give(Player player, double amount, int type) {
        if (type == -1) {
            giveMoney(player, amount);
        } else {
            giveItem(player, amount, type);
        }
    }

    /**
     * Give the specified amount of money to the player.
     *
     * @param player The player to add to.
     * @param amount The amount to give.
     */
    protected abstract void giveMoney(Player player, double amount);

    /**
     * Give the specified items/money to the player.
     * <p>
     * Give me a song, or rhythm
     * Make 'em sweat, that's what I'm giving 'em
     * Now, they know
     * You talking about the Hammer you talking about a show
     * That's hype, and tight
     * Singers are sweating so pass them a wipe
     * Or a tape, to learn
     * What's it gonna take in the 90's to burn
     * The charts? Legit
     * Either work hard or you might as well quit
     *
     * @param player the player to add to
     * @param amount the amount to give
     * @param type   the type of currency, -1 for money, itemID otherwise
     */
    protected final void giveItem(Player player, double amount, int type) {
        ItemStack item = new ItemStack(type, (int) amount);
        player.getInventory().addItem(item);
        showReceipt(player, (amount * -1), type);
    }

    /**
     * Transfers a specified amount of the type (-1 for currency, otherwise an item)
     * to the specified player, from the specified player
     * <p>
     * That's word because you know...
     * You can't touch this
     * You can't touch this
     * You can't touch this
     * Break it down! .......... Stop, Hammer time!
     *
     * @param from   The player to take from.
     * @param to     The player to give to.
     * @param amount The amount to transfer.
     * @param type   The item id (or -1, for currency).
     */
    public final void transfer(Player from, Player to, double amount, int type) {
        if (type == -1) {
            transferMoney(from, to, amount);
        } else {
            transferItem(from, to, amount, type);
        }
    }

    /**
     * Transfers money from one player to another.
     *
     * @param from   The player paying.
     * @param to     The player recieving
     * @param amount The amount of money in the transaction.
     */
    protected void transferMoney(Player from, Player to, double amount) {
        if (!hasMoney(from, amount, "")) {
            return;
        }
        takeMoney(from, amount);
        giveMoney(to, amount);
    }

    /**
     * Transfers an item from one player to another.
     * <p>
     * Go with the funk, it is said
     * That if you can't groove to this then you probably are dead
     * So wave your hands in the air
     * Bust a few moves, fun your fingers through your hair
     * This is it, for a winner
     * Dance to this and you're gonna get thinner
     * Move, slide your rump
     * Just for a minute let's all do the bump, bump, bump
     *
     * @param from   The player sending.
     * @param to     The player recieving.
     * @param amount The amount of items in the transaction.
     * @param type   The type of item in the transaction.
     */
    protected final void transferItem(Player from, Player to, double amount, int type) {
        if (!hasEnough(from, amount, type)) {
            return;
        }
        takeItem(from, amount, type);
        giveItem(to, amount, type);
    }

    /**
     * Returns a formatted string of the given amount and type.
     * If type is -1, will return a bank specific string like:
     * "5 Dollars" If type is != -1 will return an item string like: "1 Diamond"
     * <p>
     * Yeah... (You can't touch this)
     * Look, man (You can't touch this)
     * You better get hype, boy, because you know (You can't touch this)
     * Ring the bell, school's back in (You can't touch this)
     *
     * @param amount The number of money/items
     * @param type   Money(-1) or item
     *
     * @return A formatted string of the given amount and type
     */
    public final String getFormattedItemAmount(double amount, int type) {
        // If we're here, we have to assume item, this method should only get called from it's children
        Material m = Material.getMaterial(type);
        if (m != null) {
            return amount + " " + m.toString();
        }
        return "NO ITEM FOUND";
    }

    /**
     * Gets the formatted amount of money.
     * <p>
     * Example: "$5" or "7 rupees"
     *
     * @param amount The amount to format
     * @param player The player whom we are talking about
     *
     * @return A formatted amount, like $5
     */
    protected abstract String getFormattedMoneyAmount(Player player, double amount);

    /**
     * Gets the formatted amount of either an item or money.
     * <p>
     * Break it down! Stop, Hammer time!
     * You can't touch this
     * You can't touch this
     *
     * @param player The player to display the info to.
     * @param amount  The amount to display.
     * @param item   The item or -1 for money.
     *
     * @return A pretty string that could be: $5 or 6 DIRT.
     */
    public final String getFormattedAmount(Player player, double amount, int item) {
        if (item == -1) {
            return getFormattedMoneyAmount(player, amount);
        }
        return getFormattedItemAmount(amount, item);

    }

    /**
     * This method is called if a user does not have enough money or items. The message parameter allows you to
     * customize what the user does not have enough money for. The format follows: "Sorry but you do not have the
     * required [funds|items] {message}"
     * <p>
     * You can't touch this
     * You can't touch this
     * Break it down! (Nice pants, Hammer) Stop, Hammer time!
     *
     * @param player  The player to check.
     * @param item    The item to check for or -1 for money.
     * @param message The message to display to the user.
     */
    protected final void userIsTooPoor(Player player, int item, String message) {
        String type = (item == -1) ? "funds" : "items";
        if (message == null) {
            message = "";
        } else {
            message = " " + message;
        }
        player.sendMessage(ChatColor.DARK_RED + this.prefix + ChatColor.WHITE + "Sorry but you do not have the required " + type + message);
    }

    /**
     * Prints a receipt to the user.
     * This should only be called if the econ
     * plugin does not already output when money is
     * taken (Essentials does this).
     *
     * @param player The player to send the receipt to
     * @param price  The price the user was charged.
     * @param item   The item the user was charged.
     */
    protected void showReceipt(Player player, double price, int item) {
        if (receipts) {
            if (price > 0) {
                player.sendMessage(String.format("%s%s%s%s%s %s",
                        ChatColor.DARK_GREEN, this.prefix, ChatColor.WHITE,
                        "You have been charged", ChatColor.GREEN, getFormattedAmount(player, price, item)));
            } else if (price < 0) {
                player.sendMessage(String.format("%s%s%s%s %s",
                        ChatColor.DARK_GREEN, this.prefix, getFormattedAmount(player, (price * -1), item),
                        ChatColor.WHITE, "has been added to your account."));
            }
        }
    }

    /**
     * Display an error message to a user.
     *
     * @param player  The {@link Player} to display the error to.
     * @param message The message to show them.
     */
    protected void showError(Player player, String message) {
        player.sendMessage(ChatColor.DARK_RED + this.prefix + ChatColor.WHITE + message);
    }

    /**
     * Simply returns the economy being used.
     *
     * @return The economy plugin used
     */
    public abstract String getEconUsed();

    /**
     * Sets what AllPay will preface it's messages with.
     * <p>
     * Every time you see me
     * The Hammer's just so hype
     * I'm dope on the floor and I'm magic on the mic
     * Now why would I ever stop doing this?
     *
     * @param prefix The prefix to append to all AllPay messages.
     */
    public final void setPrefix(String prefix) {
        this.prefix = prefix;
    }

    /**
     * Gets a pretty formatted version of a currency.
     * <p>
     * With others making records that just don't hit
     * I've toured around the world, from London to the Bay
     * It's "Hammer, go Hammer, MC Hammer, yo Hammer"
     * And the rest can go and play
     *
     * @param amount           The amount of currency.
     * @param currencySingular The string for a singular, like 'dollar'.
     * @param currencyPlural   The string for a plural, like 'dollars'.
     *
     * @return A pretty string.
     */
    protected final String formatCurrency(double amount, String currencySingular, String currencyPlural) {
        boolean inFront = false;
        // This determines (by lenght at the moment) if we should put the name before or after the amount.
        if (currencySingular != null && currencySingular.length() == 1 && currencySingular.matches("[a-zA-Z]")) {
            inFront = true;
        }
        // If ther was no plural
        // or the amount is 1 (we'll always display the singular)
        // or if we should display in front (If we're displaying in front, the plural will never matter, for example: $ always goes in front)
        if (currencyPlural == null || amount == 1 || inFront) {
            return inFront ? currencySingular + amount : amount + " " + currencySingular;
        } else {
            return amount + " " + currencyPlural;
        }
    }

    /**
     * Sets the balance in items or currency of a user.
     *
     * @param player the player to check.
     * @param itemId the item (or -1 for money).
     * @param amount what to set the balance to.
     *
     * @return True if success; false if fail.
     */
    public boolean setBalance(Player player, int itemId, double amount) {
        if (itemId == -1) {
            return setMoneyBalance(player, amount);
        }
        return setItemAmount(player, itemId, amount);
    }

    /**
     * Sets how much money a player has.
     *
     * @param player The player to check.
     * @param amount what to set the balance to.
     *
     * @return True if the set was successful.
     */
    protected abstract boolean setMoneyBalance(Player player, double amount);

    /**
     * Sets how many items of a type a {@link Player} has.
     * <p>
     * You can't touch this
     * You can't touch this
     * You can't touch this
     * You can't touch this
     *
     * @param player The {@link Player} to check.
     * @param type   The item id to check.
     * @param amount what to set the balance to.
     *
     * @return True if success (always).
     */
    protected final boolean setItemAmount(Player player, int type, double amount) {
        int numberOfItems = this.getItemAmount(player, type);
        if (numberOfItems > amount) {
            this.takeItem(player, numberOfItems - amount, type);
        } else if (numberOfItems < amount) {
            this.giveItem(player, amount - numberOfItems, type);
        }
        return true;
    }

    /**
     * Gets the balance in items or currency of a user.
     *
     * @param player The player to check.
     * @param itemId The item (or -1 for money).
     *
     * @return The amount of something a player has.
     */
    public double getBalance(Player player, int itemId) {
        if (itemId == -1) {
            return getMoneyBalance(player);
        }
        return getItemAmount(player, itemId);
    }

    /**
     * Returns how much money a player has.
     *
     * @param player The player to check.
     *
     * @return The amount of money a {@link Player} has.
     */
    protected abstract double getMoneyBalance(Player player);

    /**
     * @deprecated This was a typo, use getItemAmount.
     * @param player deprecated
     * @param type deprecated
     * @return deprecated stuff.
     */
    @Deprecated
    protected final int getItemAnount(Player player, int type) {
        return this.getItemAmount(player, type);
    }

    /**
     * Returns how many items of a type a {@link Player} has.
     * <p>
     * You can't touch this
     * You can't touch this
     * You can't touch this
     * You can't touch this
     *
     * @param player The {@link Player} to check.
     * @param type   The item id to check.
     *
     * @return The number of items a player has.
     */
    protected final int getItemAmount(Player player, int type) {
        HashMap<Integer, ItemStack> items = (HashMap<Integer, ItemStack>) player.getInventory().all(type);
        int total = 0;
        for (int i : items.keySet()) {
            total += player.getInventory().getItem(i).getAmount();
        }
        return total;
    }

    /**
     * Allows a developer to decide if they
     * don't want to show a recipt for a transaction.
     * @param showRecipts Whether or not AllPay should display recipts to users.
     */
    public void toggleReceipts(boolean showRecipts) {
        this.receipts = showRecipts;
    }
}
