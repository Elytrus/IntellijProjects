package me.tlwv2.bosses;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.LivingEntity;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

import me.tlwv2.bosses.bosses.Archangel;
import me.tlwv2.bosses.commands.BossItemsCommand;
import me.tlwv2.core.infolist.ILWrapper;

public class Bosses extends JavaPlugin {
    public static Bosses self;

    private List<Boss> activeBosses;
    private HashMap<ItemStack, Boss> bossList;

    @Override
    public void onEnable(){
        self = this;
        activeBosses = new ArrayList<Boss>();
        bossList = new HashMap<ItemStack, Boss>();
        ILWrapper.registerPlugin(self);

        Bukkit.getPluginCommand("bossitems").setExecutor(new BossItemsCommand());

        this.registerBoss(new Archangel());

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

    public void spawnNewBoss(Boss boss, Location spawnLocation){
        Boss b = boss.getNewInstance();
        b.spawn(spawnLocation);
        activeBosses.add(b);
    }

    public void removeBoss(Boss b){
        bossList.remove(b);
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
