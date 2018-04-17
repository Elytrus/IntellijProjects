package me.tlwv2.passiveaddons;

import me.tlwv2.kitsp.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.PotionMeta;
import org.bukkit.potion.PotionEffect;

public class EListener implements Listener {
    private Addons main;

    public EListener(Addons main) {
        this.main = main;
        Bukkit.getPluginManager().registerEvents(this, main);
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
                Bukkit.getScheduler().scheduleSyncDelayedTask(main, () -> {
                    killer.setHealth(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
                }, 5l);
            }
        }
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        Action a = e.getAction();

//        if(a != null && (a == Action.RIGHT_CLICK_AIR || a == Action.RIGHT_CLICK_BLOCK)){
//            Player p = e.getPlayer();
//            ItemStack item = p.getInventory().getItemInMainHand();
//
//            if(item.hasItemMeta() && item.getItemMeta() instanceof PotionMeta){
//                PotionMeta pm = (PotionMeta) item.getItemMeta();
//                for(PotionEffect pe : pm.getCustomEffects()){
//                    p.addPotionEffect(pe);
//                }
//
//                if(item.getAmount() > 1){
//                    item.setAmount(item.getAmount() - 1);
//                }
//                else{
//                    item.setAmount(0);
//                }
//
//                e.setCancelled(true);
//            }
//        }
    }

//    @EventHandler
//    public void onRespawn(PlayerRespawnEvent e){
//        Bukkit.getScheduler().scheduleSyncDelayedTask(this, () -> {
//            e.getPlayer().getInventory().clear();
//        }, 10L);
//    }
}
