package me.tlwv2.bosses;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;

import me.tlwv2.bosses.bosses.LeMaxwell;
import me.tlwv2.bosses.bosses.Minion;
import me.tlwv2.bosses.commands.KillBossesCommand;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.tlwv2.bosses.bosses.Archangel;
import me.tlwv2.bosses.commands.BossItemsCommand;
import me.tlwv2.core.infolist.ILWrapper;

public class Bosses extends JavaPlugin {
    public static final String RADIUS_KEY = "protection";
    public static Bosses self;

    private CopyOnWriteArrayList<Boss> activeBosses;
    private HashMap<ItemStack, Boss> bossList;

    public double getRadius() {
        return radius;
    }

    private double radius;

    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable(){
        self = this;
        activeBosses = new CopyOnWriteArrayList<>();
        bossList = new HashMap<>();
        ILWrapper.registerPlugin(self);
//        getConfig().options().copyDefaults(true);

        Bukkit.getPluginCommand("bossitems").setExecutor(new BossItemsCommand());
        Bukkit.getPluginCommand("killbosses").setExecutor(new KillBossesCommand());

        this.registerBoss(new Archangel());
        this.registerBoss(new LeMaxwell());
        this.registerBoss(new Minion());

        File configFile = new File(getDataFolder(), "config.yml");

        if(!configFile.exists()){
            Bukkit.getLogger().warning("Config does not exist! Resetting Configuration!");
            saveDefaultConfig();
            reloadConfig();
        }

        if(!getConfig().contains(RADIUS_KEY, true) || !getConfig().isDouble(RADIUS_KEY)){
            Bukkit.getLogger().warning("Invalid Configuration Option! Resetting config!");
            configFile.delete();
            saveDefaultConfig();
            reloadConfig();
        }

        radius = getConfig().getDouble(RADIUS_KEY);

        new EListener(self);
    }

    public boolean isBossSpawnItem(ItemStack item){
        return bossList.keySet().stream().anyMatch(e -> e.isSimilar(item));
    }

    public Boss getBoss(ItemStack item){
        return bossList.entrySet().stream().filter(e -> e.getKey().isSimilar(item))
                .findFirst()
                .get().getValue();
    }

    public void spawnNewBoss(Boss boss, Location spawnLocation, Player player){
        Boss b = boss.getNewInstance();
        b.spawn(spawnLocation, player);
        activeBosses.add(b);
    }

    public void removeBoss(Boss b){
        activeBosses.remove(b);
    }

    public List<Boss> getBossList(){
        return activeBosses;
    }

    public boolean isActiveBoss(LivingEntity le){
        return activeBosses.stream()
                .map(Boss::getEntity)
                .anyMatch(le::equals);
    }

    public Boss get(LivingEntity le){
        return activeBosses.stream()
                .filter(e -> le.equals(e.getEntity()))
                .findFirst()
                .orElse(null);
    }

    public void preformBossTick(){
        activeBosses.stream()
                .forEach(e -> e.preformTickActions());
    }

    public void registerBoss(Boss b){
        bossList.put(b.getSpawnItem(), b);
    }

    public Set<ItemStack> spawnItems(){
        return bossList.keySet();
    }
}
