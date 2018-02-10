package me.tlwv2.customitems.blocks.inventory;

import me.tlwv2.core.utils.ItemUtil;
import me.tlwv2.customitems.Main;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Moses on 2018-01-25.
 */
public abstract class CustomInventoryHandler implements Listener{
    public static ItemStack disabled = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short)15);

    static{
        ItemUtil.addMetadata(disabled, "\u00a70-", false);
    }

    public Inventory inv;

    public CustomInventoryHandler(Main plugin, String name, int size) {
        Bukkit.getPluginManager().registerEvents(this, plugin);
        inv = Bukkit.createInventory(null, size, name);
    }

    @EventHandler
    public void onInvClick(InventoryClickEvent e){
        if(e.getInventory() != inv){ // Should be literally referring to the same inventory in memory
            return;
        }

        invClick(e);
    }

    public abstract void invClick(InventoryClickEvent e);
    public abstract void onTick(Block block);
}
