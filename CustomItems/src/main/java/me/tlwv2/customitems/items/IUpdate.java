package me.tlwv2.customitems.items;

import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by Moses on 2018-01-24.
 */
public interface IUpdate {
    void onUpdate(Player p, PlayerInventory pi, ItemStack item, int slot);
}
