package me.tlwv2.customitems.blocks;

import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

/**
 * Created by Moses on 2018-01-24.
 */
public class CustomBlocks {
    public HashMap<ItemStack, ICustomBlock> blocks;

    public CustomBlocks() {
        this.blocks = new HashMap<>();
    }
}
