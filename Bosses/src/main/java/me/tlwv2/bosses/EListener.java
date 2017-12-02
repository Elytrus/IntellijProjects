package me.tlwv2.bosses;

import me.tlwv2.bosses.bosses.Minion;
import me.tlwv2.bosses.commands.KillBossesCommand;
import me.tlwv2.core.Constants;
import me.tlwv2.core.infolist.ILWrapper;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.BlockFace;
import org.bukkit.entity.Entity;
import org.bukkit.entity.LivingEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.PrepareItemCraftEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.CraftingInventory;
import org.bukkit.inventory.ItemStack;

import java.util.Arrays;
import java.util.Random;

public class EListener implements Listener {
    public static final String NO_EXPLODE_CUSTOMNAME = "noexplodeblocks";
    public static final String BYPASS_PERM = "addon.bypasss.bossesinspawn";

    public static final String[] TAUNTS = {
            "No.",
            "Do you knokw what that means?",
            "It means you CAN'T",
            "I find you quite persistent in your actions",
            "Look at you, so innocent and naive...",
            "Persistence is a virtue, well at least for you I guess",
            "Go away",
            "Stop.",
            "Just leave and do something else",
            "No means no",
            "Fine.... No",
            "I'm not letting you",
            "Somebody else isn't either",
            "You can't",
            "Incompetent Fool",
            "No you won't",
            "Too weak for my taste",
            "You lack the permission",
            "Not trustworthy enough",
            "Just stop",
            "Ha",
            "You lack the DETERMINATION to execute this command",
            "I don't sense enough want to execute this command yet",
            "You think you're so clever....",
            "You don't wanna know what's inside...",
            "No, I have a duty to not let you do this",
            "You lack the self worth to execute me"
    };
    public static final int N = TAUNTS.length;

    public EListener(Bosses plugin){
        Bukkit.getPluginManager().registerEvents(this, plugin);

        ILWrapper.addPerm(BYPASS_PERM, "Allows you to spawn bosses in the world spawn", Bosses.self);
    }

    @EventHandler
    public void onInteract(PlayerInteractEvent e){
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK && e.getBlockFace() == BlockFace.UP){
            ItemStack used = e.getItem();
            if(used == null)
                return;

//            e.getPlayer().sendMessage(used.isSimilar(new Minion().getSpawnItem()) + "");
//            e.getPlayer().sendMessage(new Minion().getSpawnItem().toString());
//            e.getPlayer().sendMessage(used.toString());

            if(Bosses.self.isBossSpawnItem(used)){
//                e.getPlayer().sendMessage("" + Bosses.self.getBoss(used) + " | " +
//                	Bosses.self.isBossSpawnItem(used));

                Location location = e.getClickedBlock().getLocation();
                Location l2 = location.clone();
                Location worldSpawn = location.getWorld().getSpawnLocation().clone();
                Player player = e.getPlayer();

                l2.setY(0);
                worldSpawn.setY(0);

                double distance = l2.distance(worldSpawn);
                if(!player.hasPermission(BYPASS_PERM) && distance <= Bosses.self.getRadius()){
                    player.sendMessage(Constants.WARN + "Too close to spawn point! You must spawn the mob at least " + Bosses.self.getRadius()
                        + " block(s) away but you did it " + distance + " block(s) away!");
                    return;
                }

                Bosses.self
                        .spawnNewBoss(Bosses.self.getBoss(used)
                                , location.clone().add(0, 1, 0), player);
                if(used.getAmount() == 1) {
                    used.setAmount(-1);
                }
                else
                    used.setAmount(used.getAmount() - 1);

                e.setCancelled(true);
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

    @EventHandler
    public void onCraft(PrepareItemCraftEvent e){
        CraftingInventory inv = e.getInventory();
        if(Arrays.stream(inv.getMatrix()).anyMatch(i -> Bosses.self.isBossSpawnItem(i))){
            inv.setResult(null);
        }
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        String message = e.getMessage();
        if(message.startsWith("/killall")){
            Player p = e.getPlayer();

            if(!p.hasPermission("essentials.killall")){
                return;
            }

            if(!p.hasPermission(KillBossesCommand.PERM)){
                int index = new Random().nextInt(N);
                p.sendMessage("\u00a7c" + TAUNTS[index]);
                e.setCancelled(true);
            }
            else if(!message.equals("/killall")){
                Bukkit.dispatchCommand(p, "killbosses");
            }
        }
    }
}
