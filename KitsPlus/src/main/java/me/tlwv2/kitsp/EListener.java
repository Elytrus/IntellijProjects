package me.tlwv2.kitsp;

import me.tlwv2.core.Constants;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEvent;

public class EListener implements Listener {
    public static final String PLACESIGNPERM = "addon.kitsplus.placekitspsign";

    public EListener(Main plugin){
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler()
    public void onInvClick(InventoryClickEvent e){
        if(e.getInventory().getName().equals(Main.KITSPGUINAME)){
            Kit k = Main.check(e.getCurrentItem());

            if(null != k){
                if(e.getWhoClicked().hasPermission("kitsplus.use." + k.getName()) || !(Main.needsPerms))
                    k.apply((Player) e.getWhoClicked());
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
        if(e.getAction() == Action.RIGHT_CLICK_BLOCK){
            if(isKitspSign(e.getClickedBlock()))
                Bukkit.getServer().dispatchCommand(e.getPlayer(), "kitsplus gui");
        }
    }

    @EventHandler()
    public void onSignChange(SignChangeEvent e){
        if(e.getLine(0).equals(Main.SIGNTEXTBEFORE)){
            if(e.getPlayer().hasPermission(PLACESIGNPERM)){
                e.setLine(0, Main.SIGNTEXTAFTER);
                e.setLine(1, "");
                e.setLine(2, "");
                e.setLine(3, "");

                e.getPlayer().sendMessage(Main.KITSPLOGO + "Successfully placed KitsPlus Sign!");
            }
            else{
                e.getPlayer().sendMessage(Constants.NOPERM);
            }
        }
    }

    public boolean isKitspSign(Block b){
        Sign s;

        if(b.getType() == Material.SIGN_POST || b.getType() == Material.WALL_SIGN)
            s = (Sign) b.getState();
        else
            return false;

        return s.getLine(0).equals(Main.SIGNTEXTAFTER);
    }
}
