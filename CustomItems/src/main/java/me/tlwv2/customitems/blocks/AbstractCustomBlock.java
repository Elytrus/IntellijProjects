package me.tlwv2.customitems.blocks;

import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.inventory.ItemStack;

/**
 * Created by Moses on 2018-01-24.
 */
public abstract class AbstractCustomBlock implements ConfigurationSerializable{
    public Location l;

    public AbstractCustomBlock(Block b) {
        this.l = b.getLocation();
    }

    public abstract ItemStack[] drops();
}
