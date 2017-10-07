package me.tlwv2.banknotes;

import me.tlwv2.banknotes.commands.BankNoteCommand;
import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.utils.ItemData;
import me.tlwv2.core.utils.ItemUtil;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Moses on 2017-09-18.
 */
public class BankNotes extends JavaPlugin {
    public static final String AMOUNT_KEY = "amount";
    private static BankNotes self;
    public static Economy economy = null;

    @Override
    public void onEnable() {
        self = this;

        RegisteredServiceProvider<Economy> economyService = getServer().getServicesManager().getRegistration(Economy.class);
        ILWrapper.registerPlugin(self);

        if(economyService != null)
            economy = economyService.getProvider();

        new EListener(self);

        Bukkit.getPluginCommand("banknote").setExecutor(new BankNoteCommand());
    }

    @Override
    public void onDisable() {

    }

    public ItemStack constructBankNote(double amount){
        return constructCustomBankNote(Material.PAPER, 1, 0, "\u00a7aBank Note", true, amount,
                "\u00a76Worth $" + amount,
                "\u00a7aClaim with a right click");
    }

    //API
    public ItemStack constructCustomBankNote(Material type, int count, int data, String name, boolean glow, double amount, String... lore){
        ItemStack i = new ItemStack(type, 1);
        ItemUtil.addMetadata(i, name, glow, lore);

        i = ItemData.setValue(i, AMOUNT_KEY, amount);

        return i;
    }

    public boolean canCreateBankNote(Player p, double amt){
        return economy.getBalance(p) >= amt;
    }

    public boolean isBankNote(ItemStack i){
        return ItemData.hasFlag(i, AMOUNT_KEY);
    }

    public double getBalanceOfBankNote(ItemStack i){
        return ItemData.getDouble(i, AMOUNT_KEY);
    }

    public static BankNotes getInstance(){
        return self;
    }

    /*
    Don't use please
     */
    public static void breakPlugin(){
        self = null;
    }
}
