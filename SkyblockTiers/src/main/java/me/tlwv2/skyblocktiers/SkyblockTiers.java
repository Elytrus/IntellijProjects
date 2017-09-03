package me.tlwv2.skyblocktiers;

import me.tlwv2.core.infolist.ILWrapper;
import me.tlwv2.core.utils.ItemUtil;
import me.tlwv2.skyblocktiers.commands.SkyblockMiningUpgradeCommand;
import me.tlwv2.skyblocktiers.commands.SkyblockTierManualSetCommand;
import me.tlwv2.skyblocktiers.commands.TestProbabilitiesCommand;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;
import us.talabrek.ultimateskyblock.api.IslandInfo;
import us.talabrek.ultimateskyblock.api.uSkyBlockAPI;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

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
            {0.08, 0.06, 0.04, 0.02, 0.015, 0.005, 0.002}
    };
    public static final int[] UNLOCK_AMT = {1, 1, 1, 2, 2};
    public static final String TIER_LIST_KEY = "tier_map";

    public static SkyblockTiers self;

    public Economy getEconomy() {
        return economy;
    }

    public uSkyBlockAPI getSkyblock(){
        return skyblockAPI;
    }

    private uSkyBlockAPI skyblockAPI;
    private Economy economy;

    public HashMap<String, Integer> getTierList() {
        return tierList;
    }

    private HashMap<String, Integer> tierList = new HashMap<>();

    @Override
    public void onEnable() {
        self = this;

        RegisteredServiceProvider<Economy> economyService = getServer().getServicesManager().getRegistration(Economy.class);
        ILWrapper.registerPlugin(self);

        if(economyService != null)
            economy = economyService.getProvider();

        skyblockAPI = (uSkyBlockAPI) Bukkit.getPluginManager().getPlugin("uSkyBlock");

        getCommand("miningupgrade").setExecutor(new SkyblockMiningUpgradeCommand());
        getCommand("settier").setExecutor(new SkyblockTierManualSetCommand());
        getCommand("testprobabilities").setExecutor(new TestProbabilitiesCommand());

        try {
            getConfig().load("data.yml");
        } catch (IOException e) {
            getLogger().info("Data not found, generating new data file");
            try {
                getConfig().save("data.yml");
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } catch (InvalidConfigurationException e) {
            e.printStackTrace();
        }

        if(getConfig().contains(TIER_LIST_KEY)){
            for(Map.Entry<String, Object> entry : getConfig().getConfigurationSection(TIER_LIST_KEY).getValues(true).entrySet()){
                tierList.put(entry.getKey(), (Integer)entry.getValue());
            }
        }

        new EListener(self);
    }

    public int getTier(Player p){
        IslandInfo island = skyblockAPI.getIslandInfo(p);
        String UUID = island.getLeader();
        return getTier(UUID);
    }

    public int getTier(String UUID) {
        if(tierList.containsKey(UUID))
            return tierList.get(UUID);
        else
            return 0;
    }

    public void upgradeTier(Player p){
        IslandInfo island = skyblockAPI.getIslandInfo(p);
        String UUID = island.getLeader();
        if(tierList.containsKey(UUID))
            tierList.put(UUID, tierList.get(UUID) + 1);
        else
            tierList.put(UUID, 1);
    }

    @Override
    public void onDisable() {
        getConfig().createSection(TIER_LIST_KEY, tierList);
        try {
            getConfig().save("data.yml");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setTier(String s, int tier){
        if(tier > 0)
            tierList.put(s, tier);
        else{
            if(tierList.containsKey(s))
                tierList.remove(s);
        }
    }

    public boolean canBuy(Player p){
        double bal = economy.getBalance(p);
        int tier = getTier(p);

        return bal >= UPGRADE_COSTS[tier];
    }

    public Material rollForBlock(String name){
        int tier = tierList.get(name);
        return rollForBlock(tier);
    }

    public Material rollForBlock(int tier) {
        if(tier < 1)
            return null;

        int level = Arrays.stream(Arrays.copyOfRange(UNLOCK_AMT, 0, tier)).sum();
        double[] probabilities = PROBABILITY_TABLE[tier - 1];
        Material m = null;

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
        int cost = UPGRADE_COSTS[getTier(p)];

        economy.withdrawPlayer(p, cost);
        upgradeTier(p);
    }

    public void buildUpgradeInventory(Inventory i, int tier, double balance) {
        i.setItem(4, ItemUtil.addMetadata(new ItemStack(tier < 5 ? SkyblockTiers.UPGRADE_ITEMS[tier] : Material.BARRIER),
                tier < 5 ? "\u00A7bBuy Tier " + (tier + 1) : "\u00a7cAlready at max tier!", true,
                tier < 5 ? infoText(tier + 1) : new String[0]));
        i.setItem(0, ItemUtil.addMetadata(new ItemStack(Material.GLASS), "\u00A7bCurrent Tier: " + tier, true));
        i.setItem(8, ItemUtil.addMetadata(new ItemStack(Material.EMERALD_BLOCK),
                "\u00A7aCurrent Balance: " + balance, true));
    }

    boolean chance(double x){
        return Math.random() < x;
    }

    String[] infoText(int tierTo){
        return new String[]{
                "\u00a7eCost: " + UPGRADE_COSTS[tierTo - 1],
                "\u00a7aUnlocks: " + getUpgrades(tierTo),
                tierTo == 1 ? "" : "Increased chance for unlocked ores"
        };
    }

    String getUpgrades(int tierTo){
        switch(tierTo){
            case 1:
                return "Coal";
            case 2:
                return "Iron";
            case 3:
                return "Redstone";
            case 4:
                return "Lapis and Gold";
            case 5:
                return "Diamond and Emerald";
            default:
                return "ERROR! Invalid tier specified!";
        }
    }
}
