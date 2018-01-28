package me.tlwv2.customitems.items;

import org.bukkit.entity.Player;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

/**
 * Created by Moses on 2018-01-25.
 */
public interface IBlockBreak {
    void onBlockBreak(BlockBreakEvent e, Player p, PlayerInventory pi, ItemStack i, int slot);
}
