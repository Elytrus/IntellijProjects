package me.tlwv2.customitems.items;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by Moses on 2018-01-23.
 */
public abstract class ICustomItem{
    public ICustomItem() {
        //
    }

    public abstract void onInteract(PlayerInteractEvent e);
    public abstract void onTick(Player p, PlayerInventory inventory, ItemStack i, int slot);
}
