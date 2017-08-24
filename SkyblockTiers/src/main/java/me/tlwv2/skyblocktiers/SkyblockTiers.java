package me.tlwv2.skyblocktiers;

import me.tlwv2.skyblocktiers.commands.SkyblockMiningUpgradeCommand;
import me.tlwv2.skyblocktiers.commands.SkyblockTierManualSetCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Arrays;
import java.util.HashMap;

/**
 * Created by Moses on 2017-08-22.
 */
public class SkyblockTiers extends JavaPlugin{
    public static final String UPGRADE_INVENTORY_NAME = "Skyblock Mining Upgrades";
    public static final int[] UPGRADE_COSTS = {5000, 25000, 100000, 500000, 1000000};//Y SO EXPENSIVE KRAZAK
    public static final Material[] UPGRADE_ITEMS = {Material.COAL_BLOCK, Material.IRON_BLOCK, Material.REDSTONE_BLOCK,
            Material.GOLD_BLOCK, Material.DIAMOND_BLOCK};
    public static final Material[] MATERIAL_LIST = {Material.COAL_ORE, Material.IRON_ORE,
            Material.REDSTONE_ORE, Material.LAPIS_ORE, Material.GOLD_ORE, Material.DIAMOND_ORE, Material.EMERALD_ORE};
    public static final double[][] PROBABILITY_TABLE = {
            //Coal, Iron, Redstone, Lapis, Gold, Diamond, Emerald
            {0.04, 0, 0, 0, 0, 0, 0},
            {0.05, 0.03, 0, 0, 0, 0, 0},
            {0.06, 0.04, 0.02, 0, 0, 0, 0},
            {0.07, 0.05, 0.03, 0.015, 0.01, 0, 0},
            {0.08, 0.06, 0.04, 0.02, 0.015, 0.005, 0.001}
    };
    public static final int[] UNLOCK_AMT = {1, 1, 1, 2, 2};

    public static SkyblockTiers self;

    public Economy getEconomy() {
        return economy;
    }

    private Economy economy;
    private HashMap<String, Integer> tierList = new HashMap<>();

    @Override
    public void onEnable() {
        self = this;

        RegisteredServiceProvider<Economy> economyService = getServer().getServicesManager().getRegistration(Economy.class);

        if(economyService != null)
            economy = economyService.getProvider();

        getCommand("skyblockminingupgrade").setExecutor(new SkyblockMiningUpgradeCommand());
        getCommand("skyblocktiermanualset").setExecutor(new SkyblockTierManualSetCommand());

        new EListener(self);
    }

    public int getTier(Player p){
        String UUID = p.getUniqueId().toString();
        if(tierList.containsKey(UUID))
            return tierList.get(UUID);
        else
            return 0;
    }
    
    public void upgradeTier(Player p){
        String UUID = p.getUniqueId().toString();
        if(tierList.containsKey(UUID))
            tierList.put(UUID, tierList.get(UUID) + 1);
        else
            tierList.put(UUID, 1);
    }

    public void setTier(Player p, int tier){
        tierList.put(p.getUniqueId().toString(), tier);
    }

    public boolean canBuy(Player p){
        double bal = economy.getBalance(p);
        int tier = getTier(p);

        return bal > UPGRADE_COSTS[tier - 1];
    }

    public Material rollForBlock(Player p){
        int tier = getTier(p);
        int level = Arrays.stream(Arrays.copyOfRange(UNLOCK_AMT, 0, tier - 1)).sum();
        double[] probabilities = PROBABILITY_TABLE[tier];
        Material m = null;

        //MINECRAFT FIDGET SPINNERS
        for(int i = 0; i < level; i++){
            double probability = probabilities[i];
            Material cm = MATERIAL_LIST[i];

            if(chance(probability)){
                m = cm;
                break;
            }
        }

        return m;
    }

    public void buy(Player p){
        int cost = UPGRADE_COSTS[getTier(p) - 1];

        economy.
    }

    boolean chance(double x){
        return Math.random() > x;
    }
}
