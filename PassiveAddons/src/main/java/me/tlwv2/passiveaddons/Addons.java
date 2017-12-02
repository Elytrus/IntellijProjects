package me.tlwv2.passiveaddons;

import me.tlwv2.core.utils.ItemUtil;
import me.tlwv2.kitsp.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;

/**
 * Created by Moses on 2017-10-10.
 */
public class Addons extends JavaPlugin implements Listener {
    @Override
    public void onDisable() {

    }

    @Override
    public void onEnable() {
        Bukkit.getPluginManager().registerEvents(this, this);
        getLogger().info("Plugin Started!");
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        if(e.getEntity() instanceof Player){
            e.getEntity().getInventory().clear();

            Player killer = e.getEntity().getKiller();
            if(killer != null){
//                killer.getInventory().addItem(ItemUtil.addMetadata(
//                        new ItemStack(Material.BREAD), "\u00a76Compensation", true));
                killer.getInventory().addItem(Main.instance().getRefillItemStack());
            }
        }
    }

//    @EventHandler
//    public void onRespawn(PlayerRespawnEvent e){
//        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
//            e.getPlayer().getInventory().clear();
//        }, 10L);
//    }
}
