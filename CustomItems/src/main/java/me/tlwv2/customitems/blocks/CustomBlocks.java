package me.tlwv2.customitems.blocks;

import org.bukkit.Location;
import org.bukkit.configuration.serialization.ConfigurationSerializable;
import org.bukkit.event.Listener;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Moses on 2018-01-24.
 */
public class CustomBlocks implements Listener, ConfigurationSerializable{
    public HashMap<ItemStack, AbstractCustomBlock> blocklist;

    public HashMap<Location, AbstractCustomBlock> blocks;

    @Override
    public Map<String, Object> serialize() {
        HashMap<String, Object> map = new HashMap<>();

        return map;
    }
}
