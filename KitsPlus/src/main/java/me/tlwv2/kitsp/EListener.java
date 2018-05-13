package me.tlwv2.kitsp;

import me.tlwv2.core.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.attribute.Attribute;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

import java.util.Arrays;

public class EListener implements Listener {
    private Main main;

    public EListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
        this.main = plugin;
    }

    @EventHandler()
    public void onInvClick(InventoryClickEvent e){
        if(e.getInventory().getName().equals(Main.KITS_PLUS_GUI_NAME)){
            Kit k = Main.check(e.getCurrentItem());
            Player p = (Player) e.getWhoClicked();

            if(null != k){
                if(p.hasPermission("kitsplus.use." + k.getName()) || !(Main.needsPerms)) {
                    k.apply(p);
                    Main.instance().addCurrentKit(p, k);
                }
                else
                    e.getWhoClicked().sendMessage(Constants.NOPERM);

                e.getWhoClicked().closeInventory();
                e.setCancelled(true);
            }
            else
                return;
        }
    }

    @EventHandler(priority = EventPriority.HIGH)
    public void onInteract(PlayerInteractEvent e){
        Action action = e.getAction();
        Player player = e.getPlayer();
        if(action == Action.RIGHT_CLICK_BLOCK || action == Action.RIGHT_CLICK_AIR){
            PlayerInventory inventory = player.getInventory();
            ItemStack item = inventory.getItemInMainHand();
            if(item.isSimilar(Main.instance().getRefillItemStack())){
                if(Main.instance().hasKit(player)){
                    Kit kit = Main.instance().getKit(player);
                    ItemStack refillItem = Main.instance().getRefillItemStack();
                    int amount = Arrays.stream(inventory.getStorageContents())
                            .filter(f -> f != null)
                            .filter(f -> f.isSimilar(refillItem))
                            .map(ItemStack::getAmount)
                            .reduce(0, (a, b) -> a + b);
//                    player.sendMessage(amount + "");

                    kit.applyWithoutHP(player);

                    if(amount > 1){
                        ItemStack stack = Main.instance().getRefillItemStack();
                        stack.setAmount(amount - 1);
                        inventory.addItem(stack);
                    }

                    e.setCancelled(true);
                }
            }
        }
        if(action == Action.RIGHT_CLICK_BLOCK) {
            if (isKitspSign(e.getClickedBlock()))
                Bukkit.getServer().dispatchCommand(player, "kitsplus gui");
        }
    }

    @EventHandler()
    public void onSignChange(SignChangeEvent e){
        if(e.getLine(0).equals(Main.SIGN_TEXT_BEFORE)){
            if(e.getPlayer().hasPermission(Main.PLACE_SIGN_PERM)){
                e.setLine(0, Main.SIGN_TEXT_AFTER);
                e.setLine(1, "");
                e.setLine(2, "");
                e.setLine(3, "");

                e.getPlayer().sendMessage(Main.KITS_PLUS_LOGO + "Successfully placed KitsPlus Sign!");
            }
            else{
                e.getPlayer().sendMessage(Constants.NOPERM);
            }
        }
    }

    @EventHandler
    public void onDeath(PlayerDeathEvent e){
        Player player = e.getEntity();
        if(Main.instance().hasKit(player)){
            Main.instance().removeKit(player);

            player.getInventory().clear();

            Player killer = player.getKiller();
            if(killer != null && !killer.getUniqueId().equals(player.getUniqueId())){
                killer.getInventory().addItem(main.getRefillItemStack());
                killer.setHealth(killer.getAttribute(Attribute.GENERIC_MAX_HEALTH).getValue());
            }
        }
    }

    public boolean isKitspSign(Block b){
        Sign s;

        if(b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
            s = (Sign) b.getState();
        else
            return false;

        return s.getLine(0).equals(Main.SIGN_TEXT_AFTER);
    }
}
