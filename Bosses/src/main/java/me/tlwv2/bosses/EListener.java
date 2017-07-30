package me.tlwv2.bosses;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class EListener implements Listener {
    public static final String NO_EXPLODE_CUSTOMNAME = "noexplodeblocks";

    public EListener(Bosses plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getBlockFace() == BlockFace.UP){
            ItemStack used = e.getItem();
            if(used == null)
                return;

            if(Bosses.self.isBossSpawnItem(used)){
                //e.getPlayer().sendMessage("" + Bosses.self.getBoss(used) + " | " +
                //	Bosses.self.isBossSpawnItem(used));
                Bosses.self
                        .spawnNewBoss(Bosses.self.getBoss(used)
                                ,e.getClickedBlock().getLocation().clone().add(0, 1, 0));
                if(used.getAmount() == 1)
                    e.getPlayer().getInventory().setItemInMainHand(new ItemStack(Material.AIR));
                else
                    used.setAmount(used.getAmount() - 1);
            }
        }
    }

    @EventHandler
    public void onDeath(EntityDeathEvent e){
        LivingEntity entity = e.getEntity();
        Boss b = Bosses.self.get(entity);
        if(b != null){//sopmetimes throws nullpointerexception when boss dies
            b.death(entity.getLocation());
            e.getDrops().clear();
        }
    }

    @EventHandler
    public void onDamage(EntityDamageByEntityEvent e){
        Entity d = e.getDamager();
        if(d instanceof LivingEntity)
            if(Bosses.self.isActiveBoss((LivingEntity)d))
                e.setCancelled(true);
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent e){
        Entity entity = e.getEntity();

        if (entity.getCustomName() == null) {
            return;
        }
        if(entity.getCustomName().equals(NO_EXPLODE_CUSTOMNAME))
            e.blockList().clear();
    }
}
