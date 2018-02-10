package me.tlwv2.customitems.items;

import me.tlwv2.core.utils.ItemData;
import me.tlwv2.customitems.Main;
import org.bukkit.Bukkit;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.EntityDamageByBlockEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import org.bukkit.inventory.Recipe;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moses on 2018-01-23.
 */
public class CustomItems implements Listener{
    public HashMap<ItemStack, ICustomItem> items;

    public HashMap<ItemStack, IAttack> attacks;
    public HashMap<ItemStack, IDefend> defends;
    public HashMap<ItemStack, IInteract> interacts;
    public HashMap<ItemStack, IUpdate> updates;
    public HashMap<ItemStack, IBlockBreak> breaks;

    public BukkitRunnable tickTimer;

    public CustomItems(Main plugin) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        items = new HashMap<>();
        tickTimer = new BukkitRunnable() {
            @Override
            public void run() {
                for(Player p : Bukkit.getOnlinePlayers()){
                    PlayerInventory pi = p.getInventory();
                    ItemStack[] contents = pi.getContents();

                    for(int i = 0; i < contents.length; i++){
                        ItemStack is = contents[i];
                        if(hasKey(updates, is)){
                            get(updates, is).onUpdate(p, pi, is, i);
                        }
                    }
                }
            }
        };
    }

    @EventHandler
    public void onPlayerInteract(PlayerInteractEvent e){
        ItemStack i = e.getItem();
        if(i == null){
            return;
        }

        if(hasKey(interacts, i)){
            get(interacts, i).onInteract(e);
        }
    }

    @EventHandler
    public void onEntityDamageEntity(EntityDamageByEntityEvent e){
        Entity damager = e.getDamager();
        if(damager instanceof Player){
            Player p = (Player) damager;
            PlayerInventory inventory = p.getInventory();
            ItemStack heldItem = inventory.getItemInMainHand();
            if(hasKey(attacks, heldItem)){
                get(attacks, heldItem).onAttack(e, p, heldItem, inventory.getHeldItemSlot());
            }
        }
        else {
            Entity entity = e.getEntity();
            if(entity instanceof Player){
                Player p = (Player) entity;
                ItemStack[] contents = p.getInventory().getContents();
                for(int i = 0; i < contents.length; i++){
                    if(hasKey(defends, contents[i])){
                        get(defends, contents[i]).onDefendEntity(e, p, contents[i], i);
                    }
                }
            }
        }
    }

    @EventHandler
    public void onEntityDamageBlock(EntityDamageByBlockEvent e){
        Entity entity = e.getEntity();

        if(entity instanceof Player){
            Player p = (Player)entity;

            ItemStack[] contents = p.getInventory().getContents();
            for(int i = 0; i < contents.length; i++){
                if(hasKey(defends, contents[i])){
                    get(defends, contents[i]).onDefendBlock(e, p, contents[i], i);
                }
            }
        }
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        Player p = e.getPlayer();
        PlayerInventory inv = p.getInventory();
        ItemStack i = inv.getItemInMainHand();

        if(hasKey(breaks, i)){
            get(breaks, i).onBlockBreak(e, p, inv, i, inv.getHeldItemSlot());
        }
    }

    public void registerItem(String flagName, ItemStack i, ICustomItem c){
        ItemStack flagItem = ItemData.setFlag(i, flagName);

        items.put(flagItem, c);

        if(c instanceof IAttack){
            attacks.put(flagItem, (IAttack)c);
        }
        if(c instanceof IDefend){
            defends.put(flagItem, (IDefend)c);
        }
        if(c instanceof IInteract){
            interacts.put(flagItem, (IInteract)c);
        }
        if(c instanceof IUpdate){
            updates.put(flagItem, (IUpdate)c);
        }
        if(c instanceof IBlockBreak){
            breaks.put(flagItem, (IBlockBreak)c);
        }
    }

    public void registerItem(String flagName, ItemStack i, ICustomItem c, Recipe r){
        items.put(ItemData.setFlag(i, flagName), c);
        Bukkit.addRecipe(r);
    }

    public ItemStack[] getItems(){
        return getItemsColl().toArray(new ItemStack[0]);
    }

    public Collection<ItemStack> getItemsColl(){
        return items.keySet();
    }

    //PRIVATE ------------------------------------------------------------------------------------------------------------

    private static boolean hasKey(HashMap<ItemStack, ?> map, ItemStack key){
        return map.keySet().stream().anyMatch(is -> is.isSimilar(key));
    }

    private static <T> T get(HashMap<ItemStack, T> map, ItemStack key){
        return map.entrySet().stream().filter(en -> en.getKey().isSimilar(key)).findFirst().orElseGet(() -> new Map.Entry<ItemStack, T>() {
            @Override
            public ItemStack getKey() {
                return null;
            }

            @Override
            public T getValue() {
                return null;
            }

            @Override
            public T setValue(T value) {
                return null;
            }
        }).getValue();
    }
}
